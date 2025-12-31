package miniRPG.ui.screen;

import miniRPG.character.Player;
import miniRPG.dungeon.DungeonController;
import miniRPG.monster.Monster;
import miniRPG.ui.asset.AssetLoader;

import javax.swing.*;
import java.awt.*;

public class DungeonPanel extends JPanel implements DungeonController.Session {

    private final AppFrame frame;

    private final AssetLoader assets = new AssetLoader(DungeonPanel.class);

    private Image bgRaw;
    private Image bgScaled;
    private int bgW = -1, bgH = -1;

    private Image playerSprite;
    private Image monsterSprite;

    private final HudPanel hud = new HudPanel();

    private final JButton attackBtn = new JButton("ATTACK");
    private final JButton skillBtn = new JButton("Skill:");
    private final JButton backBtn;

    private static final int SPR_W = 96;
    private static final int SPR_H = 96;

    private final JLabel floorLabel = new JLabel("", SwingConstants.CENTER);

    private final DungeonController controller;

    private float fadeAlpha = 0f;
    private Timer fadeTimer;
    private int holdTicks = 0;
    private Runnable fadeMid;
    private Runnable fadeEnd;

    private enum FadePhase { OUT, HOLD, IN }
    private FadePhase fadePhase;

    public DungeonPanel(AppFrame frame) {
        this.frame = frame;
        this.controller = new DungeonController(this);

        setLayout(new BorderLayout());
        setOpaque(true);

        bgRaw = assets.loadRaw("/images/dungeon.png");

        hud.bind(frame);
        UiKit.setFixedSize(hud, UiKit.HUD_SIZE);

        floorLabel.setText("Floor 1");
        floorLabel.setForeground(Color.WHITE);
        floorLabel.setFont(floorLabel.getFont().deriveFont(Font.BOLD, 14f));

        JPanel north = new JPanel(new GridBagLayout());
        north.setOpaque(false);
        north.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));

        GridBagConstraints a = new GridBagConstraints();
        a.gridx = 0;
        a.gridy = 0;
        a.weightx = 0;
        a.anchor = GridBagConstraints.WEST;
        north.add(hud, a);

        GridBagConstraints b = new GridBagConstraints();
        b.gridx = 1;
        b.gridy = 0;
        b.weightx = 1;
        b.fill = GridBagConstraints.HORIZONTAL;
        b.anchor = GridBagConstraints.CENTER;
        north.add(floorLabel, b);

        JPanel rightPad = new JPanel();
        rightPad.setOpaque(false);
        rightPad.setPreferredSize(UiKit.HUD_SIZE);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0;
        c.anchor = GridBagConstraints.EAST;
        north.add(rightPad, c);

        add(north, BorderLayout.NORTH);

        UiKit.applyPrimary(attackBtn);
        UiKit.setFixedSize(attackBtn, new Dimension(150, 44));
        attackBtn.addActionListener(e -> onAttackClicked());

        UiKit.applyGhost(skillBtn);
        UiKit.setFixedSize(skillBtn, new Dimension(150, 40));
        skillBtn.addActionListener(e -> onSkillClicked());

        backBtn = UiKit.createGoBackButton(() -> {
            Player p = frame.getCurrentPlayer();
            if (p != null) p.restoreFull();
            frame.openMap();
        });
        UiKit.setFixedSize(backBtn, UiKit.BTN_GHOST_SIZE);

        JPanel leftStack = new JPanel();
        leftStack.setOpaque(false);
        leftStack.setLayout(new BoxLayout(leftStack, BoxLayout.Y_AXIS));
        leftStack.add(skillBtn);
        leftStack.add(Box.createVerticalStrut(8));
        leftStack.add(attackBtn);

        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        south.setBorder(BorderFactory.createEmptyBorder(0, 12, 16, 12));
        south.add(leftStack, BorderLayout.WEST);
        south.add(UiKit.bottomRightWrap(backBtn), BorderLayout.EAST);

        add(south, BorderLayout.SOUTH);

        addHierarchyListener(e -> {
            if (isShowing()) {
                hud.syncFromFrame(frame);
                controller.startNewRun();
                refreshFromController();
            }
        });
    }

    private void onSkillClicked() {
        DungeonController.Result r = controller.useSkill();
        refreshFromController();
        repaint();

        if (r == DungeonController.Result.REQUEST_NEXT_FLOOR) {
            playLocalFade(() -> {
                controller.stepNextFloor();
                refreshFromController();
            }, null);
        }
    }

    private void onAttackClicked() {
        DungeonController.Result r = controller.attack();
        refreshFromController();
        repaint();

        if (r == DungeonController.Result.REQUEST_MONSTER_TURN) {
            timerOnce(500, this::doMonsterTurn);
        } else if (r == DungeonController.Result.REQUEST_NEXT_FLOOR) {
            playLocalFade(() -> {
                controller.stepNextFloor();
                refreshFromController();
            }, null);
        }
    }

    private void doMonsterTurn() {
        DungeonController.Result r = controller.stepMonsterTurn();
        refreshFromController();
        repaint();

        if (r == DungeonController.Result.PLAYER_DEAD) {
            Player p = frame.getCurrentPlayer();
            if (p != null) p.restoreFull();
            frame.openMap();
            return;
        }

        if (r == DungeonController.Result.REQUEST_AFTER_MONSTER_TURN) {
            timerOnce(500, () -> {
                controller.stepAfterMonsterTurn();
                refreshFromController();
                repaint();
            });
        }
    }

    private void refreshFromController() {
        floorLabel.setText("Floor " + controller.getFloor());

        Player p = frame.getCurrentPlayer();
        Monster m = controller.getMonster();

        if (p != null) playerSprite = assets.loadScaled(p.getSpritePath(), SPR_W, SPR_H, Image.SCALE_SMOOTH);
        else playerSprite = null;

        if (m != null) monsterSprite = assets.loadScaled(m.getSpritePath(), SPR_W, SPR_H, Image.SCALE_SMOOTH);
        else monsterSprite = null;

        attackBtn.setEnabled(controller.canAttack());

        skillBtn.setText(controller.getSkillButtonText());
        skillBtn.setEnabled(controller.canUseSkill());
    }

    @Override
    public Player getPlayer() {
        return frame.getCurrentPlayer();
    }

    @Override
    public int getCurrentExp() {
        return frame.getCurrentExp();
    }

    @Override
    public void addExp(int delta) {
        frame.addExp(delta);
    }

    @Override
    public void addCoin(int delta) {
        frame.addCoin(delta);
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
        Monster m = controller.getMonster();

        if (p != null) drawHpBar(g2, px, y + SPR_H + 10, 160, 14, p.getCurrentHP(), p.getMaxHP());
        if (m != null) drawHpBar(g2, mx - 64, y + SPR_H + 10, 160, 14, m.getCurrentHP(), m.getMaxHP());

        if (fadeAlpha > 0f) {
            g2.setColor(new Color(0, 0, 0, Math.round(fadeAlpha * 255)));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        g2.dispose();
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
}
