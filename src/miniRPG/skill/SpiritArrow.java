package miniRPG.skill;

import miniRPG.character.Character;

public class SpiritArrow extends Skill {

    private double damageMultiplier;

    public SpiritArrow() {
        super("Spirit Arrow", "Shoots a precise energy arrow", 3);
        this.damageMultiplier = 1.8;
    }

    @Override
    protected void applyEffect(Character caster, Character target) {
        int damage = (int) (caster.getAttack() * damageMultiplier);
        target.receiveDamage(damage);
    }

    @Override
    protected void onLevelUp() {
        damageMultiplier += 0.3;
    }
}
