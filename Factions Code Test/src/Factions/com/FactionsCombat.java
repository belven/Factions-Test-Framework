package Factions.com;

import java.util.ArrayList;
import java.util.Collections;

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
		System.out.println("\n");
	}

	private void claimResourceGenerator(Group group, ResourceGenerator rg) {
		Faction enemyFaction = rg.getOwner();

		if (enemyFaction != null) {
			fight(group, rg, enemyFaction);
		} else {
			// System.out.println(group.getOwner().getName() + " claimed " + rg.getType());
			rg.setOwner(group.getOwner());
		}
	}

	private void fight(Group ourGroup, ResourceGenerator rg, Faction enemyFaction) {
		Faction ourFaction = ourGroup.getOwner();
		Group enemyGroup = getEnemyGroup(enemyFaction, rg);

		if (enemyGroup != null) {
			// System.out.println(ourFaction.getName() + " is attacking " + enemyFaction.getName());

			for (Unit ourUnit : ourGroup.getUnits()) {
				for (Unit enemyUnit : enemyGroup.getUnits()) {
					if (ourUnit.getHealth() > 0 && enemyUnit.getHealth() > 0) {
						double rand = Math.random() % 100;
						if (rand > .51) {
							enemyUnit.takeDamage(2 + (ourUnit.hasCraftedWeapon() ? 1 : 0));
						} else {
							ourUnit.takeDamage(2 + (enemyUnit.hasCraftedWeapon() ? 1 : 0));
						}
					}
				}
			}

			removeDead(ourGroup);
			removeDead(enemyGroup);

			if (enemyGroup.getUnits().size() > 0 && ourGroup.getUnits().size() > 0) {
				fight(ourGroup, rg, ourFaction);
			} else if (enemyGroup.getUnits().size() <= 0) {
				// System.out.println(enemyFaction.getName() + " lost claim of " + rg.getType() + " to " + ourFaction.getName());
				rg.setOwner(ourGroup.getOwner());
			}

		} else {
			// System.out.println(enemyFaction.getName() + " lost claim of " + rg.getType() + " to " + ourFaction.getName());
			rg.setOwner(ourGroup.getOwner());
		}
	}

	private void removeDead(Group group) {
		ArrayList<Unit> deadUnits = new ArrayList<Unit>();

		for (Unit ourUnit : group.getUnits()) {
			if (ourUnit.getHealth() <= 0) {
				deadUnits.add(ourUnit);
				// System.out.println(ourUnit.getName() + " in faction " + group.getOwner().getName() + " died in combat");
			}
		}

		if (deadUnits.size() > 0) {
			System.out.println("Faction " + group.getOwner().getName() + " lost " + deadUnits.size() + " units in combat");

			group.getUnits().removeAll(deadUnits);
			group.getOwner().getUnits().removeAll(deadUnits);
		}
	}

	private Group getEnemyGroup(Faction enemyFaction, ResourceGenerator rg) {
		for (Group g : enemyFaction.getCurrentGroups()) {
			if (g.getTask() != null && g.getTask().getType().equals(TaskType.DEFENSIVE)) {
				if (((DefensiveTask) g.getTask()).getResourceGenerator() == rg) {
					return g;
				}
			}
		}
		return null;
	}

	private ArrayList<ResourceGenerator> filterGenerators(Faction faction) {
		ArrayList<ResourceGenerator> unownedGenerators = new ArrayList<>();

		for (ResourceGenerator rg : MainApplication.fc.getResourceGenerators()) {
			if (rg.getOwner() != faction)
				unownedGenerators.add(rg);
		}

		Collections.shuffle(unownedGenerators);
		return unownedGenerators;

	}

}
