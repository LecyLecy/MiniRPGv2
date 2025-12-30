package miniRPG.monster;

import miniRPG.monster.bosses.CorruptedSlime;
import miniRPG.monster.normalMonster.ArmoredSkeleton;
import miniRPG.monster.normalMonster.Shadow;
import miniRPG.monster.normalMonster.Slime;

public class MonsterFactory {

    private MonsterFactory() {}

    public static Monster createMonster(int floor) {

        if (floor == 30) {
            return new CorruptedSlime();
        }

        switch (floor) {
            case 5:
            case 10:
            case 15:
            case 20:
            case 25:
                return new CorruptedSlime();
        }

        if (floor >= 1 && floor <= 4) {
            return new Slime(floor);
        }

        if (floor >= 6 && floor <= 9) {
            return new Slime(floor);
        }

        if (floor >= 11 && floor <= 14) {
            return new ArmoredSkeleton(floor);
        }

        if (floor >= 16 && floor <= 19) {
            return new ArmoredSkeleton(floor);
        }

        if (floor >= 21 && floor <= 24) {
            return new Shadow(floor);
        }

        if (floor >= 26 && floor <= 29) {
            return new Shadow(floor);
        }

        throw new IllegalArgumentException("Invalid dungeon floor: " + floor);
    }
}
