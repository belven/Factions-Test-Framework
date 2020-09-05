package Factions.com;

public class Supply {
	private int supplyAmount = 5;
	private Faction owner = null;
	private ResourceType type;
	
	public Supply(int supplyAmount, Faction owner, ResourceType type) {
		this.supplyAmount = supplyAmount;
		this.owner = owner;
		this.type = type;
	}

	public int getSupplyAmount() {
		return supplyAmount;
	}

	public void setSupplyAmount(int supplyAmount) {
		this.supplyAmount = supplyAmount;
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
