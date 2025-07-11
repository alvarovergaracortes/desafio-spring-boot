package cl.previred.task.infrastructure.web.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.previred.common.dto.ErrorResponse;
import cl.previred.common.security.JwtUtil;
import cl.previred.task.application.service.TaskService;
import cl.previred.task.domain.modelo.Task;
import cl.previred.task.infrastructure.web.dto.TaskRequest;
import cl.previred.task.infrastructure.web.dto.TaskResponse;
import cl.previred.task.infrastructure.web.mapper.TaskMapper;
import cl.previred.user.application.service.UserService;
import cl.previred.user.domain.modelo.UserRoleEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(name = "TaskController", description = "Realiza Crud a las tareas de usuario")
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService; 
    private final TaskMapper taskMapper;
    private final JwtUtil jwtUtil;

	public TaskController(TaskService taskService, UserService userService, TaskMapper taskMapper, JwtUtil jwtUtil) {
		super();
		this.taskService = taskService;
		this.userService = userService;
		this.taskMapper = taskMapper;
		this.jwtUtil = jwtUtil;
	}


	@Operation(summary = "Inserta los datos de un Task", description = "")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Creacion del task exitosa"),
        @ApiResponse(responseCode = "404", description = "Proveedor no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponse> create(@RequestBody @Valid TaskRequest taskRequest) {
		Task taskCreated = taskMapper.toDomain(taskRequest);
		
		Task task = taskService.create(taskCreated);
		TaskResponse response = taskMapper.toResponse(task);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Actualiza los datos de un Task", description = "")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Actualizacion del task exitosa"),
        @ApiResponse(responseCode = "404", description = "Task no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponse> update(@PathVariable Long id, @Valid @RequestBody TaskRequest taskRequest) {
    	Task task = taskMapper.toDomain(taskRequest);
    	Task taskUpdate = taskService.updateTask(id, task);
    	return ResponseEntity.ok(taskMapper.toResponse(taskUpdate));
    }
    
    @Operation(summary = "Elimina un Task", description = "")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Eliminacion de Task exitosa"),
        @ApiResponse(responseCode = "404", description = "Task no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
    	taskService.deleteTask(id);
    	return ResponseEntity.noContent().build();
    }
    

    
    @Operation(summary = "Obtiene una lista de Task", description = "")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lsta de tasks exitosa"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<TaskResponse>> findAll() {
		return ResponseEntity.ok(taskService.findAll().stream()
				.map(taskMapper::toResponse)
				.toList()
				);
	}
    
    @Operation(summary = "Obtiene un Task", description = "")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "busqueda de Task exitosa"),
    		@ApiResponse(responseCode = "404", description = "Task no encontrado"),
    		@ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable Long id) {
    	Task task = taskService.findById(id);
    	return ResponseEntity.ok(taskMapper.toResponse(task));
    }
    
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> findByUserEntityId(@PathVariable Long userId,
    		@RequestHeader("Authorization") String authHeader) {

    	String token = authHeader.replace("Bearer ", "");
    	String username = jwtUtil.extractAllClaims(token).getSubject();
    	
    	List<String> roles = jwtUtil.extractRoles(token);
    	
    	if (!roles.contains(UserRoleEnum.ADMIN.name())) {
        	Long authenticatedUserId = userService.findByUsername(username).getId();
        	
        	if (!authenticatedUserId.equals(userId)) {
        		return ResponseEntity
        				.status(HttpStatus.FORBIDDEN)
        				.body(new ErrorResponse("No tienes permisos para ver tareas de otros usuarios"));
        	}
        }
    	
    	List<Task> tasks = taskService.findByUserEntityId(userId);
    	List<TaskResponse> responses = tasks.stream()
    			.map(taskMapper::toResponse)
    			.toList();
    
    	return ResponseEntity.ok(responses);
    }

}