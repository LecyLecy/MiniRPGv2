package miniRPG.ui.screen;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

public class HudPanel extends JPanel implements PropertyChangeListener {

    private ImageIcon coinIcon;
    private final JLabel coinLabel = new JLabel();
    private final JLabel levelLabel = new JLabel();
    private final JProgressBar expBar = new JProgressBar();

    private int coin = 0;
    private int exp = 0;
    private AppFrame frame;

    public HudPanel() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        coinIcon = loadIcon("/images/coin.png", 18);

        // styles
        Font titleFont = getFont().deriveFont(Font.BOLD, 14f);
        Font smallFont = getFont().deriveFont(Font.PLAIN, 12f);

        coinLabel.setFont(titleFont);
        coinLabel.setForeground(Color.WHITE);

        levelLabel.setFont(smallFont);
        levelLabel.setForeground(new Color(230, 230, 230));

        expBar.setBorderPainted(false);
        expBar.setStringPainted(true);
        expBar.setFont(getFont().deriveFont(Font.BOLD, 11f));
        expBar.setOpaque(false);
        expBar.setPreferredSize(new Dimension(170, 14));
        expBar.setMaximumSize(new Dimension(220, 14));

        add(coinLabel);
        add(Box.createVerticalStrut(6));
        add(levelLabel);
        add(Box.createVerticalStrut(6));
        add(expBar);

        refresh();
    }

    public void bind(AppFrame frame) {
        if (this.frame != null) this.frame.removeAppListener(this);
        this.frame = frame;
        if (this.frame != null) {
            this.frame.addAppListener(this);
            syncFromFrame(this.frame);
        }
    }

    public void unbind() {
        if (this.frame != null) this.frame.removeAppListener(this);
        this.frame = null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (frame == null) return;
        String k = evt.getPropertyName();
        if ("coin".equals(k) || "exp".equals(k) || "stats".equals(k)) {
            syncFromFrame(frame);
            repaint();
        }
    }


    private ImageIcon loadIcon(String path, int size) {
        URL url = getClass().getResource(path);
        if (url == null) return null;
        ImageIcon icon = new ImageIcon(url);
        Image scaled = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public void setCoin(int coin) {
        this.coin = Math.max(0, coin);
        refresh();
    }

    public void setExp(int exp) {
        this.exp = Math.max(0, exp);
        refresh();
    }

    public void syncFromFrame(AppFrame frame) {
        setCoin(frame.getCurrentCoin());
        setExp(frame.getCurrentExp());
    }

    private void refresh() {
        int level = (exp / 1000) + 1;
        int nextTotal = level * 1000;
        int intoLevel = exp - ((level - 1) * 1000);
        int needed = 1000;

        // Coin line
        if (coinIcon != null) {
            coinLabel.setIcon(coinIcon);
            coinLabel.setIconTextGap(6);
            coinLabel.setText(String.valueOf(coin));
        } else {
            coinLabel.setIcon(null);
            coinLabel.setText("Coin: " + coin);
        }

        // Level + EXP text
        levelLabel.setText("Level " + level + "  •  EXP " + exp + "/" + nextTotal);

        // Progress bar for this level (0..1000)
        expBar.setMinimum(0);
        expBar.setMaximum(needed);
        expBar.setValue(Math.max(0, Math.min(needed, intoLevel)));
        expBar.setString(intoLevel + "/" + needed);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // “glass card” background
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(new Color(0, 0, 0, 150)); // translucent black
        g2.fillRoundRect(0, 0, w, h, 18, 18);

        g2.setColor(new Color(255, 255, 255, 60)); // subtle outline
        g2.drawRoundRect(0, 0, w - 1, h - 1, 18, 18);

        g2.dispose();
        super.paintComponent(g);
    }
}
