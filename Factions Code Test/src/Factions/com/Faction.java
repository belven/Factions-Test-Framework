package Factions.com;

import java.util.ArrayList;
import java.util.HashMap;

public class Faction {

	private String name = "";
	private int currentWater = 0;
	private int currentFood = 0;
	private int currentWeapons = 0;
	private int currentScrap = 0;
	private int currentShelter = 0;
	private int currentUnits = 0;
	private HashMap<ResourceType, Need> currentNeeds = new HashMap<ResourceType, Need>();
	private ArrayList<Task> tasks = new ArrayList<>();

	public Faction(String name, int currentWater, int currentFood, int currentWeapons, int currentScrap,
			int currentShelter, int currentUnits, ArrayList<Supply> supplies) {
		super();
		this.name = name;
		this.currentWater = currentWater;
		this.currentFood = currentFood;
		this.currentWeapons = currentWeapons;
		this.currentScrap = currentScrap;
		this.currentShelter = currentShelter;
		this.currentUnits = currentUnits;
		this.supplies = supplies;
	}

	private ArrayList<Supply> supplies = new ArrayList<>();

	public void nextDay() {
		// Gain resources
		calculateBasicResources();

		// Use resources
		calculateTierTwoResources();

		calculateUnitNeeds();
		calculateNeeds();
		calculateTasks();
	}

	private void calculateTasks() {
		tasks.clear();
		
		//This is where we define our priority order
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
	}

	private void calculateTierTwoResources() {
		// We need to calculate weapons after getting scrap, as it's dependent on it
		for (Supply supply : supplies) {
			switch (supply.getType()) {
			case WEAPONS: {
				// Only add weapons if we have scrap for it
				int weaponProductionAmount = supply.getSupplyAmount();

				// This will work out how many weapons we make
				// If we have 4 scrap but can make 5 weapons, we'll use 4 scrap and make 4
				// weapons.
				// If we have no weapons generated then this will be 0
				int weaponsCreated = Math.min(currentScrap, weaponProductionAmount);

				currentWeapons += weaponsCreated;
				currentScrap -= weaponsCreated;
			}
				break;
			default:
				break;
			}
		}
	}

	private void calculateBasicResources() {
		// Add Supplies
		for (Supply supply : supplies) {
			switch (supply.getType()) {
			case WATER:
				currentWater += supply.getSupplyAmount();
				break;
			case FOOD:
				currentFood += supply.getSupplyAmount();
				break;
			case SCRAP:
				currentScrap += supply.getSupplyAmount();
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

		// Calculate our current resource generation
		for (Supply supply : supplies) {
			switch (supply.getType()) {
			case WATER:
				waterGenerated += supply.getSupplyAmount();
				break;
			case FOOD:
				foodGenerated += supply.getSupplyAmount();
				break;
			case WEAPONS:
				weaponsGenerated += supply.getSupplyAmount();
				break;
			case SCRAP:
				scrapGenerated += supply.getSupplyAmount();
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

		// TODO Make weapons a more varied resource, with different types such as
		// offensive and defensive and make it one per unit

		// Do we have enough weapon generation? Weapons are only needed if we have none
		if (weaponsGenerated == 0) {
			currentNeeds.put(ResourceType.WEAPONS, new Need(ResourceType.WEAPONS, 1));
		}

		// Do we have enough scrap generation? We only need scrap if we have a use for
		// it, in this case if we have a weapons supplier we need a scrap supplier
		// TODO add in needs for armour
		if (weaponsGenerated == 1 && scrapGenerated < currentUnits) {
			currentNeeds.put(ResourceType.SCRAP, new Need(ResourceType.SCRAP, weaponsGenerated));
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append("\n");
		sb.append("Water: " + currentWater);
		sb.append("\n");
		sb.append("Food: " + currentFood);
		sb.append("\n");
		sb.append("Shelter: " + currentShelter);
		sb.append("\n");
		sb.append("Scrap: " + currentScrap);
		sb.append("\n");
		sb.append("Units: " + currentUnits);
		sb.append("\n");
		sb.append("Weapons: " + currentWeapons);
		sb.append("\n");
		sb.append("Needs: " + currentNeeds.toString());
		sb.append("\n");

		return sb.toString();
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

	public ArrayList<Supply> getSupplies() {
		return supplies;
	}

	public void setSupplies(ArrayList<Supply> supplies) {
		this.supplies = supplies;
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
