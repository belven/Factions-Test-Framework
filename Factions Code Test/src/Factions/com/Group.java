package Factions.com;

import java.util.ArrayList;

public class Group {
	private String name;
	private ArrayList<Unit> units = new ArrayList<>();
	private Task task;
	private TaskType groupType;
	private Faction owner;

	public Group(String name, ArrayList<Unit> units, TaskType newGroupType, Task newTask, Faction newOwner) {
		super();
		this.name = name;
		this.units = units;
		this.setTask(newTask);
		this.setGroupType(newGroupType);
		this.setOwner(newOwner);
	}

	public ArrayList<Unit> getUnits() {
		return units;
	}

	public void setUnits(ArrayList<Unit> units) {
		this.units = units;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String taskText = task != null ? task.toString() : "None";

		sb.append("Group " + name + ", ");
		sb.append("Units " + units.size() + ", ");
		sb.append("Task " + taskText);
		sb.append("\n");
		// sb.append(units.toString());
		// sb.append("\n");

		return sb.toString();
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public TaskType getGroupType() {
		return groupType;
	}

	public void setGroupType(TaskType groupType) {
		this.groupType = groupType;
	}

	public Faction getOwner() {
		return owner;
	}

	public void setOwner(Faction owner) {
		this.owner = owner;
	}
}
