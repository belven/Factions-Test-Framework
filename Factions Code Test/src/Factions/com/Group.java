package Factions.com;

import java.util.ArrayList;

public class Group {
	private String name;
	private ArrayList<Unit> units = new ArrayList<>();

	public Group(String name, ArrayList<Unit> units) {
		super();
		this.name = name;
		this.units = units;
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
}
