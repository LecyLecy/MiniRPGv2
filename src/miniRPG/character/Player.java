package miniRPG.character;

import miniRPG.data.Currency;
import miniRPG.data.PlayerClass;
import miniRPG.item.Inventory;
import miniRPG.skill.Skill;

import java.util.ArrayList;
import java.util.List;

public class Player extends Character {

    private final PlayerClass playerClass;

    // Base stats (role defaults). Final stats = base * level
    private final int baseHP;
    private final int baseATK;
    private final int baseDEF;

    private final Inventory inventory;
    protected final List<Skill> skills;

    // If you're using this later, keep it; otherwise you can remove.
    protected Currency currency;

    public Player(String name,
                  PlayerClass playerClass,
                  int baseHP, int baseATK, int baseDEF,
                  int startingExp,
                  int startingGold) {

        // temporary values; recalcStats() will set correct ones based on EXP-derived level
        super(name, 1, 1, 1);

        this.playerClass = playerClass;

        this.baseHP = baseHP;
        this.baseATK = baseATK;
        this.baseDEF = baseDEF;

        this.inventory = new Inventory();
        this.skills = new ArrayList<>();

        this.currency = new Currency(Math.max(0, startingGold));

        // total exp persisted in CSV; level is derived
        this.exp = Math.max(0, startingExp);

        recalcStats(true); // full heal at spawn
    }

    /* =======================
       Progression
       ======================= */

    // Adds EXP and recalculates derived stats.
    // Full-heal only if level increased.
    public void gainExp(int amount) {
        if (amount <= 0) return;

        int oldLevel = getLevel();
        exp += amount;

        int newLevel = getLevel();
        boolean leveledUp = newLevel > oldLevel;

        recalcStats(leveledUp);
    }

    // Derived stats = base * level
    private void recalcStats(boolean fullHeal) {
        int lvl = getLevel();

        this.maxHP = baseHP * lvl;
        this.attack = baseATK * lvl;
        this.defense = baseDEF * lvl;

        if (fullHeal) {
            this.currentHP = this.maxHP;
        } else {
            this.currentHP = Math.min(this.currentHP, this.maxHP);
        }
    }

    /* =======================
       Skills
       ======================= */

    public void useSkill(int index, Character target) {
        if (index < 0 || index >= skills.size()) return;
        skills.get(index).use(this, target);
    }

    public List<Skill> getSkills() {
        return skills;
    }

    /* =======================
       Inventory
       ======================= */

    public Inventory getInventory() {
        return inventory;
    }

    /* =======================
       Turn Control
       ======================= */

    @Override
    public void takeTurn(Character target) {
        // Controlled by UI
    }

    @Override
    public void endTurn() {
        for (Skill skill : skills) {
            skill.reduceCooldown();
        }
    }

    /* =======================
       Getters
       ======================= */

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
