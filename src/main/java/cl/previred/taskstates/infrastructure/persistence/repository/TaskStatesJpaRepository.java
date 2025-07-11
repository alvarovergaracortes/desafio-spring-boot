package cl.previred.taskstates.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.previred.taskstates.infrastructure.persistence.entity.TaskStatesEntity;

public interface TaskStatesJpaRepository extends JpaRepository<TaskStatesEntity, Integer> {

	Optional<TaskStatesEntity> findById(Integer stateId);
}
