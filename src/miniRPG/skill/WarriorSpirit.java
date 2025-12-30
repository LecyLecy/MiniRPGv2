package miniRPG.skill;

import miniRPG.character.Character;
import miniRPG.data.StatusEffect;

public class WarriorSpirit extends Skill {

    public WarriorSpirit() {
        super("Warrior Spirit", "Increases attack and defense", 5);
    }

    @Override
    protected void applyEffect(Character caster, Character target) {
        caster.addStatusEffect(StatusEffect.ATTACK_BUFF);
        caster.addStatusEffect(StatusEffect.DEFENSE_BUFF);
    }

    @Override
    protected void onLevelUp() {
        // Buff strength or duration handled nanti aja
    }
}
