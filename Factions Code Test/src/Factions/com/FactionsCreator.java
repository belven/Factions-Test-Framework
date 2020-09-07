package Factions.com;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class FactionsCreator {
	private Faction factionGreen = null;
	private Faction factionBlue = null;
	private Faction factionYellow = null;

	private int unitsPerFaction = 15;

	private ArrayList<Faction> currentFactions = new ArrayList<Faction>();
	private ArrayList<ResourceGenerator> resourceGenerators = new ArrayList<>();

	public FactionsCreator() {
		setupBasicFactionResources();
	}

	private void setupBasicFactionResources() {
		// Clear the console
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Give them X days worth of supplies
		int daysOfSupplies = 4;
		int currentWater = unitsPerFaction * daysOfSupplies;
		int currentFood = unitsPerFaction * daysOfSupplies;

		// Name, Water, Food, Weapons, Scrap, Units, Supplies
		factionGreen = new Faction("Green", currentWater, currentFood, 0, 0, unitsPerFaction, createFactionGreenSupplies());
		factionBlue = new Faction("Blue", currentWater, currentFood, 0, 0, unitsPerFaction, createFactionBlueSupplies());
		factionYellow = new Faction("Yellow", currentWater, currentFood, 0, 0, unitsPerFaction, createFactionYellowSupplies());

		// Get a master list of all resources, this will be used for look-ups later when I implement attacking
		resourceGenerators.addAll(factionGreen.getResourceGenerators());
		resourceGenerators.addAll(factionBlue.getResourceGenerators());
		resourceGenerators.addAll(factionYellow.getResourceGenerators());

		currentFactions.add(factionGreen);
		currentFactions.add(factionBlue);
		currentFactions.add(factionYellow);
	}

	public void startCycles() {
		factionOutput();

		int days = 500;

		long begin = System.currentTimeMillis();

		// TODO I need to make nextDay be closer to every 15 seconds, so the decisions
		// occur faster and more in real time. This could allow for other stats for
		// factions, like a politics stat that changes how quickly decisions are made
		for (int i = 1; i < days + 1; i++) {
			System.out.println("------------------- Day " + i + " ------------------");

			factionGreen.nextDay();
			factionBlue.nextDay();
			factionYellow.nextDay();

			FactionsCombat.getINSTANCE().performOffensiveActions(factionGreen);
			FactionsCombat.getINSTANCE().performOffensiveActions(factionBlue);
			FactionsCombat.getINSTANCE().performOffensiveActions(factionYellow);

			factionOutput();
		}

		System.out.println(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - begin));
	}

	private void factionOutput() {
		System.out.println(factionGreen.toString());
		System.out.println(factionBlue.toString());
		System.out.println(factionYellow.toString());
	}

	// Green
	private ArrayList<ResourceGenerator> createFactionGreenSupplies() {
		ArrayList<ResourceGenerator> supplies = new ArrayList<>();
		supplies.add(new ResourceGenerator(unitsPerFaction, ResourceType.FOOD));
		supplies.add(new ResourceGenerator(unitsPerFaction, ResourceType.SCRAP));
		return supplies;
	}

	// Blue
	private ArrayList<ResourceGenerator> createFactionBlueSupplies() {
		ArrayList<ResourceGenerator> supplies = new ArrayList<>();
		supplies.add(new ResourceGenerator(unitsPerFaction, ResourceType.WATER));
		supplies.add(new ResourceGenerator(unitsPerFaction, ResourceType.WEAPONS));
		return supplies;
	}

	// Yellow
	private ArrayList<ResourceGenerator> createFactionYellowSupplies() {
		ArrayList<ResourceGenerator> supplies = new ArrayList<>();
		supplies.add(new ResourceGenerator(unitsPerFaction, ResourceType.SHELTER));
		return supplies;
	}

	public ArrayList<ResourceGenerator> getResourceGenerators() {
		return resourceGenerators;
	}

	public void setResourceGenerators(ArrayList<ResourceGenerator> resourceGenerators) {
		this.resourceGenerators = resourceGenerators;
	}
}
