package cl.previred.task.infrastructure.web.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import cl.previred.task.domain.modelo.Task;
import cl.previred.task.infrastructure.persistence.entity.TaskEntity;
import cl.previred.task.infrastructure.web.dto.TaskRequest;
import cl.previred.task.infrastructure.web.dto.TaskResponse;
import cl.previred.taskstates.infrastructure.persistence.entity.TaskStatesEntity;
import cl.previred.user.infrastructure.persistence.entity.UserEntity;

@Component
public class TaskMapper {
    public Task toDomain(TaskEntity entity) {
    	Task task = new Task();
        task.setId(entity.getId());
        task.setTitle(entity.getTitle());
        task.setDescription(entity.getDescription());
        task.setCreationDate(entity.getCreationDate());

        if (entity.getTaskStates() != null) {
            task.setTaskStatesId(entity.getTaskStates().getId());
        }

        if (entity.getUserEntity() != null) {
            task.setUserId(entity.getUserEntity().getId());
        }

        return task;
    }
    
    public Task toDomain(TaskRequest req) {
    	Task task = new Task();
        task.setId(req.id());
        task.setTitle(req.title());
        task.setDescription(req.description());
        task.setCreationDate(LocalDateTime.now());
        task.setTaskStatesId(req.stateId());
        task.setUserId(req.userId());
        return task;
    }

    public TaskEntity toEntity(Task task){
    	TaskEntity entity = new TaskEntity();
        entity.setId(task.getId());
        entity.setTitle(task.getTitle());
        entity.setDescription(task.getDescription());
        entity.setCreationDate(task.getCreationDate());
        entity.setTaskStates(null);
        entity.setUserEntity(null);
        return entity;
    }
    
    public TaskEntity toEntity(UserEntity userEntity, TaskStatesEntity taskStatesEntity, Task task){
    	TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(task.getTitle());
        taskEntity.setDescription(task.getDescription());
        taskEntity.setCreationDate(LocalDateTime.now());
        
        if (taskStatesEntity != null) {
        	taskEntity.setTaskStates(taskStatesEntity);
        }

        if (userEntity != null) {
        	taskEntity.setUserEntity(userEntity);
        }
        
        return taskEntity;
    }

    
    public TaskResponse toResponse(Task task) {
    	return new TaskResponse(
    	        task.getId(),
    	        task.getTitle(),
    	        task.getDescription(),
    	        task.getCreationDate(),
    	        task.getTaskStatesId(),
    	        task.getUserId()
    	    );
    }
}
