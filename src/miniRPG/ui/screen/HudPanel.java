package miniRPG.ui.screen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;

public class HudPanel extends JPanel {

    private final JLabel coinValue = new JLabel("0");
    private final JLabel levelValue = new JLabel("Level 1");
    private final JProgressBar expBar = new JProgressBar(0, 1000);

    private Image coinIcon;

    private AppFrame frame;
    private PropertyChangeListener listener;

    public HudPanel() {
        setOpaque(false);
        setLayout(new BorderLayout(10, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        coinIcon = loadIcon("/images/coin.png", 18, 18);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);

        JLabel coinImg = new JLabel();
        if (coinIcon != null) coinImg.setIcon(new ImageIcon(coinIcon));

        coinValue.setForeground(Color.WHITE);
        coinValue.setFont(coinValue.getFont().deriveFont(Font.BOLD, 14f));

        levelValue.setForeground(Color.WHITE);
        levelValue.setFont(levelValue.getFont().deriveFont(Font.BOLD, 13f));

        top.add(coinImg);
        top.add(coinValue);
        top.add(Box.createHorizontalStrut(10));
        top.add(levelValue);

        JPanel expRow = new JPanel(new BorderLayout(8, 0));
        expRow.setOpaque(false);

        JLabel expLabel = new JLabel("EXP");
        expLabel.setForeground(new Color(180, 210, 255));
        expLabel.setFont(expLabel.getFont().deriveFont(Font.BOLD, 12f));

        expBar.setOpaque(false);
        expBar.setBorderPainted(false);
        expBar.setStringPainted(true);
        expBar.setForeground(new Color(80, 170, 255));
        expBar.setBackground(new Color(0, 0, 0, 90));
        expBar.setFont(expBar.getFont().deriveFont(Font.BOLD, 11f));

        expRow.add(expLabel, BorderLayout.WEST);
        expRow.add(expBar, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
        add(expRow, BorderLayout.SOUTH);
    }

    public void bind(AppFrame frame) {
        this.frame = frame;

        if (listener != null) frame.removeAppListener(listener);

        listener = (PropertyChangeEvent evt) -> {
            String n = evt.getPropertyName();
            if ("coin".equals(n) || "exp".equals(n) || "stats".equals(n)) {
                syncFromFrame(frame);
            }
        };

        frame.addAppListener(listener);
        syncFromFrame(frame);
    }

    public void syncFromFrame(AppFrame frame) {
        int coin = frame.getCurrentCoin();
        int exp = frame.getCurrentExp();

        int level = (exp / 1000) + 1;
        int nextTotal = level * 1000;

        int intoLevel = exp - ((level - 1) * 1000);
        int need = 1000;

        coinValue.setText(String.valueOf(coin));
        levelValue.setText("Level " + level);

        expBar.setMinimum(0);
        expBar.setMaximum(need);
        expBar.setValue(Math.max(0, Math.min(need, intoLevel)));
        expBar.setString(exp + "/" + nextTotal);

        repaint();
    }

    private Image loadIcon(String path, int w, int h) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) return null;
            BufferedImage raw = ImageIO.read(is);
            return raw.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setClip(0, 0, getWidth(), getHeight());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(new Color(0, 0, 0, 130));
        g2.fillRoundRect(0, 0, w, h, 18, 18);

        g2.setColor(new Color(255, 255, 255, 70));
        g2.drawRoundRect(0, 0, w - 1, h - 1, 18, 18);

        g2.dispose();
    }
}
