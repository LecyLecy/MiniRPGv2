package miniRPG.character;

public abstract class Character {

    protected final String name;

    protected int maxHP;
    protected int currentHP;

    protected int attack;
    protected int defense;

    protected int exp;

    public Character(String name, int maxHP, int attack, int defense) {
        this.name = (name == null) ? "" : name;

        this.maxHP = Math.max(1, maxHP);
        this.currentHP = this.maxHP;

        this.attack = Math.max(1, attack);
        this.defense = Math.max(0, defense);

        this.exp = 0;
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

    public boolean isAlive() {
        return currentHP > 0;
    }

    public void restoreFull() {
        currentHP = maxHP;
    }

    public void receiveDamage(int amount) {
        int dmg = Math.max(0, amount);
        currentHP = Math.max(0, currentHP - dmg);
    }

    public int basicAttack() {
        return attack;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = Math.max(0, exp);
    }

    public int getLevel() {
        return (exp / 1000) + 1;
    }

    public int getExpIntoLevel() {
        return exp % 1000;
    }

    public int getNextLevelExpTotal() {
        return getLevel() * 1000;
    }
}
