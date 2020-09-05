package Factions.com;

public enum TaskType {
	OFFENSIVE("Offensive"), DEFENSIVE("Defensive");

	private String name;

	TaskType(String newName) {
		name = newName;
	}

	@Override
	public String toString() {
		return name;
	}
}
