package Factions.com;

public abstract class Task {
	private TaskType type;

	public Task(TaskType inType) {
		type = inType;
	}

	public TaskType getType() {
		return type;
	}

	public void setType(TaskType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type.toString();
	}
}
