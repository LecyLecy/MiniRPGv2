package miniRPG.map;

import java.awt.*;

public class MapLayout {

    private final int entrW;
    private final int entrH;

    private final int topY;
    private final int rightMargin;
    private final int bottomMargin;

    public MapLayout(int entrW, int entrH) {
        this(entrW, entrH, 80, 70, 70);
    }

    public MapLayout(int entrW, int entrH, int topY, int rightMargin, int bottomMargin) {
        this.entrW = entrW;
        this.entrH = entrH;
        this.topY = topY;
        this.rightMargin = rightMargin;
        this.bottomMargin = bottomMargin;
    }

    public void apply(int panelW, int panelH, Rectangle upgrade, Rectangle shop, Rectangle dungeon) {
        int midX = (panelW - entrW) / 2;

        int upgradeX = midX;
        int upgradeY = topY;

        int shopX = midX;
        int shopY = panelH - entrH - bottomMargin;

        int dungeonX = panelW - entrW - rightMargin;
        int dungeonY = (panelH - entrH) / 2;

        upgrade.setBounds(upgradeX, upgradeY, entrW, entrH);
        shop.setBounds(shopX, shopY, entrW, entrH);
        dungeon.setBounds(dungeonX, dungeonY, entrW, entrH);
    }
}
