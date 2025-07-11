package cl.previred.task.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
public class TaskServiceImpl implements TaskService {

	private final TaskMapper taskMapper;
	private final TaskStatesMapper taskStatesMapper;
	private final TaskJpaRepository taskJpaRepository;
	private final UserService userService;
	private final TaskStatesService taskStatesService;

	
	public TaskServiceImpl(TaskMapper taskMapper, TaskStatesMapper taskStatesMapper,
			TaskJpaRepository taskJpaRepository, UserService userService, TaskStatesService taskStatesService) {
		this.taskMapper = taskMapper;
		this.taskStatesMapper = taskStatesMapper;
		this.taskJpaRepository = taskJpaRepository;
		this.userService = userService;
		this.taskStatesService = taskStatesService;
	}

	@Override
	@Transactional
	public Task create(Task task) {
		
		UserEntity userEntity = userService.findEntityById(task.getUserId());
		
		TaskStates taskStates = taskStatesService.findById(task.getTaskStatesId());
		TaskStatesEntity taskStatesEntity = taskStatesMapper.toEntity(taskStates);
		TaskEntity taskEntity = taskMapper.toEntity(userEntity, taskStatesEntity, task);
		
		TaskEntity saved = taskJpaRepository.save(taskEntity);
		
		Task domain = taskMapper.toDomain(saved);
		
		return domain;
	}

	@Override
	@Transactional
	public Task updateTask(Long id, Task task) {
		TaskEntity existingEntity = taskJpaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tarea con ID " + id + " no encontrada"));
		
		UserEntity userEntity = userService.findEntityById(task.getUserId());
		
		TaskStates taskStates = taskStatesService.findById(task.getTaskStatesId());
		
		// Mapear a entidades
		TaskStatesEntity taskStatesEntity = taskStatesMapper.toEntity(taskStates);
		
		existingEntity.setTitle(task.getTitle());
		existingEntity.setDescription(task.getDescription());
		existingEntity.setCreationDate(task.getCreationDate() != null ? task.getCreationDate() : existingEntity.getCreationDate());
		existingEntity.setUserEntity(userEntity);
		existingEntity.setTaskStates(taskStatesEntity);
		
		TaskEntity updated = taskJpaRepository.save(existingEntity);
		
		return taskMapper.toDomain(updated);
	}
	
	@Override
	@Transactional
	public void deleteTask(Long id) {
		TaskEntity taskEntity = taskJpaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tarea con ID " + id + " no encontrada"));
		
		taskJpaRepository.delete(taskEntity);
	}

	@Override
	public List<Task> findAll() {
		return taskJpaRepository.findAll().stream()
				.map(taskMapper::toDomain).toList();
	}

	
	@Override
	public Task findById(Long id) {
		return taskJpaRepository.findById(id)
				.map(taskMapper::toDomain)
				.orElseThrow(() -> new ResourceNotFoundException("Tarea con ID " + id + " no encontrada"));
	}

	@Override
	public List<Task> findByUserEntityId(Long id) {
		List<TaskEntity> entities = taskJpaRepository.findByUserEntityId(id);
		return entities.stream().map(taskMapper::toDomain).collect(Collectors.toList());
	}

}
