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

    public UpgradePanel(AppFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setOpaque(true);

        // background
        bgRaw = loadImageRaw("/images/upgrade.png");

        HudPanel hud = new HudPanel();
        hud.syncFromFrame(frame);
        hud.bind(frame);

        JButton back = new JButton("Go Back");
        back.addActionListener(e -> frame.openMap());

        back.setFocusPainted(false);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        back.setBackground(new Color(0, 0, 0, 160));
        back.setForeground(Color.WHITE);
        back.setOpaque(true);

        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);

        JPanel rightStack = new JPanel();
        rightStack.setOpaque(false);
        rightStack.setLayout(new BoxLayout(rightStack, BoxLayout.Y_AXIS));

        hud.setAlignmentX(Component.RIGHT_ALIGNMENT);
        back.setAlignmentX(Component.RIGHT_ALIGNMENT);

        rightStack.add(hud);
        rightStack.add(Box.createVerticalStrut(8));
        rightStack.add(back);

        south.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 12));
        south.add(rightStack, BorderLayout.EAST);

        add(south, BorderLayout.SOUTH);

        addHierarchyListener(e -> {
            if (isShowing()) hud.syncFromFrame(frame);
        });
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
            // fallback if image missing
            g.setColor(new Color(25, 25, 25));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
