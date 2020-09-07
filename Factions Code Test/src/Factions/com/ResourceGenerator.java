package Factions.com;

public class ResourceGenerator {
	private int resourceAmount = 5;
	private Faction owner = null;
	private ResourceType type;

	public ResourceGenerator(int newResourceAmount, ResourceType newType) {
		this.resourceAmount = newResourceAmount;
		this.type = newType;
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
		if (this.owner != null) {
			this.owner.getResourceGenerators().remove(this);
		}

		this.owner = owner;

		if (!this.owner.getResourceGenerators().contains(this))
			this.owner.getResourceGenerators().add(this);

	}

	public ResourceType getType() {
		return type;
	}

	public void setType(ResourceType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(type + " " + resourceAmount);
		sb.append("\n");

		return sb.toString();
	}
}
