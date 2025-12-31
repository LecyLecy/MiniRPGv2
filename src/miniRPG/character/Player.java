package miniRPG.character;

import miniRPG.data.Currency;
import miniRPG.data.PlayerClass;
import miniRPG.dungeon.BattleContext;
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

    protected final List<Skill> skills;

    protected final Currency currency;

    private final String spritePath;

    public Player(String name,
                  PlayerClass playerClass,
                  int baseHP, int baseATK, int baseDEF,
                  int startingExp,
                  int startingGold,
                  String spritePath) {

        super(name, 1, 1, 1);

        this.playerClass = playerClass;

        this.baseHP = Math.max(1, baseHP);
        this.baseATK = Math.max(1, baseATK);
        this.baseDEF = Math.max(0, baseDEF);

        this.bonusHP = 0;
        this.bonusATK = 0;
        this.bonusDEF = 0;

        this.skills = new ArrayList<>();
        this.currency = new Currency(Math.max(0, startingGold));

        this.spritePath = spritePath;

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

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public String getSpritePath() {
        return spritePath;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public Skill getPrimarySkill() {
        if (skills.isEmpty()) return null;
        return skills.get(0);
    }

    public String getPrimarySkillName() {
        Skill s = getPrimarySkill();
        return (s == null) ? "" : s.getName();
    }

    public void usePrimarySkill(BattleContext ctx, Character target) {
        Skill s = getPrimarySkill();
        if (s == null) return;
        s.use(ctx, this, target);
    }

    public void increaseAttack(int delta) {
        if (delta <= 0) return;
        bonusATK += delta;
        recalcStats(false);
    }

    public void increaseDefense(int delta) {
        if (delta <= 0) return;
        bonusDEF += delta;
        recalcStats(false);
    }

    public void increaseMaxHP(int delta) {
        if (delta <= 0) return;
        bonusHP += delta;
        recalcStats(true);
    }

    public Currency getCurrency() {
        return currency;
    }
}
