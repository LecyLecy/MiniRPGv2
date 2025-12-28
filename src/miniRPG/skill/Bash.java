package miniRPG.skill;

import miniRPG.character.Character;
import miniRPG.data.StatusEffect;

public class Bash extends miniRPG.skill.Skill {

    private double damageMultiplier;
    private double stunChance;

    public Bash() {
        super("Bash", "Deals heavy damage with a chance to stun", 3);
        this.damageMultiplier = 1.5;
        this.stunChance = 0.2;
    }

    @Override
    protected void applyEffect(Character caster, Character target) {
        int damage = (int) (caster.getAttack() * damageMultiplier);
        target.receiveDamage(damage);

        if (Math.random() < stunChance) {
            target.addStatusEffect(StatusEffect.STUN);
        }
    }

    @Override
    protected void onLevelUp() {
        damageMultiplier += 0.25;
        stunChance += 0.05;
    }
}
