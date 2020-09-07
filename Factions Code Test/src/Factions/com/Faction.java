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

	private ArrayList<Task> tasks = new ArrayList<>();
	private ArrayList<Unit> currentUnits = new ArrayList<>();
	private ArrayList<Group> currentGroups = new ArrayList<>();
	private ArrayList<ResourceGenerator> ownedResourceGenerators = new ArrayList<>();

	private HashMap<ResourceType, Need> currentNeeds = new HashMap<ResourceType, Need>();

	public Faction(String name, int currentWater, int currentFood, int currentWeapons, int currentScrap, int amountOfUnits, ArrayList<ResourceGenerator> supplies) {
		super();
		this.name = name;
		this.currentWater = currentWater;
		this.currentFood = currentFood;
		this.currentWeapons = currentWeapons;
		this.currentScrap = currentScrap;
		this.ownedResourceGenerators = supplies;

		for (int i = 0; i < amountOfUnits; i++) {
			String id = String.valueOf(i + 1);
			currentUnits.add(new Unit(id));
		}

		calculateResourcesAndNeeds();
	}

	private void calculateResourcesAndNeeds() {
		// Gain resources
		calculateTierOneResources();

		// Use resources
		calculateTierTwoResources();

		calculateUnitNeeds();
		calculateNeeds();
	}

	public void nextDay() {
		calculateResourcesAndNeeds();
		calculateTasks();
		assignGroups();
	}

	private void assignGroups() {
		int unitID = 0;
		currentGroups.clear();

		int offensiveActionsAmount = getOffensiveActionsAmount();
		int defensiveActionsAmount = ownedResourceGenerators.size();

		// Max units will be the total amount of units / the sum of both tasks. If we have 5 tasks and 10 units, 2 units should be in each group
		int totalActions = offensiveActionsAmount + defensiveActionsAmount;

		int unitsPerAction = getCurrentUnits() / totalActions;

		// int minUnitsPerGroup = Math.max(1, Math.round(getCurrentUnits() * 0.1f));
		int minUnitsPerGroup = 3;
		int maxUnitsPerGroup = Math.max(minUnitsPerGroup, Math.max(1, unitsPerAction));

		int offensiveUnits = offensiveActionsAmount * maxUnitsPerGroup;

		// If we have 11 units and offensiveUnits is 6, then we want the remainder to be the defensiveUnits
		int defensiveUnits = getCurrentUnits() - offensiveUnits;

		// Split up the defensiveUnits between our resource generators
		// TODO make the AI prioritise locations based on needs and possibility of attack
		// If we have a well but it's at the back of the map, lean towards areas further from the base

		unitID = assignSubGroups(unitID, offensiveUnits, offensiveActionsAmount, TaskType.OFFENSIVE, minUnitsPerGroup, maxUnitsPerGroup);
		unitID = assignSubGroups(unitID, defensiveUnits, defensiveActionsAmount, TaskType.DEFENSIVE, minUnitsPerGroup, maxUnitsPerGroup);

		for (Task task : tasks) {
			Group group = getAvalibleGroupByTaskType(task.getType());

			if (group != null) {
				group.setTask(task);
			}
		}
	}

	private int assignSubGroups(int unitID, int unitsAvalible, int amountOfTasks, TaskType type, int minGroupSize, int maxGroupSize) {
		// Limit the units per group
		int unitsPerGroup = Math.min(maxGroupSize, Math.max(minGroupSize, Math.round(unitsAvalible / amountOfTasks)));

		// Another issue where we may have an odd number, so add the extras to the first group
		// It shouldn't make much difference

		int currentUnitsPerGroup = amountOfTasks * unitsPerGroup;
		int excessUnits = currentUnitsPerGroup != 0 ? unitsAvalible % currentUnitsPerGroup : 0;

		for (int group = 0; group < amountOfTasks; group++) {
			ArrayList<Unit> units = new ArrayList<>();
			int unitsToAdd = unitsPerGroup;

			if (excessUnits > 0) {
				unitsToAdd++;
				excessUnits--;
			}

			// unitID should progress from 0 to units.size(), this avoids adding the same unit to multiple groups
			// units.size() < unitsToAdd should only add units until we've reached unitsToAdd
			// TODO We now have a situation with units not being assigned to a task at all, is this an issue??
			for (; units.size() < unitsToAdd && currentUnits.size() > unitID; unitID++) {
				Unit unit = currentUnits.get(unitID);
				units.add(unit);
			}

			if (units.size() != 0) {
				String groupID = String.valueOf(currentGroups.size() + 1);
				currentGroups.add(new Group(groupID, units, type, null));
			}
		}
		return unitID;
	}

	// Find groups by a task type, either offensive or defensive and one that doesn't have a task already
	Group getAvalibleGroupByTaskType(TaskType type) {
		for (Group group : currentGroups) {
			if (group.getGroupType() == type && group.getTask() == null) {
				return group;
			}
		}
		return null;
	}

	public int getOffensiveActionsAmount() {
		int amount = 0;
		for (Task task : tasks) {
			if (task.getType() == TaskType.OFFENSIVE) {
				amount++;
			}
		}

		return amount;
	}

	private void calculateTasks() {
		tasks.clear();

		// This is where we define our priority order
		if (currentNeeds.get(ResourceType.WATER) != null) {
			tasks.add(new OffensiveTask(currentNeeds.get(ResourceType.WATER)));
		}

		if (currentNeeds.get(ResourceType.FOOD) != null) {
			tasks.add(new OffensiveTask(currentNeeds.get(ResourceType.FOOD)));
		}

		if (currentNeeds.get(ResourceType.SHELTER) != null) {
			tasks.add(new OffensiveTask(currentNeeds.get(ResourceType.SHELTER)));
		}

		if (currentNeeds.get(ResourceType.WEAPONS) != null) {
			tasks.add(new OffensiveTask(currentNeeds.get(ResourceType.WEAPONS)));
		}

		if (currentNeeds.get(ResourceType.SCRAP) != null) {
			tasks.add(new OffensiveTask(currentNeeds.get(ResourceType.SCRAP)));
		}

		for (ResourceGenerator resourceGenerator : ownedResourceGenerators) {
			tasks.add(new DefensiveTask(resourceGenerator));
		}
	}

	private void calculateUnitNeeds() {
		// TODO make this calculate using only units that have lost water
		currentWater -= getCurrentUnits();

		// TODO make this calculate using only units that have lost food
		currentFood -= getCurrentUnits();

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
		if (waterGenerated < getCurrentUnits()) {
			currentNeeds.put(ResourceType.WATER, new Need(ResourceType.WATER, getCurrentUnits() - waterGenerated));
		}

		// Do we have enough food generation?
		if (foodGenerated < getCurrentUnits()) {
			currentNeeds.put(ResourceType.FOOD, new Need(ResourceType.FOOD, getCurrentUnits() - foodGenerated));
		}

		// Do we have enough shelter generation?
		if (shelterGenerated < getCurrentUnits()) {
			currentNeeds.put(ResourceType.SHELTER, new Need(ResourceType.SHELTER, getCurrentUnits() - shelterGenerated));
		}

		// TODO Make weapons a more varied resource, with different types such as
		// offensive and defensive and make it one per unit

		// Are we generating enough weapons? WE want to aim for one weapon per unit
		if (weaponsGenerated < getCurrentUnits()) {
			currentNeeds.put(ResourceType.WEAPONS, new Need(ResourceType.WEAPONS, getCurrentUnits() - weaponsGenerated));
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
		sb.append("Current Supplis: ");
		sb.append("Water: " + currentWater);
		sb.append(", ");
		sb.append("Food: " + currentFood);
		sb.append(", ");
		sb.append("Shelter: " + currentShelter);
		sb.append(", ");
		sb.append("Scrap: " + currentScrap);
		sb.append(", ");
		sb.append("Units: " + getCurrentUnits());
		sb.append(", ");
		sb.append("Weapons: " + currentWeapons);
		sb.append("\n");
		sb.append("Needs: " + getNeedsString());
		sb.append("\n");
		sb.append(getGroupsString());

		return sb.toString();
	}

	private String getGroupsString() {
		StringBuilder sb = new StringBuilder();

		for (Group group : currentGroups) {
			sb.append(group.toString());
		}

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
		if (sb.toString().length() > 0) {
			return sb.toString().substring(0, sb.toString().length() - 2);
		} else {
			return "";
		}
	}

	private void getNeedString(StringBuilder sb, ResourceType type) {
		getNeedString(sb, type, false);
	}

	private void getNeedString(StringBuilder sb, ResourceType type, boolean showZero) {
		if (currentNeeds.get(type) != null) {
			sb.append(currentNeeds.get(type).getAmountNeeded() + " " + type.toString() + ", ");
		} else if (showZero) {
			sb.append(0 + " " + type.toString() + ", ");
		}
	}

	public ArrayList<Group> getCurrentGroups() {
		return currentGroups;
	}

	public void setCurrentGroups(ArrayList<Group> currentGroups) {
		this.currentGroups = currentGroups;
	}

	public int getCurrentUnits() {
		return currentUnits.size();
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
