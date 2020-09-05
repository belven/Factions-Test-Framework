package Factions.com;

public enum ResourceType {
	WATER("Water"), FOOD("Food"), SHELTER("Shelter"), WEAPONS("Weapons"), SCRAP("Scrap");
	
	private String name;
	
	ResourceType(String newName) {
		name = newName;
	}

	@Override
	public String toString() {
		return name;
	}
	
	
}
