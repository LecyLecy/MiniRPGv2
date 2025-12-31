package miniRPG.character;

import miniRPG.data.PlayerClass;
import miniRPG.skill.Conceal;

public class Mage extends Player {

    private static final int BASE_HP = 120;
    private static final int BASE_ATK = 16;
    private static final int BASE_DEF = 6;

    private static final String SPRITE = "/images/mage.png";

    public Mage(String name, int startingExp, int startingGold) {
        super(name, PlayerClass.MAGE, BASE_HP, BASE_ATK, BASE_DEF, startingExp, startingGold, SPRITE);
        skills.add(new Conceal());
    }

    public Mage(String name, int startingExp) {
        this(name, startingExp, 0);
    }

    public Mage(String name) {
        this(name, 0, 0);
    }
}
