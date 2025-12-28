package miniRPG.skill;

import miniRPG.character.Character;
import miniRPG.data.StatusEffect;

public class Conceal extends miniRPG.skill.Skill {

    public Conceal() {
        super("Conceal", "Become invisible and avoid attacks", 5);
    }

    @Override
    protected void applyEffect(Character caster, Character target) {
        caster.addStatusEffect(StatusEffect.INVISIBLE);
    }

    @Override
    protected void onLevelUp() {
        // Duration handled later
    }
}
