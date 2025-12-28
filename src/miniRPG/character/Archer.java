package miniRPG.character;

import miniRPG.data.PlayerClass;
import miniRPG.skill.SpiritArrow;

public class Archer extends miniRPG.character.Player {

    public Archer(String name) {
        super(name, PlayerClass.ARCHER, 120, 25, 10);

        // Starting skills
        skills.add(new SpiritArrow());
    }
    
    @Override
    protected void onLevelUp() {
        level++;
        maxHP += 15;
        attack += 5;
        defense += 2;
        currentHP = maxHP;
    }
}
