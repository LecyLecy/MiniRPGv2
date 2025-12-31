package miniRPG.ui.component;

import javax.swing.*;
import java.awt.*;

public class FadeOverlay extends JComponent {

    private float alpha = 0f;
    private Timer timer;
    private int holdTicks = 0;

    private Runnable midAction;
    private Runnable endAction;

    private enum Phase { FADE_OUT, HOLD_BLACK, FADE_IN }
    private Phase phase;

    public FadeOverlay() {
        setOpaque(false);
        setFocusable(false);
        setVisible(false);
    }

    public void play(Runnable midAction, Runnable endAction) {
        this.midAction = midAction;
        this.endAction = endAction;

        alpha = 0f;
        phase = Phase.FADE_OUT;
        holdTicks = 0;

        setVisible(true);

        if (timer != null && timer.isRunning()) timer.stop();

        timer = new Timer(16, e -> tick());
        timer.start();
    }

    private void tick() {
        final float step = 0.08f;

        switch (phase) {
            case FADE_OUT:
                alpha = Math.min(1f, alpha + step);
                repaint();
                if (alpha >= 1f) {
                    if (midAction != null) midAction.run();
                    phase = Phase.HOLD_BLACK;
                    holdTicks = 8;
                }
                break;

            case HOLD_BLACK:
                repaint();
                holdTicks--;
                if (holdTicks <= 0) {
                    phase = Phase.FADE_IN;
                }
                break;

            case FADE_IN:
                alpha = Math.max(0f, alpha - step);
                repaint();
                if (alpha <= 0f) {
                    timer.stop();
                    setVisible(false);
                    if (endAction != null) endAction.run();
                }
                break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (alpha <= 0f) return;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, Math.round(alpha * 255)));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}
