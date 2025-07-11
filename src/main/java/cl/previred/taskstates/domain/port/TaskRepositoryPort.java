  package cl.previred.taskstates.domain.port;
  
  import cl.previred.task.domain.modelo.Task;
  import java.util.Optional;
  import java.util.List;

  public interface TaskRepositoryPort {
    Task save(Task task);
    Optional<Task> findById(Long id);
    List<Task> findAll();
    void delete(Long id);
    boolean existsById(Long id);
  }
