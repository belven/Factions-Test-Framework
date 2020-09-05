package Factions.com;

public class OffensiveTask extends Task {
	private Need need = null;

	public OffensiveTask(Need need) {
		super(TaskType.OFFENSIVE);
		this.setNeed(need);
	}

	public Need getNeed() {
		return need;
	}

	public void setNeed(Need need) {
		this.need = need;
	}

}
