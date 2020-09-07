package Factions.com;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class FactionsCreator {
	private Faction factionGreen = null;
	private Faction factionBlue = null;
	private Faction factionYellow = null;

	private int unitsPerFaction = 50;

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

		resourceGenerators.addAll(factionGreen.getResourceGenerators());
		resourceGenerators.addAll(factionBlue.getResourceGenerators());
		resourceGenerators.addAll(factionYellow.getResourceGenerators());

		// In theory, Green and Yellow will head to claim Blues water
		// Blue will try and claim Yellows Food

		currentFactions.add(factionGreen);
		currentFactions.add(factionBlue);
		currentFactions.add(factionYellow);
	}

	public void startCycles() {
		factionOutput();

		int days = 1;

		long begin = System.currentTimeMillis();

		// TODO I need to make nextDay be closer to every 15 seconds, so the decisions
		// occur faster and more in real time. This could allow for other stats for
		// factions, like a politics stat that changes how quickly decisions are made
		for (int i = 1; i < days + 1; i++) {
			System.out.println("\n");
			System.out.println("------------------- Day " + i + " ------------------");

			factionGreen.nextDay();
			factionBlue.nextDay();
			factionYellow.nextDay();

			factionOutput();
		}

		System.out.println(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - begin));
	}

	private void factionOutput() {
		// System.out.println("------------------- Green Start ------------------");
		System.out.println(factionGreen.toString());
		// System.out.println("------------------- Green End ------------------");
		System.out.println("\n");

		// System.out.println("------------------- Blue Start ------------------");
		System.out.println(factionBlue.toString());
		// System.out.println("------------------- Blue End ------------------");
		System.out.println("\n");

		// System.out.println("------------------- Yellow Start ------------------");
		System.out.println(factionYellow.toString());
		// System.out.println("------------------- Yellow End ------------------");
		System.out.println("\n");
	}

	// Green has food
	private ArrayList<ResourceGenerator> createFactionGreenSupplies() {
		ArrayList<ResourceGenerator> supplies = new ArrayList<>();
		supplies.add(new ResourceGenerator(unitsPerFaction, factionGreen, ResourceType.FOOD));
		supplies.add(new ResourceGenerator(unitsPerFaction, factionGreen, ResourceType.SCRAP));
		return supplies;
	}

	// Blue has water
	private ArrayList<ResourceGenerator> createFactionBlueSupplies() {
		ArrayList<ResourceGenerator> supplies = new ArrayList<>();
		supplies.add(new ResourceGenerator(unitsPerFaction, factionBlue, ResourceType.WATER));
		supplies.add(new ResourceGenerator(unitsPerFaction, factionGreen, ResourceType.WEAPONS));
		supplies.add(new ResourceGenerator(unitsPerFaction, factionGreen, ResourceType.SCRAP));
		return supplies;
	}

	// Yellow has shelter
	private ArrayList<ResourceGenerator> createFactionYellowSupplies() {
		ArrayList<ResourceGenerator> supplies = new ArrayList<>();
		supplies.add(new ResourceGenerator(unitsPerFaction, factionGreen, ResourceType.FOOD));
		return supplies;
	}
}
