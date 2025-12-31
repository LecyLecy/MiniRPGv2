package miniRPG.monster;

import miniRPG.monster.bosses.CorruptedSlime;
import miniRPG.monster.normalMonster.ArmoredSkeleton;
import miniRPG.monster.normalMonster.Shadow;
import miniRPG.monster.normalMonster.Slime;

import java.util.concurrent.ThreadLocalRandom;

public final class MonsterFactory {

    private MonsterFactory() {}

    public static Monster createMonster(int floor) {
        int f = Math.max(1, floor);

        if (f % 5 == 0) {
            return new CorruptedSlime(f);
        }

        int roll = ThreadLocalRandom.current().nextInt(100);

        if (f <= 3) {
            return (roll < 75) ? new Slime(f) : new Shadow(f);
        }

        if (f <= 8) {
            if (roll < 45) return new Slime(f);
            if (roll < 80) return new Shadow(f);
            return new ArmoredSkeleton(f);
        }

        if (roll < 30) return new Shadow(f);
        if (roll < 70) return new ArmoredSkeleton(f);
        return new Slime(f);
    }
}
