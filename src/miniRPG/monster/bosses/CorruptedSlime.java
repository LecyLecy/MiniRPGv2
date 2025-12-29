package miniRPG.monster.bosses;

import miniRPG.character.Character;
import miniRPG.data.MonsterType;
import miniRPG.monster.Monster;

public class CorruptedSlime extends Monster {

    public CorruptedSlime() {
        super(
                "Corrupted Slime",
                hp(), atk(), def(),
                MonsterType.MINI_BOSS,
                expReward(),
                goldReward()
        );
    }

    private static int floor() { return 5; }

    private static int hp()  { return (60 + floor() * 15) * 2; }
    private static int atk() { return (10 + floor() * 3) * 2; }
    private static int def() { return (5  + floor() * 2) * 2; }

    private static int expReward()  { return 120 + (floor() * 10); }
    private static int goldReward() { return 80  + (floor() * 8); }

    @Override
    public void takeTurn(Character target) {
        if (!alive) return;
        attackTarget(target);
    }
}
