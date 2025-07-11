package cl.previred.task.infrastructure.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class TaskControllerDELETETest {
	@Autowired private MockMvc mockMvc;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private TaskJpaRepository taskJpaRepository;
    @Autowired private UserJpaRepository userJpaRepository;
    @Autowired private TaskStatesJpaRepository taskStatesJpaRepository;

    private Long userId;
    private Long taskId;
    private String jwtToken;

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
    	task.setTitle("Tarea para eliminar");
    	task.setDescription("Descripción");
    	task.setUserEntity(user);
    	task.setTaskStates(state);
    	task = taskJpaRepository.save(task);
    	
    	taskId = task.getId();
    }

    /**
     * Verifica que una tarea existente se puede eliminar correctamente 
     * cuando se incluye un JWT válido. Espera codigo 204 No Content.
     *  
     * @throws Exception
     */
    @Test
    void deleteTaskSuccessfully() throws Exception {
    	mockMvc.perform(delete("/tasks/{id}", taskId)
    			.header("Authorization", "Bearer " + jwtToken))
    		.andExpect(status().isNoContent());
    }

    /**
     * Verifica que al intentar eliminar una tarea inexistente,
     * el sistema responde con 404 Not Found.
     * @throws Exception
     */
    @Test
    void returnNotFoundWhenDeletingNonExistingTask() throws Exception {
    	mockMvc.perform(delete("/tasks/{id}", 9999)
    			.header("Authorization", "Bearer " + jwtToken))
    		.andExpect(status().isNotFound());
    }
    

    /**
     * Verifica que el endpoint rechaza solicitudes de eliminación si no 
     * se proporciona un token JWT. Espera código 401 Unauthorized.
     * 
     * @throws Exception
     */
    @Test
    void returnUnauthorizedWhenNoTokenProvided() throws Exception {
    	mockMvc.perform(delete("/tasks/{id}", taskId))
    		.andExpect(status().isUnauthorized());
    }
}
