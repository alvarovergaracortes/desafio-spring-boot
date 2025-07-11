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
public class TaskServiceUpdateTest {
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
	void shouldUpdateTaskSuccessfully_test() {
	    // Given
	    Long taskId = 1L;
	    Task taskToUpdate = new Task(null, "Nuevo Título", "Nueva Desc", null, 2, 2L);

	    TaskEntity existingEntity = new TaskEntity();
	    existingEntity.setId(taskId);
	    existingEntity.setTitle("Antiguo Título");
	    existingEntity.setDescription("Antigua Desc");

	    UserEntity userEntity = new UserEntity();
	    TaskStates taskStates = new TaskStates(2, "EN_PROCESO");
	    TaskStatesEntity taskStatesEntity = new TaskStatesEntity();
	    TaskEntity updatedEntity = new TaskEntity();

	    Task expectedTask = new Task(taskId, "Nuevo Título", "Nueva Desc", null, 2, 2L);

	    when(taskJpaRepository.findById(taskId)).thenReturn(Optional.of(existingEntity));
	    when(userService.findEntityById(2L)).thenReturn(userEntity);
	    when(taskStatesService.findById(2)).thenReturn(taskStates);
	    when(taskStatesMapper.toEntity(taskStates)).thenReturn(taskStatesEntity);
	    when(taskJpaRepository.save(existingEntity)).thenReturn(updatedEntity);
	    when(taskMapper.toDomain(updatedEntity)).thenReturn(expectedTask);

	    // When
	    Task result = taskService.updateTask(taskId, taskToUpdate);

	    // Then
	    assertNotNull(result);
	    assertEquals("Nuevo Título", result.getTitle());
	    verify(taskJpaRepository).save(existingEntity);
	}

	@Test
	void shouldThrowWhenUpdatingNonExistentTask_test() {
	    // Given
	    Long taskId = 99L;
	    Task taskToUpdate = new Task(null, "Irrelevante", "Irrelevante", null, 1, 1L);
	    when(taskJpaRepository.findById(taskId)).thenReturn(Optional.empty());

	    // When / Then
	    assertThrows(ResourceNotFoundException.class, () -> taskService.updateTask(taskId, taskToUpdate));
	}

}
