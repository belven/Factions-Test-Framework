package Factions.com;

public class Need {

	private ResourceType type;
	private int amountNeeded = 0;	

	public Need(ResourceType type, int amountNeeded) {
		super();
		this.type = type;
		this.setAmountNeeded(amountNeeded);
	}

	public ResourceType getType() {
		return type;
	}

	public void setType(ResourceType type) {
		this.type = type;
	}

	public int getAmountNeeded() {
		return amountNeeded;
	}

	public void setAmountNeeded(int amountNeeded) {
		this.amountNeeded = amountNeeded;
	}

}
