package miniRPG.character;

import miniRPG.data.Currency;
import miniRPG.data.PlayerClass;
import miniRPG.item.Inventory;
import miniRPG.skill.Skill;

import java.util.ArrayList;
import java.util.List;

public class Player extends Character {

    private PlayerClass playerClass;
    private Inventory inventory;
    protected List<Skill> skills;

    public Player(String name, PlayerClass playerClass,
                  int maxHP, int attack, int defense) {

        super(name, 1, maxHP, attack, defense);
        this.playerClass = playerClass;
        this.inventory = new Inventory();
        this.skills = new ArrayList<>();

        this.currency = new Currency(100); // NEW (starting gold)
    }

    /* =======================
       Level-Up Stat Growth
       ======================= */

    @Override
    protected void onLevelUp() {
        switch (playerClass) {
            case WARRIOR:
                increaseMaxHP(30);
                increaseAttack(5);
                increaseDefense(4);
                break;

            case MAGE:
                increaseMaxHP(15);
                increaseAttack(8);
                increaseDefense(2);
                break;

            case ARCHER:
                increaseMaxHP(20);
                increaseAttack(6);
                increaseDefense(3);
                break;
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

    /* =======================
       Stat Modification Helpers
       ======================= */

    public void increaseAttack(int amount) {
        if (amount > 0) attack += amount;
    }

    public void increaseDefense(int amount) {
        if (amount > 0) defense += amount;
    }

    public void increaseMaxHP(int amount) {
        if (amount > 0) {
            maxHP += amount;
            currentHP += amount;
        }
    }
}
