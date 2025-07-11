package cl.previred.taskstates.infrastructure.web.mapper;

import org.springframework.stereotype.Component;

import cl.previred.taskstates.domain.modelo.TaskStates;
import cl.previred.taskstates.infrastructure.persistence.entity.TaskStatesEntity;

@Component
public class TaskStatesMapper {

	public TaskStates toDomain(TaskStatesEntity entity) {
		TaskStates taskStates = new TaskStates();
        taskStates.setId(entity.getId());
        taskStates.setName(entity.getName());
        
        return taskStates;
    }

	public TaskStatesEntity toEntity(TaskStates taskStates){
		TaskStatesEntity entity = new TaskStatesEntity();
        entity.setId(taskStates.getId());
        entity.setName(taskStates.getName());
        
        return entity;
    }
}
