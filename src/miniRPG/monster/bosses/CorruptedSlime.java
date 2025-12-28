package miniRPG.monster.bosses;

import miniRPG.monster.Monster;
import miniRPG.character.Character;
import miniRPG.data.MonsterType;

public class CorruptedSlime extends Monster {

    public CorruptedSlime() {
        super(
            "Corrupted Slime",
            5,
            (60 + 5 * 15) * 2,
            (10 + 5 * 3) * 2,
            (5 + 5 * 2) * 2,
            MonsterType.MINI_BOSS,
            5 * 80
        );
        int floor = 5;
        this.expReward = 120 + (floor * 10);
        this.goldReward = 80 + (floor * 8);
    }

    @Override
    public void takeTurn(Character target) {
        if (!alive) return;
        attackTarget(target);
    }
}
