package Factions.com;

public class ResourceGenerator {
	private int resourceAmount = 5;
	private Faction owner = null;
	private ResourceType type;
	
	public ResourceGenerator(int resourceAmount, Faction owner, ResourceType type) {
		this.resourceAmount = resourceAmount;
		this.owner = owner;
		this.type = type;
	}

	public int getResourceAmount() {
		return resourceAmount;
	}

	public void setResourceAmount(int resourceAmount) {
		this.resourceAmount = resourceAmount;
	}

	public Faction getOwner() {
		return owner;
	}

	public void setOwner(Faction owner) {
		this.owner = owner;
	}

	public ResourceType getType() {
		return type;
	}

	public void setType(ResourceType type) {
		this.type = type;
	}

}
