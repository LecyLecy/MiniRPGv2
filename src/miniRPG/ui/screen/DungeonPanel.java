package miniRPG.ui.screen;

import miniRPG.character.Player;
import miniRPG.monster.Monster;
import miniRPG.monster.MonsterFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class DungeonPanel extends JPanel {

    private final AppFrame frame;

    private Image bgRaw;
    private Image bgScaled;
    private int bgW = -1, bgH = -1;

    private int floor = 1;
    private Monster monster;

    private Image playerSprite;
    private Image monsterSprite;

    private boolean playerTurn = true;
    private boolean busy = false;

    private final JButton attackBtn = new JButton("ATTACK");
    private final JButton backBtn = new JButton("Go Back");
    private final HudPanel hud = new HudPanel();

    private static final int SPR_W = 96;
    private static final int SPR_H = 96;

    private float fadeAlpha = 0f;
    private Timer fadeTimer;
    private int holdTicks = 0;
    private Runnable fadeMid;
    private Runnable fadeEnd;

    private enum FadePhase { OUT, HOLD, IN }
    private FadePhase fadePhase;
    private final JLabel floorLabel = new JLabel("", SwingConstants.CENTER);

    public DungeonPanel(AppFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setOpaque(true);

        bgRaw = loadImageRaw("/images/dungeon.png");

        hud.bind(frame);

        Dimension hudSize = new Dimension(220, 74);
        hud.setPreferredSize(hudSize);
        hud.setMinimumSize(hudSize);
        hud.setMaximumSize(hudSize);

        floorLabel.setText("Floor 1");
        floorLabel.setForeground(Color.WHITE);
        floorLabel.setFont(floorLabel.getFont().deriveFont(Font.BOLD, 14f));

        JPanel north = new JPanel(new GridBagLayout());
        north.setOpaque(false);
        north.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));

        GridBagConstraints n0 = new GridBagConstraints();
        n0.gridx = 0;
        n0.gridy = 0;
        n0.weightx = 0;
        n0.anchor = GridBagConstraints.WEST;
        north.add(hud, n0);

        GridBagConstraints n1 = new GridBagConstraints();
        n1.gridx = 1;
        n1.gridy = 0;
        n1.weightx = 1;
        n1.anchor = GridBagConstraints.CENTER;
        n1.fill = GridBagConstraints.HORIZONTAL;
        north.add(floorLabel, n1);

        JPanel rightPad = new JPanel();
        rightPad.setOpaque(false);
        rightPad.setPreferredSize(hudSize);

        GridBagConstraints n2 = new GridBagConstraints();
        n2.gridx = 2;
        n2.gridy = 0;
        n2.weightx = 0;
        n2.anchor = GridBagConstraints.EAST;
        north.add(rightPad, n2);

        add(north, BorderLayout.NORTH);

        stylePrimaryButton(attackBtn);
        attackBtn.addActionListener(e -> onAttack());

        styleGhostButton(backBtn);
        backBtn.addActionListener(e -> {
            Player p = frame.getCurrentPlayer();
            if (p != null) p.restoreFull();
            frame.openMap();
        });

        Dimension atkSize = new Dimension(150, 44);
        attackBtn.setPreferredSize(atkSize);
        attackBtn.setMinimumSize(atkSize);
        attackBtn.setMaximumSize(atkSize);

        Dimension backSize = new Dimension(120, 40);
        backBtn.setPreferredSize(backSize);
        backBtn.setMinimumSize(backSize);
        backBtn.setMaximumSize(backSize);

        JPanel south = new JPanel(new GridBagLayout());
        south.setOpaque(false);
        south.setBorder(BorderFactory.createEmptyBorder(0, 12, 16, 12));

        GridBagConstraints s0 = new GridBagConstraints();
        s0.gridx = 0;
        s0.gridy = 0;
        s0.weightx = 1;
        s0.anchor = GridBagConstraints.SOUTH;
        s0.insets = new Insets(0, 0, 18, 0);
        south.add(attackBtn, s0);

        GridBagConstraints s1 = new GridBagConstraints();
        s1.gridx = 1;
        s1.gridy = 0;
        s1.weightx = 0;
        s1.anchor = GridBagConstraints.SOUTHEAST;
        s1.insets = new Insets(0, 12, 18, 0);
        south.add(backBtn, s1);

        add(south, BorderLayout.SOUTH);

        addHierarchyListener(e -> {
            if (isShowing()) {
                hud.syncFromFrame(frame);
                startNewRun();
            }
        });
    }

    private void startNewRun() {
        floor = 1;
        floorLabel.setText("Floor " + floor);

        playerTurn = true;
        busy = false;
        attackBtn.setEnabled(true);
        setupFloor();
        repaint();
    }

    private void setupFloor() {
        floorLabel.setText("Floor " + floor);
        Player p = frame.getCurrentPlayer();
        if (p != null) {
            p.setExp(frame.getCurrentExp());
            p.restoreFull();
            playerSprite = loadPlayerSprite(p);
        } else {
            playerSprite = null;
        }

        monster = MonsterFactory.createMonster(floor);
        monster.restoreFull();
        monsterSprite = loadMonsterSprite(monster);

        playerTurn = true;
        busy = false;
        attackBtn.setEnabled(true);
    }

    private void onAttack() {
        if (busy || !playerTurn) return;

        Player p = frame.getCurrentPlayer();
        if (p == null || monster == null) return;

        busy = true;
        playerTurn = false;
        attackBtn.setEnabled(false);

        int dmg = Math.max(1, p.getAttack() - monster.getDefense());
        monster.receiveDamage(dmg);

        repaint();

        timerOnce(500, () -> {
            if (!monster.isAlive()) {
                onMonsterDefeated();
            } else {
                onMonsterTurn();
            }
        });
    }

    private void onMonsterTurn() {
        Player p = frame.getCurrentPlayer();
        if (p == null || monster == null) return;

        timerOnce(500, () -> {
            int dmg = Math.max(1, monster.getAttack() - p.getDefense());
            p.receiveDamage(dmg);
            repaint();

            timerOnce(500, () -> {
                if (!p.isAlive()) {
                    onPlayerDefeated();
                } else {
                    playerTurn = true;
                    busy = false;
                    attackBtn.setEnabled(true);
                }
            });
        });
    }

    private void onMonsterDefeated() {
        int goldGain = 10 * floor;
        int expGain = monster.getExpReward();
        if (floor == 5 || floor == 10 || floor == 15 || floor == 20 || floor == 25 || floor == 30) {
            expGain += 40;
        }

        frame.addCoin(goldGain);
        frame.addExp(expGain);

        Player p = frame.getCurrentPlayer();
        if (p != null) p.setExp(frame.getCurrentExp());

        playLocalFade(() -> {
            floor++;
            floorLabel.setText("Floor " + floor);
            setupFloor();
        }, null);
    }

    private void onPlayerDefeated() {
        Player p = frame.getCurrentPlayer();
        if (p != null) p.restoreFull();
        frame.openMap();
    }

    private void timerOnce(int ms, Runnable r) {
        Timer t = new Timer(ms, e -> r.run());
        t.setRepeats(false);
        t.start();
    }

    private void playLocalFade(Runnable midAction, Runnable endAction) {
        fadeMid = midAction;
        fadeEnd = endAction;

        fadeAlpha = 0f;
        fadePhase = FadePhase.OUT;
        holdTicks = 0;

        if (fadeTimer != null && fadeTimer.isRunning()) fadeTimer.stop();

        fadeTimer = new Timer(16, e -> tickFade());
        fadeTimer.start();
    }

    private void tickFade() {
        final float step = 0.08f;

        switch (fadePhase) {
            case OUT:
                fadeAlpha = Math.min(1f, fadeAlpha + step);
                repaint();
                if (fadeAlpha >= 1f) {
                    if (fadeMid != null) fadeMid.run();
                    fadePhase = FadePhase.HOLD;
                    holdTicks = 8;
                }
                break;

            case HOLD:
                repaint();
                holdTicks--;
                if (holdTicks <= 0) fadePhase = FadePhase.IN;
                break;

            case IN:
                fadeAlpha = Math.max(0f, fadeAlpha - step);
                repaint();
                if (fadeAlpha <= 0f) {
                    fadeTimer.stop();
                    if (fadeEnd != null) fadeEnd.run();
                }
                break;
        }
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
        } else {
            g.setColor(new Color(30, 30, 30));
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int pillX = Math.max(12, getWidth() - 210 - 46);

        int y = 140;
        int px = 80;
        int mx = getWidth() - 80 - SPR_W;

        if (playerSprite != null) g2.drawImage(playerSprite, px, y, null);
        else {
            g2.setColor(new Color(200, 200, 200, 180));
            g2.fillRoundRect(px, y, SPR_W, SPR_H, 18, 18);
        }

        if (monsterSprite != null) g2.drawImage(monsterSprite, mx, y, null);
        else {
            g2.setColor(new Color(200, 200, 200, 180));
            g2.fillRoundRect(mx, y, SPR_W, SPR_H, 18, 18);
        }

        Player p = frame.getCurrentPlayer();
        if (p != null) drawHpBar(g2, px, y + SPR_H + 10, 160, 14, p.getCurrentHP(), p.getMaxHP());
        if (monster != null) drawHpBar(g2, mx - 64, y + SPR_H + 10, 160, 14, monster.getCurrentHP(), monster.getMaxHP());

        if (fadeAlpha > 0f) {
            g2.setColor(new Color(0, 0, 0, Math.round(fadeAlpha * 255)));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        g2.dispose();
    }

    private void drawPill(Graphics2D g2, String text, int x, int y) {
        g2.setFont(getFont().deriveFont(Font.BOLD, 14f));
        FontMetrics fm = g2.getFontMetrics();
        int padX = 10;
        int padY = 6;
        int w = fm.stringWidth(text) + padX * 2;
        int h = fm.getHeight() + padY;

        g2.setColor(new Color(0, 0, 0, 140));
        g2.fillRoundRect(x, y, w, h, 18, 18);

        g2.setColor(Color.WHITE);
        g2.drawString(text, x + padX, y + h - 8);
    }

    private void drawHpBar(Graphics2D g2, int x, int y, int w, int h, int cur, int max) {
        float pct = (max <= 0) ? 0f : Math.max(0f, Math.min(1f, cur / (float) max));

        g2.setColor(new Color(0, 0, 0, 140));
        g2.fillRoundRect(x, y, w, h + 18, 12, 12);

        int barX = x + 8;
        int barY = y + 6;
        int barW = w - 16;

        g2.setColor(new Color(255, 255, 255, 60));
        g2.fillRoundRect(barX, barY, barW, h, 10, 10);

        g2.setColor(new Color(80, 220, 120, 200));
        g2.fillRoundRect(barX, barY, Math.round(barW * pct), h, 10, 10);

        g2.setFont(getFont().deriveFont(Font.BOLD, 12f));
        g2.setColor(Color.WHITE);
        String txt = cur + "/" + max;
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(txt, x + (w - fm.stringWidth(txt)) / 2, y + h + 18 - 6);
    }

    private Image loadPlayerSprite(Player p) {
        String path;
        switch (p.getPlayerClass()) {
            case WARRIOR: path = "/images/warrior.png"; break;
            case ARCHER:  path = "/images/archer.png"; break;
            case MAGE:    path = "/images/mage.png"; break;
            default:      path = "/images/warrior.png";
        }
        return loadScaled(path, SPR_W, SPR_H);
    }

    private Image loadMonsterSprite(Monster m) {
        String n = (m.getName() == null) ? "" : m.getName().toLowerCase();
        String path;
        if (n.contains("corrupted")) path = "/images/corrupted_slime.png";
        else if (n.contains("slime")) path = "/images/slime.png";
        else if (n.contains("shadow")) path = "/images/shadow.png";
        else if (n.contains("skeleton")) path = "/images/armored_skeleton.png";
        else path = null;

        if (path == null) return null;
        return loadScaled(path, SPR_W, SPR_H);
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

    private void stylePrimaryButton(JButton b) {
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(b.getFont().deriveFont(Font.BOLD, 14f));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1),
                BorderFactory.createEmptyBorder(10, 22, 10, 22)
        ));
        b.setBackground(new Color(25, 25, 25));
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
    }

    private void styleGhostButton(JButton b) {
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        b.setBackground(new Color(35, 35, 35));
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
    }
}
