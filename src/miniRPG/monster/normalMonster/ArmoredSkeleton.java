package miniRPG.monster.normalMonster;

import miniRPG.character.Character;
import miniRPG.data.MonsterType;
import miniRPG.monster.Monster;

public class ArmoredSkeleton extends Monster {

    public ArmoredSkeleton(int floor) {
        super(
                "Armored Skeleton",
                hp(floor), atk(floor), def(floor),
                MonsterType.NORMAL,
                expReward(floor),
                goldReward(floor)
        );
    }

    private static int hp(int floor) { return 130 + (floor * 15); }
    private static int atk(int floor) { return 18 + (floor * 3); }
    private static int def(int floor) { return 15 + (floor * 2); }

    private static int expReward(int floor) { return 45 + (floor * 7); }
    private static int goldReward(int floor) { return 25 + (floor * 5); }

    @Override
    public void takeTurn(Character target) {
        if (!alive) return;
        int reducedDamage = Math.max(0, (basicAttack() / 2) - target.getDefense());
        target.receiveDamage(reducedDamage);
    }
}
