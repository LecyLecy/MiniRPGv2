package miniRPG.skill;

import miniRPG.character.Character;
import miniRPG.dungeon.BattleContext;

public class Bash extends Skill {

    public Bash() {
        super("Bash", "Deals 50% of enemy current HP as damage");
    }

    @Override
    protected void applyEffect(BattleContext ctx, Character user, Character target) {
        int cur = Math.max(0, target.getCurrentHP());
        int dmg = (int) Math.ceil(cur * 0.5);
        dmg = Math.max(1, dmg);
        target.receiveDamage(dmg);
    }
}
