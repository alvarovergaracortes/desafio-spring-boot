package cl.previred.task.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.previred.task.infrastructure.persistence.entity.TaskEntity;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {
	
	List<TaskEntity> findByUserEntityId(Long id);
}
