package cl.previred.task.infrastructure.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.previred.common.security.JwtUtil;
import cl.previred.task.infrastructure.persistence.entity.TaskEntity;
import cl.previred.task.infrastructure.persistence.repository.TaskJpaRepository;
import cl.previred.task.infrastructure.web.dto.TaskRequest;
import cl.previred.taskstates.infrastructure.persistence.entity.TaskStatesEntity;
import cl.previred.taskstates.infrastructure.persistence.repository.TaskStatesJpaRepository;
import cl.previred.user.infrastructure.persistence.entity.UserEntity;
import cl.previred.user.infrastructure.persistence.repository.UserJpaRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerPUTTest {
	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	@Autowired private JwtUtil jwtUtil;
	@Autowired private TaskJpaRepository taskJpaRepository;
	@Autowired private UserJpaRepository userJpaRepository;
	@Autowired private TaskStatesJpaRepository taskStatesJpaRepository;

	private Long userId;
	private String jwtToken;
	private Long taskId;

	@BeforeEach
	void setUp() {
		UserEntity user = userJpaRepository.findByUsername("testuser")
			.orElseGet(() -> {
				UserEntity newUser = new UserEntity();
				newUser.setUsername("testuser");
				newUser.setPassword("123456");
				newUser.setRoles("ADMIN");
				return userJpaRepository.save(newUser);
			});
		
		
		userId = user.getId();
		jwtToken = jwtUtil.generateToken(user.getUsername(), List.of(user.getRoles()));

		TaskStatesEntity state = taskStatesJpaRepository.findById(1)
			.orElseGet(() -> {
				TaskStatesEntity s = new TaskStatesEntity();
				s.setId(1);
				s.setName("ACTIVA");
				return taskStatesJpaRepository.save(s);
			});

		TaskEntity task = new TaskEntity();
		task.setTitle("Tarea original");
		task.setDescription("Descripción original");
		task.setUserEntity(user);
		task.setTaskStates(state);
		task = taskJpaRepository.save(task);
		taskId = task.getId();
	}

	/**
	 * Verifica que una tarea existente pueda actualizarse correctamente con datos validos.
	 * 
	 * @throws Exception
	 */
	@Test
	void updateTaskSuccessfully() throws Exception {
		TaskRequest updateRequest = new TaskRequest(
			taskId,
			"Tarea actualizada",
			"Descripcion actualizada",
			1,
			userId
		);

		mockMvc.perform(put("/tasks/" + taskId)
				.header("Authorization", "Bearer " + jwtToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("Tarea actualizada"))
			.andExpect(jsonPath("$.description").value("Descripcion actualizada"));
	}

	/**
	 * Verifica que se devuelva 404 si se intenta actualizar una tarea inexistente.
	 * 
	 * @throws Exception
	 */
	@Test
	void returnNotFoundIfTaskDoesNotExist() throws Exception {
		TaskRequest request = new TaskRequest(999L, "Título", "Desc", 1, userId);

		mockMvc.perform(put("/tasks/999")
				.header("Authorization", "Bearer " + jwtToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNotFound());
	}

	/**
	 * Verifica que se devuelva 400 si el título está vacío (validación fallida).
	 * 
	 * @throws Exception
	 */
	@Test
	void returnBadRequestIfTitleIsBlank() throws Exception {
		TaskRequest request = new TaskRequest(taskId, "", "Desc", 1, userId);

		mockMvc.perform(put("/tasks/" + taskId)
				.header("Authorization", "Bearer " + jwtToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest());
	}

	/**
	 * Verifica que se devuelva 401 si no se proporciona un token JWT.
	 * 
	 * @throws Exception
	 */
	@Test
	void returnUnauthorizedIfNoToken() throws Exception {
		TaskRequest request = new TaskRequest(taskId, "Titulo", "Desc", 1, userId);

		mockMvc.perform(put("/tasks/" + taskId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isUnauthorized());
	}
	
	/**
	 * Verifica que un usuario con rol USER no pueda actualizar tareas (HTTP 403).
	 * 
	 * @throws Exception
	 */
	@Test
	void returnForbiddenIfUserRoleIsNotAdmin() throws Exception {
	    // Crear usuario con rol USER
	    UserEntity user = new UserEntity();
	    user.setUsername("usertest");
	    user.setPassword("123456");
	    user.setRoles("USER");
	    user = userJpaRepository.save(user);

	    String userJwt = jwtUtil.generateToken(user.getUsername(), List.of("USER"));

	    TaskRequest updateRequest = new TaskRequest(
	        taskId,
	        "Título modificado",
	        "Descripción modificada",
	        1,
	        user.getId()
	    );

	    mockMvc.perform(put("/tasks/" + taskId)
	            .header("Authorization", "Bearer " + userJwt)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(updateRequest)))
	        .andExpect(status().isForbidden());
	}
}