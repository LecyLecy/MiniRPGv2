package miniRPG.character;

import miniRPG.data.StatusEffect;

import java.util.ArrayList;
import java.util.List;

public abstract class Character {

    protected String name;

    protected int maxHP;
    protected int currentHP;

    protected int attack;
    protected int defense;

    protected boolean alive;

    protected List<StatusEffect> statusEffects;

    protected int exp;

    public Character(String name, int maxHP, int attack, int defense) {
        this.name = name;

        this.maxHP = Math.max(1, maxHP);
        this.currentHP = this.maxHP;

        this.attack = Math.max(1, attack);
        this.defense = Math.max(0, defense);

        this.alive = true;
        this.statusEffects = new ArrayList<>();

        this.exp = 0;
    }

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

    public void restoreFull() {
        alive = true;
        currentHP = maxHP;
    }

    public int basicAttack() {
        return attack;
    }

    public abstract void takeTurn(Character target);

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

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = Math.max(0, exp);
    }

    public void gainExp(int amount) {
        if (amount <= 0) return;
        setExp(getExp() + amount);
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

    public void endTurn() {
    }

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
