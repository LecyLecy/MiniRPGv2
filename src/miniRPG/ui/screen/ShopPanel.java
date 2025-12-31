package miniRPG.ui.screen;

import miniRPG.ui.asset.AssetLoader;

import javax.swing.*;
import java.awt.*;

public class ShopPanel extends JPanel {

    private final AppFrame frame;

    private final AssetLoader assets = new AssetLoader(ShopPanel.class);

    private Image bgRaw;
    private Image bgScaled;
    private int bgW = -1, bgH = -1;

    private final HudPanel hud = new HudPanel();
    private final JButton backBtn;

    public ShopPanel(AppFrame frame) {
        this.frame = frame;

        setLayout(new BorderLayout());
        setOpaque(true);

        bgRaw = assets.loadRaw("/images/shop.png");

        hud.bind(frame);
        UiKit.setFixedSize(hud, UiKit.HUD_SIZE);
        add(UiKit.topBarLeftHud(hud), BorderLayout.NORTH);

        backBtn = UiKit.createGoBackButton(frame::openMap);

        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        south.setBorder(BorderFactory.createEmptyBorder(0, 12, 16, 12));
        south.add(UiKit.bottomRightWrap(backBtn), BorderLayout.EAST);
        add(south, BorderLayout.SOUTH);

        addHierarchyListener(e -> {
            if (isShowing()) hud.syncFromFrame(frame);
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (bgRaw != null) {
            int w = getWidth();
            int h = getHeight();
            if (bgScaled == null || bgW != w || bgH != h) {
                bgScaled = bgRaw.getScaledInstance(w, h, Image.SCALE_FAST);
                bgW = w;
                bgH = h;
            }
            g.drawImage(bgScaled, 0, 0, null);
            return;
        }

        g.setColor(new Color(25, 25, 25));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
