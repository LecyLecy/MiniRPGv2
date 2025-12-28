package miniRPG.skill;

import miniRPG.character.Character;

public class Fireball extends miniRPG.skill.Skill {

    private double damageMultiplier;

    public Fireball() {
        super("Fireball", "Launches a powerful fire attack", 4);
        this.damageMultiplier = 2.5;
    }

    @Override
    protected void applyEffect(Character caster, Character target) {
        int damage = (int) (caster.getAttack() * damageMultiplier);
        target.receiveDamage(damage);
    }

    @Override
    protected void onLevelUp() {
        damageMultiplier += 0.5;
    }
}
