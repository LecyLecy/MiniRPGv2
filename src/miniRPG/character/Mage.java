package miniRPG.character;

import miniRPG.data.PlayerClass;
import miniRPG.skill.Conceal;

public class Mage extends Player {

    // Base stats (balanced for "base * level" scaling)
    private static final int BASE_HP = 120;
    private static final int BASE_ATK = 16;
    private static final int BASE_DEF = 6;

    public Mage(String name, int startingExp, int startingGold) {
        super(name, PlayerClass.MAGE, BASE_HP, BASE_ATK, BASE_DEF, startingExp, startingGold);

        // Starting skills
        skills.add(new Conceal());
    }

    // Convenience ctor if you don't want to pass gold yet
    public Mage(String name, int startingExp) {
        this(name, startingExp, 0);
    }

    // Convenience ctor if you don't want to pass exp/gold yet
    public Mage(String name) {
        this(name, 0, 0);
    }
}
