package Factions.com;

import java.util.ArrayList;

public class FactionsCombat {
	static public FactionsCombat INSTANCE = new FactionsCombat();

	public static FactionsCombat getINSTANCE() {
		return INSTANCE;
	}

	public void performOffensiveActions(Faction faction) {
		ArrayList<ResourceGenerator> opposingNeutralGenerators = filterGenerators(faction);

		for (Group group : faction.getCurrentGroups()) {
			if (group.getTask().getType().equals(TaskType.OFFENSIVE)) {
				OffensiveTask ot = (OffensiveTask) group.getTask();

				for (ResourceGenerator rg : opposingNeutralGenerators) {
					if (ot.getNeed().getType().equals(rg.getType())) {
						claimResourceGenerator(group, rg);
					}
				}
			}
		}
		//System.out.println("\n");
	}

	private void claimResourceGenerator(Group group, ResourceGenerator rg) {
		Faction owner = rg.getOwner();

		if (owner != null) {
			double rand = Math.random() % 100;
			String enemyFaction = rg.getOwner().getName();
			String ourFaction = group.getOwner().getName();

			if (rand > .51) {
				//System.out.println(enemyFaction + " lost claim of " + rg.getType() + " to " + ourFaction);
				rg.setOwner(group.getOwner());
			} else {
				//System.out.println(ourFaction + " failed to claim " + rg.getType() + " from " + enemyFaction);
			}
		} else {
			rg.setOwner(group.getOwner());
		}
	}

	private ArrayList<ResourceGenerator> filterGenerators(Faction faction) {
		ArrayList<ResourceGenerator> unownedGenerators = new ArrayList<>();

		for (ResourceGenerator rg : MainApplication.fc.getResourceGenerators()) {
			if (rg.getOwner() != faction)
				unownedGenerators.add(rg);
		}

		return unownedGenerators;

	}

}
