package miniRPG.monster.normalMonster;

import miniRPG.character.Character;
import miniRPG.data.MonsterType;
import miniRPG.monster.Monster;

public class Shadow extends Monster {

    public Shadow(int floor) {
        super(
                "Shadow",
                floor,
                70 + (floor * 18),   // HP: slightly tankier than Slime
                18 + (floor * 5),    // Attack: high burst damage
                8  + (floor * 3),    // Defense: moderate
                MonsterType.NORMAL,
                floor * 40           // base EXP (kept for consistency)
        );

        // Rewards (final authority)
        this.expReward = 35 + (floor * 7);
        this.goldReward = 20 + (floor * 5);
    }

    @Override
    public void takeTurn(Character target) {
        if (!alive) return;

        // Simple aggressive behavior
        attackTarget(target);
    }
}

