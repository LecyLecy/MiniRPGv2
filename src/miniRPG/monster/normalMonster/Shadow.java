package miniRPG.monster.normalMonster;

import miniRPG.data.MonsterType;
import miniRPG.monster.Monster;

public class Shadow extends Monster {

    private static final String SPRITE = "/images/shadow.png";

    public Shadow(int floor) {
        super(
                "Shadow",
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
        return 44 + (f * 11);
    }

    private static int atk(int floor) {
        int f = Math.max(1, floor);
        return 10 + (f * 3);
    }

    private static int def(int floor) {
        int f = Math.max(1, floor);
        return 1 + (f / 4);
    }

    private static int expReward(int floor) {
        int f = Math.max(1, floor);
        return 34 + (f * 7);
    }

    private static int goldReward(int floor) {
        int f = Math.max(1, floor);
        return 9 + (f * 2);
    }
}
