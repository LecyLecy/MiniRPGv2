package miniRPG.character;

import miniRPG.data.Currency;
import miniRPG.data.PlayerClass;
import miniRPG.item.Inventory;
import miniRPG.skill.Skill;

import java.util.ArrayList;
import java.util.List;

public class Player extends Character {

    private final PlayerClass playerClass;

    private final int baseHP;
    private final int baseATK;
    private final int baseDEF;

    private int bonusHP;
    private int bonusATK;
    private int bonusDEF;

    private final Inventory inventory;
    protected final List<Skill> skills;

    protected final Currency currency;

    public Player(String name,
                  PlayerClass playerClass,
                  int baseHP, int baseATK, int baseDEF,
                  int startingExp,
                  int startingGold) {

        super(name, 1, 1, 1);

        this.playerClass = playerClass;

        this.baseHP = Math.max(1, baseHP);
        this.baseATK = Math.max(1, baseATK);
        this.baseDEF = Math.max(0, baseDEF);

        this.bonusHP = 0;
        this.bonusATK = 0;
        this.bonusDEF = 0;

        this.inventory = new Inventory();
        this.skills = new ArrayList<>();

        this.currency = new Currency(Math.max(0, startingGold));

        setExp(Math.max(0, startingExp));
        recalcStats(true);
    }

    @Override
    public void setExp(int exp) {
        int oldLevel = getLevel();
        super.setExp(exp);
        boolean leveledUp = getLevel() > oldLevel;
        recalcStats(leveledUp);
    }

    private void recalcStats(boolean fullHeal) {
        int lvl = getLevel();

        int newMaxHP = (baseHP * lvl) + bonusHP;
        int newATK = (baseATK * lvl) + bonusATK;
        int newDEF = (baseDEF * lvl) + bonusDEF;

        this.maxHP = Math.max(1, newMaxHP);
        this.attack = Math.max(1, newATK);
        this.defense = Math.max(0, newDEF);

        if (fullHeal) {
            restoreFull();
        } else {
            this.currentHP = Math.min(this.currentHP, this.maxHP);
        }
    }

    public void increaseAttack(int amount) {
        if (amount <= 0) return;
        bonusATK += amount;
        recalcStats(false);
    }

    public void increaseDefense(int amount) {
        if (amount <= 0) return;
        bonusDEF += amount;
        recalcStats(false);
    }

    public void increaseMaxHP(int amount) {
        if (amount <= 0) return;
        bonusHP += amount;
        int prevMax = maxHP;
        recalcStats(false);
        int diff = maxHP - prevMax;
        if (diff > 0) currentHP += diff;
        currentHP = Math.min(currentHP, maxHP);
    }

    public void useSkill(int index, Character target) {
        if (index < 0 || index >= skills.size()) return;
        skills.get(index).use(this, target);
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void takeTurn(Character target) {
    }

    @Override
    public void endTurn() {
        for (Skill skill : skills) {
            skill.reduceCooldown();
        }
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public int getBaseHP() {
        return baseHP;
    }

    public int getBaseATK() {
        return baseATK;
    }

    public int getBaseDEF() {
        return baseDEF;
    }

    public Currency getCurrency() {
        return currency;
    }
}
