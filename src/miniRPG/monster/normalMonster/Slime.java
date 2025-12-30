package miniRPG.monster.normalMonster;

import miniRPG.character.Character;
import miniRPG.data.MonsterType;
import miniRPG.monster.Monster;

public class Slime extends Monster {

    public Slime(int floor) {
        super(
                "Slime",
                hp(floor), atk(floor), def(floor),
                MonsterType.NORMAL,
                expReward(floor),
                goldReward(floor)
        );
    }

    private static int hp(int floor) { return 20 + ((floor - 1) * 10); }
    private static int atk(int floor) { return 10 + (floor * 3); }
    private static int def(int floor) { return 5 + (floor * 2); }

    private static int expReward(int floor) { return 25 + (floor * 6); }
    private static int goldReward(int floor) { return 10 + (floor * 3); }

    @Override
    public void takeTurn(Character target) {
        if (!alive) return;
        attackTarget(target);
    }
}
