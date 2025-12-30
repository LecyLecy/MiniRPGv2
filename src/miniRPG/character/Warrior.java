package miniRPG.character;

import miniRPG.data.PlayerClass;
import miniRPG.skill.WarriorSpirit;

public class Warrior extends Player {

    private static final int BASE_HP = 180;
    private static final int BASE_ATK = 12;
    private static final int BASE_DEF = 10;

    public Warrior(String name, int startingExp, int startingGold) {
        super(name, PlayerClass.WARRIOR, BASE_HP, BASE_ATK, BASE_DEF, startingExp, startingGold);

        skills.add(new WarriorSpirit());
    }

    public Warrior(String name, int startingExp) {
        this(name, startingExp, 0);
    }

    public Warrior(String name) {
        this(name, 0, 0);
    }
}
