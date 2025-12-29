package miniRPG.ui.screen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class ShopPanel extends JPanel {

    private final AppFrame frame;

    // background
    private Image bgRaw;
    private Image bgScaled;
    private int bgW = -1, bgH = -1;

    // HUD (coin + level/exp)
    private final HudPanel hud = new HudPanel();

    public ShopPanel(AppFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        bgRaw = loadImageRaw("/images/shop.png");

        // Back button (styled)
        JButton back = new JButton("Go Back");
        styleOverlayButton(back);
        back.addActionListener(e -> frame.openMap());

        // Bottom-right stack: HUD above, Back below
        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);

        JPanel rightStack = new JPanel();
        rightStack.setOpaque(false);
        rightStack.setLayout(new BoxLayout(rightStack, BoxLayout.Y_AXIS));

        hud.bind(frame);
        hud.setAlignmentX(Component.RIGHT_ALIGNMENT);
        back.setAlignmentX(Component.RIGHT_ALIGNMENT);

        rightStack.add(hud);
        rightStack.add(Box.createVerticalStrut(8));
        rightStack.add(back);

        south.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 12));
        south.add(rightStack, BorderLayout.EAST);
        add(south, BorderLayout.SOUTH);

        // refresh HUD whenever panel becomes visible
        addHierarchyListener(e -> {
            if (isShowing()) hud.syncFromFrame(frame);
        });
    }

    private void styleOverlayButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0, 0, 0, 170));
        btn.setOpaque(true);

        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 70), 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
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
        }
    }
}
