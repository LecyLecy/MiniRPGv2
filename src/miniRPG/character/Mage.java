package miniRPG.character;

import miniRPG.data.PlayerClass;
import miniRPG.skill.Conceal;

public class Mage extends miniRPG.character.Player {

    public Mage(String name) {
        super(name, PlayerClass.MAGE, 100, 30, 8);

        // Starting skills
        skills.add(new Conceal());
    }

    
    @Override
    protected void onLevelUp() {
        level++;
        maxHP += 12;
        attack += 6;
        defense += 2;
        currentHP = maxHP;
    }
}
