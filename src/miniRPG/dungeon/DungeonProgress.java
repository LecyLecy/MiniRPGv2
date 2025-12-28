package miniRPG.dungeon;

public class DungeonProgress {

    private int highestClearedLevel;

    public DungeonProgress() {
        this.highestClearedLevel = 0;
    }

    public int getHighestClearedLevel() {
        return highestClearedLevel;
    }

    public void clearLevel(int level) {
        if (level > highestClearedLevel) {
            highestClearedLevel = level;
        }
    }

    public boolean isDungeonCleared() {
        return highestClearedLevel >= 30;
    }

    public int getNextUnclearedLevel() {
        return highestClearedLevel + 1;
    }
}
