package cl.previred.task.infrastructure.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import cl.previred.common.security.JwtUtil;
import cl.previred.task.infrastructure.persistence.entity.TaskEntity;
import cl.previred.task.infrastructure.persistence.repository.TaskJpaRepository;
import cl.previred.taskstates.infrastructure.persistence.entity.TaskStatesEntity;
import cl.previred.taskstates.infrastructure.persistence.repository.TaskStatesJpaRepository;
import cl.previred.user.infrastructure.persistence.entity.UserEntity;
import cl.previred.user.infrastructure.persistence.repository.UserJpaRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerGETTest {
	@Autowired private MockMvc mockMvc;
	@Autowired private JwtUtil jwtUtil;
	@Autowired private TaskJpaRepository taskJpaRepository;
	@Autowired private UserJpaRepository userJpaRepository;
	@Autowired private TaskStatesJpaRepository taskStatesJpaRepository;

	private Long userId;
	private String jwtToken;
	private Long taskId;
	
	/**
	 * Se ejecuta antes de cada test
	 *  
	 */
	@BeforeEach
	void setUp() {
		UserEntity admin = userJpaRepository.findByUsername("testuser")
			.orElseGet(() -> {
				UserEntity newUser = new UserEntity();
				newUser.setUsername("testuser");
				newUser.setPassword("123456");
				newUser.setRoles("ADMIN");
				return userJpaRepository.save(newUser);
			});
		userId = admin.getId();
		jwtToken = jwtUtil.generateToken(admin.getUsername(), List.of(admin.getRoles()));

		TaskStatesEntity state = taskStatesJpaRepository.findById(1)
			.orElseGet(() -> {
				TaskStatesEntity s = new TaskStatesEntity();
				s.setId(1);
				s.setName("Pendiente");
				return taskStatesJpaRepository.save(s);
			});

		// Crear una tarea de prueba
		TaskEntity task = new TaskEntity();
		task.setTitle("Tarea GET");
		task.setDescription("Descripción GET");
		task.setUserEntity(admin);
		task.setTaskStates(state);
		task = taskJpaRepository.save(task);
		taskId = task.getId();
	}

	/**
	 * Verifica que se puede obtener una tarea existente por su ID, siempre que se entregue un token JWT valido.
	 * 
	 * @throws Exception
	 */
	@Test
	void returnTaskById() throws Exception {
		mockMvc.perform(get("/tasks/" + taskId)
				.header("Authorization", "Bearer " + jwtToken)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("Tarea GET"))
			.andExpect(jsonPath("$.description").value("Descripción GET"));
	}

	
	/**
	 * Verifica que al solicitar una tarea no existente, la api responda correctamente con un error 404
	 * 
	 * @throws Exception
	 */
	@Test
	void returnNotFoundWhenTaskDoesNotExist() throws Exception {
		mockMvc.perform(get("/tasks/99999")
				.header("Authorization", "Bearer " + jwtToken)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}

	/**
	 * Verifica que el acceso a una tarea el usuario se autentique.
	 * 
	 * @throws Exception
	 */
	@Test
	void returnUnauthorizedWhenNoTokenProvided() throws Exception {
		mockMvc.perform(get("/tasks/" + taskId)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
	}
	
	/**
	 * Verifica la obtencion de todas las tareas de un usuario, retorna correctamente las tareas 
	 * asignadas si el usuario tiene permisos (su rol es: ADMIN).
	 * 
	 * @throws Exception
	 */
	@Test
	void returnTasksForUserById() throws Exception {
		mockMvc.perform(get("/tasks/users/" + userId)
				.header("Authorization", "Bearer " + jwtToken)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].title").value("Tarea GET"))
			.andExpect(jsonPath("$[0].description").value("Descripción GET"));
	}
	
	
	/**
	 * Verifica que la obtención de tareas por usuario también exige autenticación.
	 * 
	 * @throws Exception
	 */
	@Test
	void returnUnauthorizedWhenNoTokenProvidedForUserTasks() throws Exception {
		mockMvc.perform(get("/tasks/users/" + userId)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
	}
}
