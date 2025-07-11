package cl.previred.taskstates.application.service;

import org.springframework.stereotype.Service;

import cl.previred.common.exception.ResourceNotFoundException;
import cl.previred.taskstates.domain.modelo.TaskStates;
import cl.previred.taskstates.infrastructure.persistence.entity.TaskStatesEntity;
import cl.previred.taskstates.infrastructure.persistence.repository.TaskStatesJpaRepository;
import cl.previred.taskstates.infrastructure.web.mapper.TaskStatesMapper;

@Service
public class TaskStateServiceImpl implements TaskStatesService{
	
	private final TaskStatesJpaRepository taskStatesJpaRepository;
	private final TaskStatesMapper taskStatesMapper;


	public TaskStateServiceImpl(TaskStatesJpaRepository taskStatesJpaRepository, TaskStatesMapper taskStatesMapper) {
		super();
		this.taskStatesJpaRepository = taskStatesJpaRepository;
		this.taskStatesMapper = taskStatesMapper;
	}


	@Override
	public TaskStates findById(Integer id) {
		TaskStatesEntity entity = taskStatesJpaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Estado de tarea con ID " + id + " no encontrado"));
		
		return taskStatesMapper.toDomain(entity);
	}

}
