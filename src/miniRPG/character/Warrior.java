package miniRPG.character;

import miniRPG.data.PlayerClass;
import miniRPG.skill.WarriorSpirit;

public class Warrior extends Player {

    // Base stats (balanced for "base * level" scaling)
    private static final int BASE_HP = 180;
    private static final int BASE_ATK = 12;
    private static final int BASE_DEF = 10;

    public Warrior(String name, int startingExp, int startingGold) {
        super(name, PlayerClass.WARRIOR, BASE_HP, BASE_ATK, BASE_DEF, startingExp, startingGold);

        // Starting skills
        skills.add(new WarriorSpirit());
    }

    // Convenience ctor if you don't want to pass gold yet
    public Warrior(String name, int startingExp) {
        this(name, startingExp, 0);
    }

    // Convenience ctor if you don't want to pass exp/gold yet
    public Warrior(String name) {
        this(name, 0, 0);
    }
}
