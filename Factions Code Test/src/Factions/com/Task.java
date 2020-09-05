package Factions.com;

public class Task {
	private Need need = null;

	public Task(Need need) {
		super();
		this.setNeed(need);
	}

	public Need getNeed() {
		return need;
	}

	public void setNeed(Need need) {
		this.need = need;
	}

}
