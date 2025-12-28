package miniRPG.monster.normalMonster;

import miniRPG.character.Character;
import miniRPG.data.MonsterType;
import miniRPG.monster.Monster;

public class Slime extends Monster {

    public Slime(int floor) {
        super(
            "Slime",
            floor,
            60 + (floor * 15),
            10 + (floor * 3),
            5 + (floor * 2),
            MonsterType.NORMAL,
            floor * 40
        );

        // Rewards
        this.expReward = 20 + (floor * 5);
        this.goldReward = 10 + (floor * 3);
    }

    @Override
    public void takeTurn(Character target) {
        if (!alive) return;
        attackTarget(target);
    }
}
