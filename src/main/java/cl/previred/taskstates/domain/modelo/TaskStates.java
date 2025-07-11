package cl.previred.taskstates.domain.modelo;

public class TaskStates {
	private Integer id;

    private String name;

	public TaskStates(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public TaskStates() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
