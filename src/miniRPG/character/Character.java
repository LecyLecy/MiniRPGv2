package miniRPG.character;

import miniRPG.data.StatusEffect;

import java.util.ArrayList;
import java.util.List;

public abstract class Character {

    /* =======================
       Identity
       ======================= */

    protected String name;

    /* =======================
       Core Stats (derived for Player, fixed for Monster, etc.)
       ======================= */

    protected int maxHP;
    protected int currentHP;

    protected int attack;
    protected int defense;

    protected boolean alive;

    /* =======================
       Status Effects
       ======================= */

    protected List<StatusEffect> statusEffects;

    /* =======================
       Progression (TOTAL EXP ONLY)
       - Level is derived from exp
       - Next level threshold is derived from level
       ======================= */

    protected int exp; // total exp (persisted in CSV for Player)

    public Character(String name, int maxHP, int attack, int defense) {
        this.name = name;

        this.maxHP = maxHP;
        this.currentHP = maxHP;

        this.attack = attack;
        this.defense = defense;

        this.alive = true;
        this.statusEffects = new ArrayList<>();

        this.exp = 0;
    }

    /* =======================
       Core Life Cycle
       ======================= */

    public boolean isAlive() {
        return alive;
    }

    public void receiveDamage(int damage) {
        if (damage <= 0 || !alive) return;

        currentHP -= damage;
        if (currentHP <= 0) {
            currentHP = 0;
            alive = false;
        }
    }

    public void heal(int amount) {
        if (amount <= 0 || !alive) return;

        currentHP = Math.min(currentHP + amount, maxHP);
    }

    /* =======================
       Combat
       ======================= */

    public int basicAttack() {
        return attack;
    }

    public abstract void takeTurn(Character target);

    /* =======================
       Status Effects
       ======================= */

    public void addStatusEffect(StatusEffect effect) {
        if (effect == null) return;
        if (!statusEffects.contains(effect)) {
            statusEffects.add(effect);
        }
    }

    public void removeStatusEffect(StatusEffect effect) {
        statusEffects.remove(effect);
    }

    public boolean hasStatusEffect(StatusEffect effect) {
        return statusEffects.contains(effect);
    }

    /* =======================
       Experience & Level (derived)
       Rules:
       - level = (exp / 1000) + 1
       - next level total exp = level * 1000
       Display example:
       - Level 3
       - EXP 2345/3000
       ======================= */

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = Math.max(0, exp);
    }

    public int getLevel() {
        return (exp / 1000) + 1;
    }

    public int getNextLevelExpTotal() {
        return getLevel() * 1000;
    }

    public String getExpDisplay() {
        return exp + "/" + getNextLevelExpTotal();
    }

    /* =======================
       Turn Handling
       ======================= */

    public void endTurn() {
        // Default: do nothing
    }

    /* =======================
       Getters
       ======================= */

    public String getName() {
        return name;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public List<StatusEffect> getStatusEffects() {
        return statusEffects;
    }
}
