package miniRPG.monster.normalMonster;

import miniRPG.character.Character;
import miniRPG.data.MonsterType;
import miniRPG.monster.Monster;

public class ArmoredSkeleton extends Monster {

    public ArmoredSkeleton(int floor) {
        super(
            "Armored Skeleton",
            floor,
            130 + (floor * 15),
            18 + (floor * 3),
            15 + (floor * 2),
            MonsterType.NORMAL,
            floor * 40
        );
        this.expReward = 45 + (floor * 7);
        this.goldReward = 25 + (floor * 5);
    }

    @Override
    public void takeTurn(Character target) {
        if (!alive) return;

        int reducedDamage =
                Math.max(0, (basicAttack() / 2) - target.getDefense());
        target.receiveDamage(reducedDamage);
    }
}
