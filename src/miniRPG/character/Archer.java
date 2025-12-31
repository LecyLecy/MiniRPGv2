package miniRPG.character;

import miniRPG.data.PlayerClass;
import miniRPG.skill.SpiritArrow;

public class Archer extends Player {

    private static final int BASE_HP = 140;
    private static final int BASE_ATK = 14;
    private static final int BASE_DEF = 7;

    private static final String SPRITE = "/images/archer.png";

    public Archer(String name, int startingExp, int startingGold) {
        super(name, PlayerClass.ARCHER, BASE_HP, BASE_ATK, BASE_DEF, startingExp, startingGold, SPRITE);
        skills.add(new SpiritArrow());
    }

    public Archer(String name, int startingExp) {
        this(name, startingExp, 0);
    }

    public Archer(String name) {
        this(name, 0, 0);
    }
}
