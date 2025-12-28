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

    // entrances
    private static final int ENTR_W = 110;
    private static final int ENTR_H = 70;

    private int x;
    private int y;

    private boolean up, down, left, right;

    private Image sprite;
    private Image upgradeImg;
    private Image shopImg;
    private Image dungeonImg;
    private Image mapBg;
    private Image mapBgScaled;
    private Image coinImg;
    private final AppFrame frame;


    private int mapBgScaledW = -1;
    private int mapBgScaledH = -1;

    private String roleText = "";

    // entrance hitboxes
    private Rectangle upgradeRect = new Rectangle();
    private Rectangle shopRect = new Rectangle();
    private Rectangle dungeonRect = new Rectangle();

    private boolean transitioning = false; // prevent multiple triggers

    public MapPanel(AppFrame frame) {
        this.frame = frame;
        setFocusable(true);
        setBackground(new Color(245, 245, 245));
        mapBg = loadImageRaw("/images/map.png");

        upgradeImg = loadEntranceImage("/images/upgrade.png", ENTR_W - 16, ENTR_H - 28);
        shopImg    = loadEntranceImage("/images/shop.png",    ENTR_W - 16, ENTR_H - 28);
        dungeonImg = loadEntranceImage("/images/dungeon.png", ENTR_W - 16, ENTR_H - 28);
        coinImg = loadEntranceImage("/images/coin.png", 20, 20); // returns null if missing

        // initial spawn (left-middle will be corrected when shown)
        x = 20;
        y = 200;

        // key bindings
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

        // loop
        Timer timer = new Timer(TICK_MS, e -> {
            if (!isShowing()) return;
            updatePositionAndCollisions(frame);
            repaint();
        });
        timer.start();

        // when panel becomes visible, ensure spawn & hitboxes correct and focus ready
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

    private Image loadEntranceImage(String path, int targetW, int targetH) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) return null;
            Image img = ImageIO.read(is);
            return img.getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Image loadImageRaw(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) return null;
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // called by AppFrame before showing map
    public void refreshPlayer(AppFrame frame) {
        clearMovementFlags();
        Player p = frame.getCurrentPlayer();
        if (p != null) {
            roleText = p.getPlayerClass().name();
            sprite = loadSpriteForRole(roleText);
        }
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

        // upgrade: top middle
        upgradeRect = new Rectangle(
                (w / 2) - (ENTR_W / 2),
                30,
                ENTR_W, ENTR_H
        );

        // shop: bottom middle
        shopRect = new Rectangle(
                (w / 2) - (ENTR_W / 2),
                h - ENTR_H - 30,
                ENTR_W, ENTR_H
        );

        // dungeon: right middle
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

    private Image loadSpriteForRole(String role) {
        String path;
        switch (role.toUpperCase()) {
            case "WARRIOR": path = "/images/warrior.png"; break;
            case "ARCHER":  path = "/images/archer.png"; break;
            case "MAGE":    path = "/images/mage.png"; break;
            default:        path = "/images/warrior.png";
        }

        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) return null;
            Image img = ImageIO.read(is);
            return img.getScaledInstance(SPRITE_W, SPRITE_H, Image.SCALE_FAST);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
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

        // draw entrances
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (mapBg != null) {
            int w = getWidth();
            int h = getHeight();

            if (mapBgScaled == null || mapBgScaledW != w || mapBgScaledH != h) {
                mapBgScaled = mapBg.getScaledInstance(w, h, Image.SCALE_FAST);
                mapBgScaledW = w;
                mapBgScaledH = h;
            }

            g2.drawImage(mapBgScaled, 0, 0, null);
        }

        // draw player
        if (sprite != null) {
            g2.drawImage(sprite, x, y, null);
        } else {
            g2.setColor(new Color(60, 120, 200));
            g2.fillRect(x, y, SPRITE_W, SPRITE_H);
        }

        drawCoinHUD(g2, frame.getCurrentCoin());

        g2.dispose();
    }

    private void drawCoinHUD(Graphics2D g2, int coin) {
        String text = "Coin: " + coin;

        g2.setFont(getFont().deriveFont(Font.BOLD, 14f));
        FontMetrics fm = g2.getFontMetrics();

        int padding = 10;
        int iconSize = 20;
        int gap = 6;

        int textW = fm.stringWidth(text);
        int textH = fm.getAscent();

        int totalW = (coinImg != null) ? (iconSize + gap + textW) : textW;
        int x = getWidth() - padding - totalW;
        int y = getHeight() - padding - 6;

        // draw icon if exists
        if (coinImg != null) {
            int iconY = y - iconSize + 4;
            g2.drawImage(coinImg, x, iconY, null);
            x += iconSize + gap;
        }

        g2.setColor(Color.BLACK);
        g2.drawString(text, x, y);
    }


    private void drawEntrance(Graphics2D g2, Rectangle r, String label, Image img) {
        // If image missing -> fallback to old box + centered text
        if (img == null) {
            g2.setColor(new Color(230, 230, 230));
            g2.fillRoundRect(r.x, r.y, r.width, r.height, 16, 16);

            g2.setColor(new Color(160, 160, 160));
            g2.drawRoundRect(r.x, r.y, r.width, r.height, 16, 16);

            g2.setFont(getFont().deriveFont(Font.BOLD, 14f));
            FontMetrics fm = g2.getFontMetrics();
            int tx = r.x + (r.width - fm.stringWidth(label)) / 2;
            int ty = r.y + (r.height + fm.getAscent()) / 2 - 3;

            g2.setColor(Color.DARK_GRAY);
            g2.drawString(label, tx, ty);
            return;
        }

        // Draw container
        g2.setColor(new Color(245, 245, 245));
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 16, 16);

        g2.setColor(new Color(160, 160, 160));
        g2.drawRoundRect(r.x, r.y, r.width, r.height, 16, 16);

        // Reserve bottom space for label
        int labelH = 18;
        int imgAreaX = r.x + 8;
        int imgAreaY = r.y + 6;
        int imgAreaW = r.width - 16;
        int imgAreaH = r.height - (labelH + 10);

        // Draw image centered in image area
        int iw = img.getWidth(null);
        int ih = img.getHeight(null);
        int ix = imgAreaX + (imgAreaW - iw) / 2;
        int iy = imgAreaY + (imgAreaH - ih) / 2;
        g2.drawImage(img, ix, iy, null);

        // Draw label at bottom (centered)
        g2.setFont(getFont().deriveFont(Font.BOLD, 13f));
        FontMetrics fm = g2.getFontMetrics();
        int tx = r.x + (r.width - fm.stringWidth(label)) / 2;
        int ty = r.y + r.height - 6;

        g2.setColor(Color.DARK_GRAY);
        g2.drawString(label, tx, ty);
    }


    private void clearMovementFlags() {
        up = down = left = right = false;
    }

}
