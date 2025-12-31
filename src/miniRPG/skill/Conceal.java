package miniRPG.skill;

import miniRPG.character.Character;
import miniRPG.dungeon.BattleContext;

public class Conceal extends Skill {

    public Conceal() {
        super("Conceal", "Negates the next incoming damage to the player");
    }

    @Override
    protected void applyEffect(BattleContext ctx, Character user, Character target) {
        ctx.activateConceal();
    }
}
