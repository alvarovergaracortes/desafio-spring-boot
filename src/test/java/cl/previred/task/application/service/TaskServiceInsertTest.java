package cl.previred.task.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.previred.common.exception.ResourceNotFoundException;
import cl.previred.task.domain.modelo.Task;
import cl.previred.task.infrastructure.persistence.entity.TaskEntity;
import cl.previred.task.infrastructure.persistence.repository.TaskJpaRepository;
import cl.previred.task.infrastructure.web.mapper.TaskMapper;
import cl.previred.taskstates.application.service.TaskStatesService;
import cl.previred.taskstates.domain.modelo.TaskStates;
import cl.previred.taskstates.infrastructure.persistence.entity.TaskStatesEntity;
import cl.previred.taskstates.infrastructure.web.mapper.TaskStatesMapper;
import cl.previred.user.application.service.UserService;
import cl.previred.user.infrastructure.persistence.entity.UserEntity;
import cl.previred.user.infrastructure.web.mapper.UserMapper;

@ExtendWith(MockitoExtension.class)
public class TaskServiceInsertTest {
	@Mock 
	private TaskJpaRepository taskJpaRepository;
	
    @Mock 
    private UserService userService;
    
    @Mock 
    private TaskStatesService taskStatesService;
    
    @Mock 
    private TaskMapper taskMapper;
    
    @Mock 
    private UserMapper userMapper;
    
    @Mock 
    private TaskStatesMapper taskStatesMapper;
    
    @InjectMocks
    private TaskServiceImpl taskService;
    
    @Test
    void shouldCreateTaskSuccessfully_test() {
        // Given
        Task task = new Task(null, "Titulo", "Desc", null, 1, 1L);
        TaskStates mockState = new TaskStates(1, "ACTIVA");

        UserEntity userEntity = new UserEntity();
        TaskStatesEntity stateEntity = new TaskStatesEntity();
        TaskEntity savedEntity = new TaskEntity();

        
        when(userService.findEntityById(1L)).thenReturn(userEntity); // ← usar findEntityById aquí
        when(taskStatesService.findById(1)).thenReturn(mockState);
        when(taskStatesMapper.toEntity(mockState)).thenReturn(stateEntity);
        when(taskMapper.toEntity(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(savedEntity);
        when(taskJpaRepository.save(Mockito.any(TaskEntity.class))).thenReturn(savedEntity); // <-- matcher genérico
        when(taskMapper.toDomain(savedEntity)).thenReturn(task);

        // When
        Task result = taskService.create(task);

        // Then
        assertNotNull(result);
        assertEquals("Titulo", result.getTitle());
        verify(taskJpaRepository).save(savedEntity);
    }

    @Test
    void shouldThrowWhenUserNotFound_test() {
        // Given
        Task task = new Task(null, "Titulo", "Desc", null, 1, 99L);
        when(userService.findEntityById(99L)).thenThrow(new ResourceNotFoundException("Usuario no encontrado"));

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> taskService.create(task));
    }

    @Test
    void shouldFindTaskById_test() {
        TaskEntity entity = new TaskEntity();
        Task domain = new Task(1L, "Tarea", "desc", null, 1, 1L);
        when(taskJpaRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(taskMapper.toDomain(entity)).thenReturn(domain);

        Task found = taskService.findById(1L);

        assertEquals(1L, found.getId());
    }

    @Test
    void shouldThrowWhenTaskNotFound_test() {
        when(taskJpaRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> taskService.findById(100L));
    }

}
