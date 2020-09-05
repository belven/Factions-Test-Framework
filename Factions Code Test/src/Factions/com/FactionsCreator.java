package Factions.com;

import java.io.IOException;
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
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Name, Water, Food, Weapons, Scrap, Units, Supplies
		int currentWater = 50;
		int currentFood = 50;
		int currentUnits = 10;
		
		factionGreen = new Faction("Green", currentWater, currentFood, 0, 5, currentUnits, createFactionGreenSupplies());
		factionBlue = new Faction("Blue", currentWater, currentFood, 0, 0, currentUnits, createFactionBlueSupplies());
		factionYellow = new Faction("Yellow", currentWater, currentFood, 0, 0, currentUnits, createFactionYellowSupplies());

		// In theory, Green and Yellow will head to claim Blues water
		// Blue will try and claim Yellows Food

		currentFactions.add(factionGreen);
		currentFactions.add(factionBlue);
		currentFactions.add(factionYellow);

		factionOutput();

		int day = currentWater / currentUnits;

		for (int i = 1; i < day + 1; i++) {
			System.out.println("\n");
			System.out.println("------------------- Day " + i +" ------------------");

			factionGreen.nextDay();
			factionBlue.nextDay();
			factionYellow.nextDay();

			factionOutput();
		}
	}

	private void factionOutput() {
		//System.out.println("------------------- Green Start ------------------");
		System.out.println(factionGreen.toString());
		//System.out.println("------------------- Green End ------------------");
		System.out.println("\n");

		//System.out.println("------------------- Blue Start ------------------");
		System.out.println(factionBlue.toString());
		//System.out.println("------------------- Blue End ------------------");
		System.out.println("\n");

		//System.out.println("------------------- Yellow Start ------------------");
		System.out.println(factionYellow.toString());
		//System.out.println("------------------- Yellow End ------------------");
		System.out.println("\n");
	}

	// Green has food
	private ArrayList<ResourceGenerator> createFactionGreenSupplies() {
		ArrayList<ResourceGenerator> supplies = new ArrayList<>();
		supplies.add(new ResourceGenerator(20, factionGreen, ResourceType.FOOD));
		supplies.add(new ResourceGenerator(5, factionGreen, ResourceType.WEAPONS));
		return supplies;
	}

	// Blue has water
	private ArrayList<ResourceGenerator> createFactionBlueSupplies() {
		ArrayList<ResourceGenerator> supplies = new ArrayList<>();
		supplies.add(new ResourceGenerator(20, factionBlue, ResourceType.WATER));
		return supplies;
	}

	// Yellow has shelter
	private ArrayList<ResourceGenerator> createFactionYellowSupplies() {
		ArrayList<ResourceGenerator> supplies = new ArrayList<>();
		supplies.add(new ResourceGenerator(20, factionYellow, ResourceType.SHELTER));
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
