package miniRPG.ui.screen;

import miniRPG.character.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;

public class MapPanel extends JPanel {

    private static final int STEP = 8;
    private static final int TICK_MS = 16;
    private static final int SPRITE_W = 56;
    private static final int SPRITE_H = 56;
    private static final int ENTR_W = 110;
    private static final int ENTR_H = 70;
    private int x;
    private int y;
    private boolean up, down, left, right;
    private Image sprite;
    private Image mapRaw;
    private Image mapScaled;
    private int mapW = -1, mapH = -1;
    private Rectangle upgradeRect = new Rectangle();
    private Rectangle shopRect = new Rectangle();
    private Rectangle dungeonRect = new Rectangle();
    private boolean transitioning = false;
    private final HudPanel hud;



    public MapPanel(AppFrame frame) {
        setFocusable(true);
        setOpaque(true);

        setLayout(new BorderLayout());
        hud = UiKit.buildHud(frame);
        add(UiKit.topBarLeftHud(hud), BorderLayout.NORTH);

        mapRaw = loadRaw("/images/map.png");

        x = 20;
        y = 200;

        bindKey("pressed W", KeyStroke.getKeyStroke("pressed W"), () -> up = true);
        bindKey("released W", KeyStroke.getKeyStroke("released W"), () -> up = false);

        bindKey("pressed S", KeyStroke.getKeyStroke("pressed S"), () -> down = true);
        bindKey("released S", KeyStroke.getKeyStroke("released S"), () -> down = false);

        bindKey("pressed A", KeyStroke.getKeyStroke("pressed A"), () -> left = true);
        bindKey("released A", KeyStroke.getKeyStroke("released A"), () -> left = false);

        bindKey("pressed D", KeyStroke.getKeyStroke("pressed D"), () -> right = true);
        bindKey("released D", KeyStroke.getKeyStroke("released D"), () -> right = false);

        bindKey("pressed UP", KeyStroke.getKeyStroke("pressed UP"), () -> up = true);
        bindKey("released UP", KeyStroke.getKeyStroke("released UP"), () -> up = false);

        bindKey("pressed DOWN", KeyStroke.getKeyStroke("pressed DOWN"), () -> down = true);
        bindKey("released DOWN", KeyStroke.getKeyStroke("released DOWN"), () -> down = false);

        bindKey("pressed LEFT", KeyStroke.getKeyStroke("pressed LEFT"), () -> left = true);
        bindKey("released LEFT", KeyStroke.getKeyStroke("released LEFT"), () -> left = false);

        bindKey("pressed RIGHT", KeyStroke.getKeyStroke("pressed RIGHT"), () -> right = true);
        bindKey("released RIGHT", KeyStroke.getKeyStroke("released RIGHT"), () -> right = false);

        Timer timer = new Timer(TICK_MS, e -> {
            if (!isShowing()) return;
            updatePositionAndCollisions(frame);
            repaint();
        });
        timer.start();

        addHierarchyListener(e -> {
            if (isShowing()) {
                requestFocusInWindow();
                resetSpawn();
                layoutEntrances();
                clampToBounds();
            } else {
                clearMovementFlags();
            }
        });

        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    public void refreshPlayer(AppFrame frame) {
        clearMovementFlags();
        Player p = frame.getCurrentPlayer();
        if (p != null) {
            sprite = loadSpriteForRole(p.getPlayerClass().name());
        }
        hud.syncFromFrame(frame);
        transitioning = false;
        layoutEntrances();
        repaint();
    }

    public void resetSpawn() {
        clearMovementFlags();
        x = 20;
        y = (getHeight() / 2) - (SPRITE_H / 2);
        if (y < 0) y = 0;
        transitioning = false;
    }

    private void layoutEntrances() {
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) return;

        upgradeRect = new Rectangle(
                (w / 2) - (ENTR_W / 2),
                30,
                ENTR_W, ENTR_H
        );

        shopRect = new Rectangle(
                (w / 2) - (ENTR_W / 2),
                h - ENTR_H - 30,
                ENTR_W, ENTR_H
        );

        dungeonRect = new Rectangle(
                w - ENTR_W - 30,
                (h / 2) - (ENTR_H / 2),
                ENTR_W, ENTR_H
        );
    }

    private void bindKey(String name, KeyStroke key, Runnable r) {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(key, name);
        getActionMap().put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                r.run();
            }
        });
    }

    private Image loadRaw(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) return null;
            return ImageIO.read(is);
        } catch (IOException e) {
            return null;
        }
    }

    private Image loadSpriteForRole(String role) {
        String path;
        switch (role.toUpperCase()) {
            case "WARRIOR":
            case "KNIGHT":
                path = "/images/warrior.png";
                break;
            case "ARCHER":
                path = "/images/archer.png";
                break;
            case "MAGE":
                path = "/images/mage.png";
                break;
            default:
                path = "/images/warrior.png";
        }
        return loadScaled(path, SPRITE_W, SPRITE_H);
    }

    private void updatePositionAndCollisions(AppFrame frame) {
        if (transitioning) return;

        int dx = 0, dy = 0;
        if (up) dy -= STEP;
        if (down) dy += STEP;
        if (left) dx -= STEP;
        if (right) dx += STEP;

        x += dx;
        y += dy;

        clampToBounds();

        Rectangle playerRect = new Rectangle(x, y, SPRITE_W, SPRITE_H);

        if (playerRect.intersects(upgradeRect)) {
            transitioning = true;
            clearMovementFlags();
            frame.openEntrance("upgrade");
        } else if (playerRect.intersects(shopRect)) {
            transitioning = true;
            clearMovementFlags();
            frame.openEntrance("shop");
        } else if (playerRect.intersects(dungeonRect)) {
            transitioning = true;
            clearMovementFlags();
            frame.openEntrance("dungeon");
        }
    }

    private void clampToBounds() {
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) return;

        int maxX = w - SPRITE_W;
        int maxY = h - SPRITE_H;

        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > maxX) x = maxX;
        if (y > maxY) y = maxY;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (mapRaw != null) {
            int w = getWidth();
            int h = getHeight();
            if (mapScaled == null || mapW != w || mapH != h) {
                mapScaled = mapRaw.getScaledInstance(w, h, Image.SCALE_FAST);
                mapW = w;
                mapH = h;
            }
            g.drawImage(mapScaled, 0, 0, null);
        } else {
            g.setColor(new Color(90, 160, 110));
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        Graphics2D g2 = (Graphics2D) g.create();

        if (sprite != null) {
            g2.drawImage(sprite, x, y, null);
        } else {
            g2.setColor(new Color(60, 120, 200));
            g2.fillRect(x, y, SPRITE_W, SPRITE_H);
        }

        g2.dispose();
    }

    private void clearMovementFlags() {
        up = down = left = right = false;
    }

    private Image loadScaled(String path, int w, int h) {
        try {
            java.net.URL url = getClass().getResource(path);
            if (url == null) return null;

            Image img;
            try {
                img = ImageIO.read(url);
            } catch (Exception ex) {
                img = new ImageIcon(url).getImage();
            }

            if (img == null) return null;
            return img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            return null;
        }
    }

}
