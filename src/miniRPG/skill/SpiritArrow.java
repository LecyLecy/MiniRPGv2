package miniRPG.skill;

import miniRPG.character.Character;
import miniRPG.dungeon.BattleContext;

public class SpiritArrow extends Skill {

    public SpiritArrow() {
        super("SpiritArrow", "Reduces enemy next attack damage by 80%");
    }

    @Override
    protected void applyEffect(BattleContext ctx, Character user, Character target) {
        ctx.activateSpiritArrow();
    }
}
