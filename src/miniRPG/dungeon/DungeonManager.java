package miniRPG.dungeon;

import miniRPG.monster.Monster;
import miniRPG.monster.MonsterFactory;

public class DungeonManager {

    private final miniRPG.dungeon.DungeonProgress progress;

    public DungeonManager() {
        this.progress = new miniRPG.dungeon.DungeonProgress();
    }

    public DungeonProgress getProgress() {
        return progress;
    }

    public DungeonLevel getNextLevel() {
        int level = progress.getNextUnclearedLevel();

        if (level > 30) {
            return null;
        }

        return new DungeonLevel(
                level,
                isBossLevel(level),
                level == 30
        );
    }

    public Monster spawnMonster(DungeonLevel level) {
        return MonsterFactory.createMonster(level.getLevel());
    }

    public void completeLevel(DungeonLevel level) {
        progress.clearLevel(level.getLevel());
    }

    private boolean isBossLevel(int level) {
        return level == 5 || level == 10 || level == 15
            || level == 20 || level == 25 || level == 30;
    }
}
