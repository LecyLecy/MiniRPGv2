package miniRPG.ui.screen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ShopPanel extends JPanel {

    private final AppFrame frame;

    private Image bgRaw;
    private Image bgScaled;
    private int bgW = -1, bgH = -1;

    private JLabel coinLabel;
    private ImageIcon coinIcon;

    public ShopPanel(AppFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        bgRaw = loadImageRaw("/images/shop.png");

        // coin UI
        coinIcon = loadCoinIcon(18); // null if not found
        coinLabel = new JLabel();
        coinLabel.setOpaque(false);
        coinLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        coinLabel.setFont(getFont().deriveFont(Font.BOLD, 14f));
        coinLabel.setForeground(new Color(255, 215, 0)); // gold/yellow
        refreshCoinText();

        // back button
        JButton back = new JButton("Go Back");
        back.addActionListener(e -> frame.openMap());

        // bottom-right stack: coin above, back below
        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);

        JPanel rightStack = new JPanel();
        rightStack.setOpaque(false);
        rightStack.setLayout(new BoxLayout(rightStack, BoxLayout.Y_AXIS));

        coinLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        back.setAlignmentX(Component.RIGHT_ALIGNMENT);

        rightStack.add(coinLabel);
        rightStack.add(Box.createVerticalStrut(6));
        rightStack.add(back);

        south.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
        south.add(rightStack, BorderLayout.EAST);

        add(south, BorderLayout.SOUTH);

        // update coin whenever panel becomes visible
        addHierarchyListener(e -> {
            if (isShowing()) refreshCoinText();
        });
    }

    private void refreshCoinText() {
        int c = frame.getCurrentCoin();
        if (coinIcon != null) {
            coinLabel.setIcon(coinIcon);
            coinLabel.setText(String.valueOf(c));
            coinLabel.setIconTextGap(6);
        } else {
            coinLabel.setIcon(null);
            coinLabel.setText("Coin: " + c);
        }
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

    private ImageIcon loadCoinIcon(int size) {
        URL url = getClass().getResource("/images/coin.png");
        if (url == null) return null;
        ImageIcon icon = new ImageIcon(url);
        Image scaled = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
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
