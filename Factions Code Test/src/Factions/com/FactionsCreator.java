package Factions.com;

import java.util.ArrayList;

public class FactionsCreator {
	private ArrayList<Faction> currentFactions = new ArrayList<Faction>();
	private Faction factionGreen = null;
	private Faction factionBlue = null;
	private Faction factionYellow = null;

	public FactionsCreator() {
		setupBasicFactionResources();
	}

	private void setupBasicFactionResources() {
		// Water, Food, Weapons, Scrap, Shelter, Units, Supplies
		factionGreen = new Faction("A", 100, 100, 0, 0, 20, 20, createFactionGreenSupplies());
		factionBlue = new Faction("B", 20, 20, 0, 0, 20, 20, createFactionBlueSupplies());
		factionYellow = new Faction("C", 20, 20, 0, 0, 20, 20, createFactionYellowSupplies());
		
		// In theory, Green and Yellow will head to claim Blues water
		// Blue will try and claim Yellows Food
		
		currentFactions.add(factionGreen);
		currentFactions.add(factionBlue);
		currentFactions.add(factionYellow);
	}

	// Green has food
	private ArrayList<Supply> createFactionGreenSupplies() {
		ArrayList<Supply> supplies = new ArrayList<>();
		supplies.add(new Supply(20, factionGreen, ResourceType.FOOD));
		return supplies;
	}

	// Blue has water
	private ArrayList<Supply> createFactionBlueSupplies() {
		ArrayList<Supply> supplies = new ArrayList<>();
		supplies.add(new Supply(20, factionBlue, ResourceType.WATER));
		return supplies;
	}

	// Yellow has shelter 
	private ArrayList<Supply> createFactionYellowSupplies() {
		ArrayList<Supply> supplies = new ArrayList<>();
		supplies.add(new Supply(20, factionYellow, ResourceType.SHELTER));
		return supplies;
	}

	public Faction getFactionA() {
		return factionGreen;
	}

	public void setFactionA(Faction factionA) {
		this.factionGreen = factionA;
	}

	public Faction getFactionB() {
		return factionBlue;
	}

	public void setFactionB(Faction factionB) {
		this.factionBlue = factionB;
	}

	public Faction getFactionC() {
		return factionYellow;
	}

	public void setFactionC(Faction factionC) {
		this.factionYellow = factionC;
	}

	public ArrayList<Faction> getCurrentFactions() {
		return currentFactions;
	}

	public void setCurrentFactions(ArrayList<Faction> currentFactions) {
		this.currentFactions = currentFactions;
	}
}
