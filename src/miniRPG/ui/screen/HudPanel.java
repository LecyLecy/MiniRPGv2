package miniRPG.ui.screen;

import miniRPG.character.Player;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HudPanel extends JPanel implements PropertyChangeListener {

    private final JLabel coinLabel = new JLabel("Gold: 0");
    private final JLabel levelLabel = new JLabel("Level 1");
    private final JLabel expLabel = new JLabel("EXP 0/1000");

    private AppFrame boundFrame;

    public HudPanel() {
        setOpaque(true);
        setBackground(new Color(20, 20, 20, 200));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 70), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        styleLabel(coinLabel, 12f, false);
        styleLabel(levelLabel, 13f, true);
        styleLabel(expLabel, 12f, false);

        add(coinLabel);
        add(Box.createVerticalStrut(2));
        add(levelLabel);
        add(Box.createVerticalStrut(2));
        add(expLabel);
    }

    public void bind(AppFrame frame) {
        if (boundFrame != null) {
            boundFrame.removeAppListener(this);
        }
        boundFrame = frame;
        if (boundFrame != null) {
            boundFrame.addAppListener(this);
            syncFromFrame(boundFrame);
        }
    }

    public void syncFromFrame(AppFrame frame) {
        if (frame == null) return;

        int coin = frame.getCurrentCoin();
        int exp = frame.getCurrentExp();

        int level = (exp / 1000) + 1;
        int into = exp % 1000;
        int nextTotal = level * 1000;

        coinLabel.setText("Gold: " + coin);
        levelLabel.setText("Level " + level);
        expLabel.setText("EXP " + (into == 0 ? 0 : (exp - ((level - 1) * 1000))) + "/" + nextTotal);

        Player p = frame.getCurrentPlayer();
        if (p != null) {
            int pl = p.getLevel();
            int pNext = pl * 1000;
            expLabel.setText("EXP " + p.getExp() + "/" + pNext);
            levelLabel.setText("Level " + pl);
        }
    }

    private void styleLabel(JLabel l, float size, boolean bold) {
        l.setForeground(Color.WHITE);
        Font f = l.getFont().deriveFont(bold ? Font.BOLD : Font.PLAIN, size);
        l.setFont(f);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (boundFrame == null) return;

        String p = evt.getPropertyName();
        if ("coin".equals(p) || "exp".equals(p) || "stats".equals(p)) {
            SwingUtilities.invokeLater(() -> syncFromFrame(boundFrame));
        }
    }
}
