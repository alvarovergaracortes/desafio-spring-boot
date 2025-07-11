package cl.previred.task.application.service;

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
import cl.previred.task.infrastructure.persistence.entity.TaskEntity;
import cl.previred.task.infrastructure.persistence.repository.TaskJpaRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServiceDeleteTest {
	@Mock 
	private TaskJpaRepository taskJpaRepository;

    
    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void shouldDeleteTaskSuccessfully_test() {
        // Given
        Long taskId = 1L;
        TaskEntity entity = new TaskEntity();
        entity.setId(taskId);

        when(taskJpaRepository.findById(taskId)).thenReturn(Optional.of(entity));

        // When
        taskService.deleteTask(taskId);

        // Then
        verify(taskJpaRepository).delete(entity);
    }
    
    @Test
    void shouldThrowWhenDeletingNonExistentTask_test() {
        // Given
        Long taskId = 99L;
        when(taskJpaRepository.findById(taskId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(taskId));
    }
}
