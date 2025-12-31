package miniRPG.ui.screen;

import javax.swing.*;
import java.awt.*;

public final class UiKit {

    public static final Dimension HUD_SIZE = new Dimension(220, 74);
    public static final Dimension BACK_SIZE = new Dimension(120, 40);
    public static final Dimension EXIT_SIZE = new Dimension(80, 34);
    public static final Dimension BTN_GHOST_SIZE = new Dimension(120, 40);

    private UiKit() {}

    public static void setFixedSize(JComponent c, Dimension d) {
        c.setPreferredSize(d);
        c.setMinimumSize(d);
        c.setMaximumSize(d);
    }

    public static void applyGhost(AbstractButton btn) {
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(35, 35, 35));
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 70), 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
    }

    public static void applyPrimary(AbstractButton btn) {
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(25, 25, 25));
        btn.setOpaque(true);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 14f));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1),
                BorderFactory.createEmptyBorder(10, 22, 10, 22)
        ));
    }

    public static JButton createExitButton(Runnable onClick) {
        JButton b = new JButton("Exit");
        applyGhost(b);
        setFixedSize(b, EXIT_SIZE);
        b.addActionListener(e -> onClick.run());
        return b;
    }

    public static JButton createGoBackButton(Runnable onClick) {
        JButton b = new JButton("Go Back");
        applyGhost(b);
        setFixedSize(b, BACK_SIZE);
        b.addActionListener(e -> onClick.run());
        return b;
    }

    public static HudPanel buildHud(AppFrame frame) {
        HudPanel hud = new HudPanel();
        hud.bind(frame);
        setFixedSize(hud, HUD_SIZE);
        return hud;
    }

    public static JPanel topBarLeftHud(JComponent hud) {
        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        north.add(hud, BorderLayout.WEST);
        return north;
    }

    public static JPanel bottomRightWrap(JComponent c) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        p.add(c, gbc);
        return p;
    }
}
