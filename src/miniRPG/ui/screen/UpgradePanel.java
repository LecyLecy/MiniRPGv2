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
        bgRaw = loadImageRaw("/images/upgrade.png");

        JButton back = new JButton("Go Back");
        back.addActionListener(e -> frame.openMap());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false); // so background shows through
        bottom.add(back);

        add(bottom, BorderLayout.SOUTH);
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
                bgW = w; bgH = h;
            }
            g.drawImage(bgScaled, 0, 0, null);
        }
    }
}
