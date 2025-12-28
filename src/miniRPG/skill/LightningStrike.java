package miniRPG.skill;

import miniRPG.character.Character;

public class LightningStrike extends miniRPG.skill.Skill {

    private double damageMultiplier;

    public LightningStrike() {
        super("Lightning Strike", "Calls down a devastating lightning bolt", 6);
        this.damageMultiplier = 3.5;
    }

    @Override
    protected void applyEffect(Character caster, Character target) {
        int damage = (int) (caster.getAttack() * damageMultiplier);
        target.receiveDamage(damage);
    }

    @Override
    protected void onLevelUp() {
        damageMultiplier += 0.75;
    }
}
