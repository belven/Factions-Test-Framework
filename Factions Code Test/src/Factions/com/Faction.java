package Factions.com;

import java.util.ArrayList;
import java.util.HashMap;

public class Faction {

	private static final String EMPTY = "";
	private String name = EMPTY;
	private int currentWater = 0;
	private int currentFood = 0;
	private int currentWeapons = 0;
	private int currentScrap = 0;
	private int currentShelter = 0;
	private int currentUnits = 0;
	private HashMap<ResourceType, Need> currentNeeds = new HashMap<ResourceType, Need>();
	private ArrayList<Task> tasks = new ArrayList<>();

	public Faction(String name, int currentWater, int currentFood, int currentWeapons, int currentScrap, int currentUnits, ArrayList<ResourceGenerator> supplies) {
		super();
		this.name = name;
		this.currentWater = currentWater;
		this.currentFood = currentFood;
		this.currentWeapons = currentWeapons;
		this.currentScrap = currentScrap;
		this.currentUnits = currentUnits;
		this.ownedResourceGenerators = supplies;
	}

	private ArrayList<ResourceGenerator> ownedResourceGenerators = new ArrayList<>();

	public void nextDay() {
		// Gain resources
		calculateTierOneResources();

		// Use resources
		calculateTierTwoResources();

		calculateUnitNeeds();
		calculateNeeds();
		calculateTasks();
	}

	private void calculateTasks() {
		tasks.clear();

		// This is where we define our priority order
		if (currentNeeds.get(ResourceType.WATER) != null) {
			tasks.add(new Task(currentNeeds.get(ResourceType.WATER)));
		}

		if (currentNeeds.get(ResourceType.FOOD) != null) {
			tasks.add(new Task(currentNeeds.get(ResourceType.FOOD)));
		}

		if (currentNeeds.get(ResourceType.SHELTER) != null) {
			tasks.add(new Task(currentNeeds.get(ResourceType.SHELTER)));
		}

		if (currentNeeds.get(ResourceType.WEAPONS) != null) {
			tasks.add(new Task(currentNeeds.get(ResourceType.WEAPONS)));
		}

		if (currentNeeds.get(ResourceType.SCRAP) != null) {
			tasks.add(new Task(currentNeeds.get(ResourceType.SCRAP)));
		}
	}

	private void calculateUnitNeeds() {
		// TODO Need to change food so they don't need it every day?? evey 3 days?
		currentWater -= currentUnits;
		currentFood -= currentUnits;

		currentFood = Math.max(0, currentFood);
		currentWater = Math.max(0, currentWater);
	}

	private void calculateTierTwoResources() {
		// We need to calculate weapons after getting scrap, as it's dependent on it
		for (ResourceGenerator supply : ownedResourceGenerators) {
			switch (supply.getType()) {
			case WEAPONS: {
				// Only add weapons if we have scrap for it
				int weaponProductionAmount = supply.getResourceAmount();

				// This will work out how many weapons we make
				// If we have 4 scrap but can make 5 weapons, we'll use 4 scrap and make 4
				// weapons.
				// If we have no weapons generated then this will be 0
				int weaponsCreated = Math.min(currentScrap, weaponProductionAmount);

				System.out.println("Weapons Created: " + weaponsCreated);

				currentWeapons += weaponsCreated;
				currentScrap -= weaponsCreated;
			}
				break;
			default:
				break;
			}
		}

		currentScrap = Math.max(0, currentScrap);
	}

	private void calculateTierOneResources() {
		// As Shelter is static we need to reset this value to 0, to work out our total
		currentShelter = 0;

		// Add Supplies
		for (ResourceGenerator supply : ownedResourceGenerators) {
			switch (supply.getType()) {
			case WATER:
				currentWater += supply.getResourceAmount();
				break;
			case FOOD:
				currentFood += supply.getResourceAmount();
				break;
			case SCRAP:
				currentScrap += supply.getResourceAmount();
				break;
			case SHELTER:
				currentShelter += supply.getResourceAmount();
				break;
			default:
				break;
			}
		}
	}

	private void calculateNeeds() {
		currentNeeds = new HashMap<>();

		int waterGenerated = 0;
		int foodGenerated = 0;
		int weaponsGenerated = 0;
		int scrapGenerated = 0;
		int shelterGenerated = 0;

		// Calculate our current resource generation
		for (ResourceGenerator supply : ownedResourceGenerators) {
			switch (supply.getType()) {
			case WATER:
				waterGenerated += supply.getResourceAmount();
				break;
			case FOOD:
				foodGenerated += supply.getResourceAmount();
				break;
			case WEAPONS:
				weaponsGenerated += supply.getResourceAmount();
				break;
			case SCRAP:
				scrapGenerated += supply.getResourceAmount();
				break;
			case SHELTER:
				shelterGenerated += supply.getResourceAmount();
				break;
			default:
				break;
			}
		}

		// Do we have enough water generation?
		if (waterGenerated < currentUnits) {
			currentNeeds.put(ResourceType.WATER, new Need(ResourceType.WATER, currentUnits - waterGenerated));
		}

		// Do we have enough food generation?
		if (foodGenerated < currentUnits) {
			currentNeeds.put(ResourceType.FOOD, new Need(ResourceType.FOOD, currentUnits - foodGenerated));
		}

		// Do we have enough shelter generation?
		if (shelterGenerated < currentUnits) {
			currentNeeds.put(ResourceType.SHELTER, new Need(ResourceType.SHELTER, currentUnits - shelterGenerated));
		}

		// TODO Make weapons a more varied resource, with different types such as
		// offensive and defensive and make it one per unit

		// Are we generating enough weapons?
		if (weaponsGenerated < currentUnits) {
			currentNeeds.put(ResourceType.WEAPONS, new Need(ResourceType.WEAPONS, currentUnits - weaponsGenerated));
		}

		// Do we have enough scrap generation? We only need scrap if we have a use for
		// it, in this case if we have a weapons supplier we need a scrap supplier
		// TODO add in needs for armour
		if (scrapGenerated < weaponsGenerated) {
			currentNeeds.put(ResourceType.SCRAP, new Need(ResourceType.SCRAP, weaponsGenerated));
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append("\n");
		sb.append("Water: " + currentWater);
		sb.append(", ");
		sb.append("Food: " + currentFood);
		sb.append(", ");
		sb.append("Shelter: " + currentShelter);
		sb.append(", ");
		sb.append("Scrap: " + currentScrap);
		sb.append(", ");
		sb.append("Units: " + currentUnits);
		sb.append(", ");
		sb.append("Weapons: " + currentWeapons);
		sb.append("\n");
		sb.append("Needs: " + getNeedsString());

		return sb.toString();
	}

	private String getNeedsString() {
		StringBuilder sb = new StringBuilder();

		// Made a nice clean string generator for each need. This helps align them to
		// clearly compare them in the console and keeps them in the same order
		getNeedString(sb, ResourceType.WATER);
		getNeedString(sb, ResourceType.FOOD);
		getNeedString(sb, ResourceType.SHELTER);
		getNeedString(sb, ResourceType.WEAPONS);
		getNeedString(sb, ResourceType.SCRAP);

		// Trim off the extra ", "
		return sb.toString().substring(0, sb.toString().length() - 2);
	}

	private void getNeedString(StringBuilder sb, ResourceType type) {
		getNeedString(sb, type, true);
	}

	private void getNeedString(StringBuilder sb, ResourceType type, boolean showZero) {
		if (currentNeeds.get(type) != null) {
			sb.append(currentNeeds.get(type).getAmountNeeded() + " " + type.toString() + ", ");
		} else if (showZero) {
			sb.append(0 + " " + type.toString() + ", ");
		}
	}

	public int getWater() {
		return currentWater;
	}

	public void setWater(int water) {
		this.currentWater = water;
	}

	public int getFood() {
		return currentFood;
	}

	public void setFood(int food) {
		this.currentFood = food;
	}

	public int getShelter() {
		return currentShelter;
	}

	public void setShelter(int shelter) {
		this.currentShelter = shelter;
	}

	public int getUnits() {
		return currentUnits;
	}

	public void setUnits(int units) {
		this.currentUnits = units;
	}

	public ArrayList<ResourceGenerator> getResourceGenerators() {
		return ownedResourceGenerators;
	}

	public void setResourceGenerators(ArrayList<ResourceGenerator> resourceGenerators) {
		this.ownedResourceGenerators = resourceGenerators;
	}

	public int getWeapons() {
		return currentWeapons;
	}

	public void setWeapons(int weapons) {
		this.currentWeapons = weapons;
	}

	public int getScrap() {
		return currentScrap;
	}

	public void setScrap(int scrap) {
		this.currentScrap = scrap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
