package Factions.com;

public class Unit {
	private String name;
	private int health = 10;
	private int water = 2;
	private int food = 3;
	private boolean hasCraftedWeapon = false;

	public Unit(String newName) {
		name = newName;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name " + name + ", ");
		sb.append(health + " Health, ");
		sb.append(water + " Water, ");
		sb.append(food + " Food");
		sb.append("\n");

		return sb.toString();
	}

	public void takeDamage(int amount) {
		health -= amount;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getWater() {
		return water;
	}

	public void setWater(int water) {
		this.water = water;
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}

	public boolean isHasCraftedWeapon() {
		return hasCraftedWeapon;
	}

	public void setHasCraftedWeapon(boolean hasCraftedWeapon) {
		this.hasCraftedWeapon = hasCraftedWeapon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
