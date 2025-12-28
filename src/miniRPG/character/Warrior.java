package miniRPG.character;

import miniRPG.data.PlayerClass;
import miniRPG.skill.WarriorSpirit;

public class Warrior extends Player {

    public Warrior(String name) {
        super(name, PlayerClass.WARRIOR, 150, 20, 15);

        // Starting skills
        skills.add(new WarriorSpirit());
    }
    
    @Override
    protected void onLevelUp() {
        maxHP += 20;
        attack += 4;
        defense += 4;
        currentHP = maxHP;
    }

}
