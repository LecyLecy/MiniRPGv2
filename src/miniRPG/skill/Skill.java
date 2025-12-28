package miniRPG.skill;

import miniRPG.character.Character;

public abstract class Skill {

    protected String name;
    protected String description;

    protected int level;
    protected int cooldown;
    protected int currentCooldown;

    public Skill(String name, String description, int cooldown) {
        this.name = name;
        this.description = description;
        this.cooldown = cooldown;
        this.currentCooldown = 0;
        this.level = 1;
    }

    /* =======================
       Cooldown Handling
       ======================= */

    public boolean isAvailable() {
        return currentCooldown == 0;
    }

    public void triggerCooldown() {
        currentCooldown = cooldown;
    }

    public void reduceCooldown() {
        if (currentCooldown > 0) {
            currentCooldown--;
        }
    }

    public void reduceCooldown(int amount) {
        currentCooldown -= amount;
        if (currentCooldown < 0) {
            currentCooldown = 0;
        }
    }

    /* =======================
       Skill Progression
       ======================= */

    public void levelUp() {
        level++;
        onLevelUp();
    }

    protected abstract void onLevelUp();

    /* =======================
       Skill Execution
       ======================= */

    public void use(Character user, Character target) {
        if (!isAvailable()) return;

        applyEffect(user, target);
        triggerCooldown();
    }

    protected abstract void applyEffect(Character user, Character target);

    /* =======================
       Getters
       ======================= */

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getCurrentCooldown() {
        return currentCooldown;
    }
}
