package cl.previred.task.application.service;

import java.util.List;

import cl.previred.task.domain.modelo.Task;

public interface TaskService {

	
	Task create(Task task);

	Task updateTask(Long id, Task task);
	
	void deleteTask(Long id);

	List<Task> findAll();

	Task findById(Long id);

	List<Task> findByUserEntityId(Long id);

}
