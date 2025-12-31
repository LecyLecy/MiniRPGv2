package miniRPG.monster.normalMonster;

import miniRPG.data.MonsterType;
import miniRPG.monster.Monster;

public class Slime extends Monster {

    private static final String SPRITE = "/images/slime.png";

    public Slime(int floor) {
        super(
                "Slime",
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
        return 50 + (f * 12);
    }

    private static int atk(int floor) {
        int f = Math.max(1, floor);
        return 8 + (f * 2);
    }

    private static int def(int floor) {
        int f = Math.max(1, floor);
        return 2 + (f / 3);
    }

    private static int expReward(int floor) {
        int f = Math.max(1, floor);
        return 30 + (f * 6);
    }

    private static int goldReward(int floor) {
        int f = Math.max(1, floor);
        return 8 + (f * 2);
    }
}
