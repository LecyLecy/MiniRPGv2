package miniRPG.ui.screen;

import miniRPG.character.Player;
import miniRPG.map.MapController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;

public class MapPanel extends JPanel {

    private static final int STEP = 8;
    private static final int TICK_MS = 16;

    private static final int SPR_W = 56;
    private static final int SPR_H = 56;

    private static final int ENTR_W = 110;
    private static final int ENTR_H = 70;

    private final AppFrame frame;

    private Image mapBgRaw;
    private Image mapBgScaled;
    private int bgW = -1, bgH = -1;

    private Image playerSprite;

    private final HudPanel hud = new HudPanel();
    private final MapController controller;

    private Timer timer;

    private boolean spawnPending = true;
    private boolean entranceLocked = false;

    public MapPanel(AppFrame frame) {
        this.frame = frame;

        setLayout(new BorderLayout());
        setOpaque(true);
        setFocusable(true);

        mapBgRaw = loadImageRaw("/images/map.png");

        hud.bind(frame);
        UiKit.setFixedSize(hud, UiKit.HUD_SIZE);
        add(UiKit.topBarLeftHud(hud), BorderLayout.NORTH);

        controller = new MapController(STEP, SPR_W, SPR_H, ENTR_W, ENTR_H);

        bindKeys();

        timer = new Timer(TICK_MS, e -> onTick());

        addHierarchyListener(e -> {
            if (isShowing()) {
                requestFocusInWindow();
                hud.syncFromFrame(frame);
                refreshPlayer(frame);
                if (!timer.isRunning()) timer.start();
            } else {
                controller.resetInput();
                entranceLocked = false;
                if (timer.isRunning()) timer.stop();
            }
        });
    }

    private void bindKeys() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        bindKey(im, am, "pressed W", "upOn", () -> controller.setUp(true));
        bindKey(im, am, "released W", "upOff", () -> controller.setUp(false));

        bindKey(im, am, "pressed S", "downOn", () -> controller.setDown(true));
        bindKey(im, am, "released S", "downOff", () -> controller.setDown(false));

        bindKey(im, am, "pressed A", "leftOn", () -> controller.setLeft(true));
        bindKey(im, am, "released A", "leftOff", () -> controller.setLeft(false));

        bindKey(im, am, "pressed D", "rightOn", () -> controller.setRight(true));
        bindKey(im, am, "released D", "rightOff", () -> controller.setRight(false));

        bindKey(im, am, "pressed UP", "upOn2", () -> controller.setUp(true));
        bindKey(im, am, "released UP", "upOff2", () -> controller.setUp(false));

        bindKey(im, am, "pressed DOWN", "downOn2", () -> controller.setDown(true));
        bindKey(im, am, "released DOWN", "downOff2", () -> controller.setDown(false));

        bindKey(im, am, "pressed LEFT", "leftOn2", () -> controller.setLeft(true));
        bindKey(im, am, "released LEFT", "leftOff2", () -> controller.setLeft(false));

        bindKey(im, am, "pressed RIGHT", "rightOn2", () -> controller.setRight(true));
        bindKey(im, am, "released RIGHT", "rightOff2", () -> controller.setRight(false));
    }

    private void bindKey(InputMap im, ActionMap am, String stroke, String key, Runnable r) {
        im.put(KeyStroke.getKeyStroke(stroke), key);
        am.put(key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                r.run();
            }
        });
    }

    private void onTick() {
        if (!isShowing()) return;

        if (spawnPending && getWidth() > 0 && getHeight() > 0) {
            controller.resetSpawn(getWidth(), getHeight());
            spawnPending = false;
        }

        controller.tick(getWidth(), getHeight());

        if (!entranceLocked) {
            String target = controller.consumeEntranceIfAny();
            if (target != null) {
                entranceLocked = true;
                controller.resetInput();
                frame.openEntrance(target);
                return;
            }
        }

        repaint();
    }

    public void resetSpawn() {
        spawnPending = true;
        entranceLocked = false;
        controller.resetInput();
        requestFocusInWindow();
    }

    public void refreshPlayer(AppFrame frame) {
        Player p = frame.getCurrentPlayer();
        if (p == null) {
            playerSprite = null;
            return;
        }
        playerSprite = loadScaled(p.getSpritePath(), SPR_W, SPR_H);
    }

    private Image loadScaled(String path, int w, int h) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) return null;
            Image img = ImageIO.read(is);
            return img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            return null;
        }
    }

    private Image loadImageRaw(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) return null;
            return ImageIO.read(is);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (mapBgRaw != null) {
            int w = getWidth();
            int h = getHeight();
            if (mapBgScaled == null || bgW != w || bgH != h) {
                mapBgScaled = mapBgRaw.getScaledInstance(w, h, Image.SCALE_FAST);
                bgW = w;
                bgH = h;
            }
            g.drawImage(mapBgScaled, 0, 0, null);
        } else {
            g.setColor(new Color(45, 45, 45));
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = controller.getX();
        int y = controller.getY();

        if (playerSprite != null) {
            g2.drawImage(playerSprite, x, y, null);
        } else {
            g2.setColor(new Color(220, 220, 220, 180));
            g2.fillRoundRect(x, y, SPR_W, SPR_H, 18, 18);
        }

        g2.dispose();
    }
}
