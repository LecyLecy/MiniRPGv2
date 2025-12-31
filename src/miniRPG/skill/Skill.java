package miniRPG.skill;

import miniRPG.character.Character;
import miniRPG.dungeon.BattleContext;

public abstract class Skill {

    protected final String name;
    protected final String description;

    public Skill(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public final void use(BattleContext ctx, Character user, Character target) {
        if (ctx == null || user == null || target == null) return;
        applyEffect(ctx, user, target);
    }

    protected abstract void applyEffect(BattleContext ctx, Character user, Character target);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
