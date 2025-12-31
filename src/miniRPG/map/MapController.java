package miniRPG.map;

import java.awt.Rectangle;

public class MapController {

    private final int step;
    private final int spriteW;
    private final int spriteH;
    private final int entrW;
    private final int entrH;

    private int x;
    private int y;

    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    private Rectangle upgradeRect = new Rectangle();
    private Rectangle shopRect = new Rectangle();
    private Rectangle dungeonRect = new Rectangle();

    private String pendingEntrance = null;

    private int enterLockTicks = 0;

    public MapController(int step, int spriteW, int spriteH, int entrW, int entrH) {
        this.step = step;
        this.spriteW = spriteW;
        this.spriteH = spriteH;
        this.entrW = entrW;
        this.entrH = entrH;
    }

    public void resetSpawn(int panelW, int panelH) {
        x = 40;
        y = Math.max(0, (panelH - spriteH) / 2);
        updateEntrances(panelW, panelH);
        pendingEntrance = null;
        enterLockTicks = 0;
    }

    public void tick(int panelW, int panelH) {
        updateEntrances(panelW, panelH);

        int dx = 0;
        int dy = 0;

        if (up) dy -= step;
        if (down) dy += step;
        if (left) dx -= step;
        if (right) dx += step;

        x += dx;
        y += dy;

        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > panelW - spriteW) x = Math.max(0, panelW - spriteW);
        if (y > panelH - spriteH) y = Math.max(0, panelH - spriteH);

        if (enterLockTicks > 0) {
            enterLockTicks--;
            return;
        }

        Rectangle playerRect = new Rectangle(x, y, spriteW, spriteH);

        if (playerRect.intersects(upgradeRect)) {
            pendingEntrance = "upgrade";
            enterLockTicks = 30;
            return;
        }

        if (playerRect.intersects(shopRect)) {
            pendingEntrance = "shop";
            enterLockTicks = 30;
            return;
        }

        if (playerRect.intersects(dungeonRect)) {
            pendingEntrance = "dungeon";
            enterLockTicks = 30;
        }
    }

    public String consumeEntranceIfAny() {
        String t = pendingEntrance;
        pendingEntrance = null;
        return t;
    }

    private void updateEntrances(int panelW, int panelH) {
        int upgradeX = (panelW - entrW) / 2;
        int upgradeY = 70;

        int shopX = (panelW - entrW) / 2;
        int shopY = Math.max(0, panelH - entrH - 70);

        int dungeonX = Math.max(0, panelW - entrW - 80);
        int dungeonY = (panelH - entrH) / 2;

        upgradeRect.setBounds(upgradeX, upgradeY, entrW, entrH);
        shopRect.setBounds(shopX, shopY, entrW, entrH);
        dungeonRect.setBounds(dungeonX, dungeonY, entrW, entrH);
    }

    public void resetInput() {
        up = false;
        down = false;
        left = false;
        right = false;
    }

    public void setUp(boolean v) { up = v; }
    public void setDown(boolean v) { down = v; }
    public void setLeft(boolean v) { left = v; }
    public void setRight(boolean v) { right = v; }

    public int getX() { return x; }
    public int getY() { return y; }
}
