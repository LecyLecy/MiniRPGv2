package miniRPG.monster;

import miniRPG.character.Character;
import miniRPG.data.MonsterType;

import java.util.HashMap;
import java.util.Map;

public abstract class Monster extends Character {

    protected final MonsterType monsterType;
    protected final int expReward;
    protected final int goldReward;

    private final String spritePath;

    protected final Map<String, Integer> skillCooldowns;

    public Monster(String name,
                   int maxHP, int attack, int defense,
                   MonsterType monsterType,
                   int expReward,
                   int goldReward,
                   String spritePath) {

        super(name, maxHP, attack, defense);
        this.monsterType = monsterType;
        this.expReward = expReward;
        this.goldReward = goldReward;
        this.spritePath = spritePath;
        this.skillCooldowns = new HashMap<>();
    }

    public MonsterType getMonsterType() {
        return monsterType;
    }

    public int getExpReward() {
        return expReward;
    }

    public int getGoldReward() {
        return goldReward;
    }

    public String getSpritePath() {
        return spritePath;
    }

    protected void reduceCooldowns() {
        for (String key : skillCooldowns.keySet()) {
            int cd = skillCooldowns.get(key);
            if (cd > 0) {
                skillCooldowns.put(key, cd - 1);
            }
        }
    }

    protected void attackTarget(Character target) {
        int damage = Math.max(1, basicAttack() - target.getDefense());
        target.receiveDamage(damage);
    }
}
