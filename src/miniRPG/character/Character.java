package miniRPG.character;

import miniRPG.data.Currency;
import miniRPG.data.StatusEffect;

import java.util.ArrayList;
import java.util.List;

public abstract class Character {

    protected String name;
    protected int level;

    protected int maxHP;
    protected int currentHP;

    protected int attack;
    protected int defense;

    protected boolean alive;
    protected List<StatusEffect> statusEffects;

    /* =======================
       Progression
       ======================= */

    protected int currentExp;
    protected int maxExp;

    protected Currency currency;

    public Character(String name, int level, int maxHP, int attack, int defense) {
        this.name = name;
        this.level = level;
        this.maxHP = maxHP;
        this.currentHP = maxHP;
        this.attack = attack;
        this.defense = defense;
        this.alive = true;
        this.statusEffects = new ArrayList<>();

        this.currentExp = 0;
        this.maxExp = calculateNextLevelExp();
    }

    /* =======================
       Core Life Cycle
       ======================= */

    public boolean isAlive() {
        return alive;
    }

    public void receiveDamage(int damage) {
        if (damage <= 0) return;

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
       Experience & Leveling
       ======================= */

    public void gainExp(int amount) {
        if (amount <= 0) return;

        currentExp += amount;

        while (currentExp >= maxExp) {
            currentExp -= maxExp;
            levelUp();
        }
    }

    protected void levelUp() {
        level++;
        maxExp = calculateNextLevelExp();
        onLevelUp();
    }

    protected int calculateNextLevelExp() {
        return 100 + (level * 50);
    }

    protected abstract void onLevelUp();

    /* =======================
       Turn Handling
       ======================= */

    public void endTurn() {
        // Default: do nothing
    }

    /* =======================
       Economy
       ======================= */

    public Currency getCurrency() {
        return currency;
    }

    /* =======================
       Getters
       ======================= */

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
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
}
	