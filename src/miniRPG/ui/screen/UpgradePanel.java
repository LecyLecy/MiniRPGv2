package miniRPG.ui.screen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class UpgradePanel extends JPanel {

    private final AppFrame frame;

    private Image bgRaw;
    private Image bgScaled;
    private int bgW = -1, bgH = -1;

    private final HudPanel hud = new HudPanel();

    public UpgradePanel(AppFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setOpaque(true);

        bgRaw = loadImageRaw("/images/upgrade.png");

        hud.bind(frame);
        Dimension hudSize = new Dimension(220, 74);
        hud.setPreferredSize(hudSize);
        hud.setMinimumSize(hudSize);
        hud.setMaximumSize(hudSize);

        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        north.add(hud, BorderLayout.WEST);
        add(north, BorderLayout.NORTH);

        JButton back = new JButton("Go Back");
        styleOverlayButton(back);
        back.addActionListener(e -> frame.openMap());

        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        south.setBorder(BorderFactory.createEmptyBorder(0, 12, 16, 12));
        south.add(back, BorderLayout.EAST);
        add(south, BorderLayout.SOUTH);

        addHierarchyListener(e -> {
            if (isShowing()) hud.syncFromFrame(frame);
        });
    }

    private void styleOverlayButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(35, 35, 35));
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 70), 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        Dimension backSize = new Dimension(120, 40);
        btn.setPreferredSize(backSize);
        btn.setMinimumSize(backSize);
        btn.setMaximumSize(backSize);
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
            g.setColor(new Color(25, 25, 25));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
