package miniRPG.dungeon;

public class DungeonLevel {

    private final int level;
    private final boolean bossLevel;
    private final boolean finalBoss;

    public DungeonLevel(int level, boolean bossLevel, boolean finalBoss) {
        this.level = level;
        this.bossLevel = bossLevel;
        this.finalBoss = finalBoss;
    }

    public int getLevel() {
        return level;
    }

    public boolean isBossLevel() {
        return bossLevel;
    }

    public boolean isFinalBoss() {
        return finalBoss;
    }
}
