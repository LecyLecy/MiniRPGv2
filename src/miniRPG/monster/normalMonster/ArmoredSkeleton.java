package miniRPG.monster.normalMonster;

import miniRPG.data.MonsterType;
import miniRPG.monster.Monster;

public class ArmoredSkeleton extends Monster {

    private static final String SPRITE = "/images/armored_skeleton.png";

    public ArmoredSkeleton(int floor) {
        super(
                "Armored Skeleton",
                maxHp(floor),
                atk(floor),
                def(floor),
                MonsterType.NORMAL,
                expReward(floor),
                goldReward(floor),
                SPRITE
        );
    }

    private static int maxHp(int floor) {
        int f = Math.max(1, floor);
        return 70 + (f * 14);
    }

    private static int atk(int floor) {
        int f = Math.max(1, floor);
        return 9 + (f * 2);
    }

    private static int def(int floor) {
        int f = Math.max(1, floor);
        return 4 + (f / 2);
    }

    private static int expReward(int floor) {
        int f = Math.max(1, floor);
        return 38 + (f * 8);
    }

    private static int goldReward(int floor) {
        int f = Math.max(1, floor);
        return 10 + (f * 3);
    }
}
