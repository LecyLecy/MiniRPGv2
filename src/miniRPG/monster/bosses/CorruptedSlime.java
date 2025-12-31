package miniRPG.monster.bosses;

import miniRPG.data.MonsterType;
import miniRPG.monster.Monster;

public class CorruptedSlime extends Monster {

    private static final String SPRITE = "/images/corrupted_slime.png";

    public CorruptedSlime(int floor) {
        super(
                "Corrupted Slime",
                maxHp(floor),
                atk(floor),
                def(floor),
                MonsterType.FINAL_BOSS,
                expReward(floor),
                goldReward(floor),
                SPRITE
        );
    }

    private static int maxHp(int floor) {
        int f = Math.max(1, floor);
        return 140 + (f * 20);
    }

    private static int atk(int floor) {
        int f = Math.max(1, floor);
        return 14 + (f * 3);
    }

    private static int def(int floor) {
        int f = Math.max(1, floor);
        return 5 + (f / 2);
    }

    private static int expReward(int floor) {
        int f = Math.max(1, floor);
        return 80 + (f * 12);
    }

    private static int goldReward(int floor) {
        int f = Math.max(1, floor);
        return 20 + (f * 4);
    }
}
