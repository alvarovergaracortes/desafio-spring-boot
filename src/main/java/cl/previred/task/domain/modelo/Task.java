package cl.previred.task.domain.modelo;

import java.time.LocalDateTime;

public class Task {
	private Long id;
	private String title;
	private String description;
	private LocalDateTime creationDate;
	private Integer taskStatesId;
	private Long userId;

    public Task(){

    }

	public Task(Long id, String title, String description, LocalDateTime creationDate, Integer taskStatesId,
			Long userId) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.creationDate = creationDate;
		this.taskStatesId = taskStatesId;
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getTaskStatesId() {
		return taskStatesId;
	}

	public void setTaskStatesId(Integer taskStatesId) {
		this.taskStatesId = taskStatesId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", title=" + title + ", description=" + description + ", creationDate=" + creationDate
				+ ", taskStatesId=" + taskStatesId + ", userId=" + userId + "]";
	}
  }
