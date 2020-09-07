package Factions.com;

public class DefensiveTask extends Task {

	private ResourceGenerator resourceGenerator;

	public DefensiveTask(ResourceGenerator newResourceGenerator) {
		super(TaskType.DEFENSIVE);
		setResourceGenerator(newResourceGenerator);
	}

	public ResourceGenerator getResourceGenerator() {
		return resourceGenerator;
	}

	public void setResourceGenerator(ResourceGenerator resourceGenerator) {
		this.resourceGenerator = resourceGenerator;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + " protect " + resourceGenerator.getType() + " resource";
	}

}
