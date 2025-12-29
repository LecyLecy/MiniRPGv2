package miniRPG.monster.normalMonster;

import miniRPG.character.Character;
import miniRPG.data.MonsterType;
import miniRPG.monster.Monster;

public class Shadow extends Monster {

    public Shadow(int floor) {
        super(
                "Shadow",
                hp(floor), atk(floor), def(floor),
                MonsterType.NORMAL,
                expReward(floor),
                goldReward(floor)
        );
    }

    private static int hp(int floor) { return 70 + (floor * 18); }
    private static int atk(int floor) { return 18 + (floor * 5); }
    private static int def(int floor) { return 8 + (floor * 3); }

    private static int expReward(int floor) { return 35 + (floor * 7); }
    private static int goldReward(int floor) { return 20 + (floor * 5); }

    @Override
    public void takeTurn(Character target) {
        if (!alive) return;
        attackTarget(target);
    }
}
