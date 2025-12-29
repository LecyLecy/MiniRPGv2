package miniRPG.character;

import miniRPG.data.PlayerClass;
import miniRPG.skill.SpiritArrow;

public class Archer extends Player {

    // Base stats (balanced for "base * level" scaling)
    private static final int BASE_HP = 150;
    private static final int BASE_ATK = 14;
    private static final int BASE_DEF = 8;

    public Archer(String name, int startingExp, int startingGold) {
        super(name, PlayerClass.ARCHER, BASE_HP, BASE_ATK, BASE_DEF, startingExp, startingGold);

        // Starting skills
        skills.add(new SpiritArrow());
    }

    // Convenience ctor if you don't want to pass gold yet
    public Archer(String name, int startingExp) {
        this(name, startingExp, 0);
    }

    // Convenience ctor if you don't want to pass exp/gold yet
    public Archer(String name) {
        this(name, 0, 0);
    }
}
