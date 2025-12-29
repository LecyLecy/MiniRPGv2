package miniRPG.monster;

import miniRPG.character.Character;
import miniRPG.data.MonsterType;

import java.util.HashMap;
import java.util.Map;

public abstract class Monster extends Character {

    protected MonsterType monsterType;
    protected int expReward;
    protected int goldReward;
    protected Map<String, Integer> skillCooldowns;

    public Monster(String name,
                   int maxHP, int attack, int defense,
                   MonsterType monsterType,
                   int expReward,
                   int goldReward) {

        super(name, maxHP, attack, defense);
        this.monsterType = monsterType;
        this.expReward = expReward;
        this.goldReward = goldReward;
        this.skillCooldowns = new HashMap<>();
    }

    public int getExpReward() {
        return expReward;
    }

    public int getGoldReward() {
        return goldReward;
    }

    public MonsterType getMonsterType() {
        return monsterType;
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
        int damage = Math.max(0, basicAttack() - target.getDefense());
        target.receiveDamage(damage);
    }

    @Override
    public abstract void takeTurn(Character target);
}
