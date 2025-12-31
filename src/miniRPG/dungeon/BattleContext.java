package miniRPG.dungeon;

public class BattleContext {

    private boolean concealNextHit = false;
    private boolean spiritArrowNextAttack = false;

    public void resetFloorEffects() {
        concealNextHit = false;
        spiritArrowNextAttack = false;
    }

    public void activateConceal() {
        concealNextHit = true;
    }

    public void activateSpiritArrow() {
        spiritArrowNextAttack = true;
    }

    public int modifyIncomingDamageToPlayer(int damage) {
        int d = Math.max(0, damage);

        if (concealNextHit) {
            concealNextHit = false;
            return 0;
        }

        return d;
    }

    public int modifyMonsterAttackDamage(int damage) {
        int d = Math.max(0, damage);

        if (spiritArrowNextAttack) {
            spiritArrowNextAttack = false;
            return (int) Math.ceil(d * 0.2);
        }

        return d;
    }
}
