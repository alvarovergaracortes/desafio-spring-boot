package cl.previred.task.infrastructure.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import cl.previred.task.infrastructure.web.dto.TaskRequest;
import cl.previred.taskstates.infrastructure.persistence.entity.TaskStatesEntity;
import cl.previred.taskstates.infrastructure.persistence.repository.TaskStatesJpaRepository;
import cl.previred.user.infrastructure.persistence.entity.UserEntity;
import cl.previred.user.infrastructure.persistence.repository.UserJpaRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerPOSTTest {
	@Autowired
	private MockMvc mockMvc;
    
	@Autowired 
	private ObjectMapper objectMapper;
    
	@Autowired 
	private JwtUtil jwtUtil;

    @Autowired 
    private UserJpaRepository userJpaRepository;
    
    @Autowired
    private TaskStatesJpaRepository taskStatesJpaRepository;

    private Long userId;
    private Integer taskStateId;
    private String jwtToken;

    @BeforeEach
    void setUp() {
    	// Verifica si el usuario ya existe
        UserEntity user = userJpaRepository.findByUsername("testuser")
                .orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setUsername("testuser");
                    newUser.setPassword("123456");
                    newUser.setRoles("ADMIN");
                    return userJpaRepository.save(newUser);
                });

        // Verifica si el estado ya existe
        taskStatesJpaRepository.findById(1).orElseGet(() -> {
            TaskStatesEntity state = new TaskStatesEntity();
            state.setId(1);
            state.setName("Pendiente");
            return taskStatesJpaRepository.save(state);
        });

        userId = user.getId();
        taskStateId = 1;

        jwtToken = jwtUtil.generateToken(user.getUsername(), List.of(user.getRoles()));
    }

    /**
     * Prueba que verifica si una tarea se crea correctamente (http 201
     * 
     * @throws Exception
     */
    @Test
    void createTaskSuccessfull() throws Exception {
    	TaskRequest request = new TaskRequest(
                null,
                "Tarea de integracion",
                "Descripción de la tarea",
                1,
                userJpaRepository.findByUsername("testuser").get().getId()
            );
    	
    	mockMvc.perform(post("/tasks")
    			.header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("Tarea de integracion"))
        .andExpect(jsonPath("$.description").value("Descripción de la tarea"));
    }
    
    
    /**
     * Prueba que falle la creación si el titulo está en blanco (HTTP 400).
     * 
     * @throws Exception
     */
    @Test
    void failWhenTitleIsBlank() throws Exception {
        TaskRequest request = new TaskRequest(
            null, "", "Desc", taskStateId, userId
        );

        mockMvc.perform(post("/tasks")
        		.header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
    
    
    /**
     * Prueba que falle la creacion si no se proporciona JWT (HTTP 401).
     * 
     * @throws Exception
     */
    @Test
    void returnUnauthorizedWhenNoTokenProvided() throws Exception {
        TaskRequest request = new TaskRequest(null, "Tarea", "desc", taskStateId, userId);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }
    
    /**
     * Prueba que falle la creación si se utiliza un ID de usuario inexistente (HTTP 404).
     * 
     * @throws Exception
     */
    @Test
    void failWhenUserNotFound() throws Exception {
        TaskRequest request = new TaskRequest(null, "Tarea", "desc", taskStateId, 9999L);

        mockMvc.perform(post("/tasks")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }
    
    /**
     * Prueba que verifica que un usuario sin rol ADMIN no pueda crear tareas (HTTP 403).
     * 
     * @throws Exception
     */
    @Test
    void failWhenUserRoleIsNotAdmin() throws Exception {
        // Crear usuario con rol USER
        UserEntity user = new UserEntity();
        user.setUsername("usertest");
        user.setPassword("123456");
        user.setRoles("USER");
        user = userJpaRepository.save(user);

        String userJwt = jwtUtil.generateToken(user.getUsername(), List.of("USER"));

        TaskRequest request = new TaskRequest(null, "Tarea", "desc", taskStateId, user.getId());

        mockMvc.perform(post("/tasks")
                .header("Authorization", "Bearer " + userJwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }
}
