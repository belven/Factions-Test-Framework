package Factions.com;

public class Unit {
	private int health = 5;
	private int water = 1;
	private int food = 3;
	private boolean hasCraftedWeapon = false;

	public Unit() {

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
}
