package miniRPG.ui.screen;

import javax.swing.*;
import java.awt.*;

public final class UiKit {

    public static final Dimension HUD_SIZE = new Dimension(220, 74);
    public static final Dimension BTN_GHOST_SIZE = new Dimension(84, 34);
    public static final Dimension BTN_PRIMARY_SIZE = new Dimension(150, 44);

    private UiKit() {}

    public static void applyGhost(JButton b) {
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        b.setBackground(new Color(35, 35, 35));
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setRolloverEnabled(false);
    }

    public static void applyPrimary(JButton b) {
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
        b.setContentAreaFilled(true);
        b.setRolloverEnabled(false);
    }

    public static void setFixedSize(JComponent c, Dimension d) {
        c.setPreferredSize(d);
        c.setMinimumSize(d);
        c.setMaximumSize(d);
    }

    public static HudPanel buildHud(AppFrame frame) {
        HudPanel hud = new HudPanel();
        hud.bind(frame);
        UiKit.setFixedSize(hud, UiKit.HUD_SIZE);
        setFixedSize(hud, HUD_SIZE);
        return hud;
    }

    public static JPanel topBarLeftHud(HudPanel hud) {
        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        north.add(hud, BorderLayout.WEST);
        return north;
    }

    public static JPanel topBarCenterAndRight(JComponent center, HudPanel hud) {
        JPanel north = new JPanel(new GridBagLayout());
        north.setOpaque(false);
        north.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));

        JPanel leftPad = new JPanel();
        leftPad.setOpaque(false);
        leftPad.setPreferredSize(HUD_SIZE);

        GridBagConstraints a = new GridBagConstraints();
        a.gridx = 0;
        a.gridy = 0;
        a.weightx = 0;
        a.anchor = GridBagConstraints.WEST;
        north.add(leftPad, a);

        GridBagConstraints b = new GridBagConstraints();
        b.gridx = 1;
        b.gridy = 0;
        b.weightx = 1;
        b.fill = GridBagConstraints.HORIZONTAL;
        b.anchor = GridBagConstraints.CENTER;
        north.add(center, b);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0;
        c.anchor = GridBagConstraints.EAST;
        north.add(hud, c);

        return north;
    }

    public static JPanel bottomBarCenterAndRight(JComponent center, JComponent right, int bottomInset, int rightInset) {
        JPanel south = new JPanel(new GridBagLayout());
        south.setOpaque(false);
        south.setBorder(BorderFactory.createEmptyBorder(0, 12, bottomInset, rightInset));

        GridBagConstraints a = new GridBagConstraints();
        a.gridx = 0;
        a.gridy = 0;
        a.weightx = 1;
        a.anchor = GridBagConstraints.SOUTH;
        a.insets = new Insets(0, 0, 18, 0);
        south.add(center, a);

        GridBagConstraints b = new GridBagConstraints();
        b.gridx = 1;
        b.gridy = 0;
        b.weightx = 0;
        b.anchor = GridBagConstraints.SOUTHEAST;
        b.insets = new Insets(0, 12, 18, 0);
        south.add(right, b);

        return south;
    }
}
