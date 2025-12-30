package miniRPG.ui.screen;

import miniRPG.auth.AuthService;
import miniRPG.auth.UserRepositoryCsv;
import miniRPG.character.Archer;
import miniRPG.character.Mage;
import miniRPG.character.Player;
import miniRPG.character.Warrior;
import miniRPG.data.PlayerClass;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AppFrame extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel root = new JPanel(cardLayout);
    private PlayerClass selectedClass;
    private final AuthService AuthService;
    private Player currentPlayer;
    private String currentUsername;
    private final MapPanel mapPanel;
    private final ShopPanel shopPanel;
    private final DungeonPanel dungeonPanel;
    private final UpgradePanel upgradePanel;
    private final FadeOverlay fadeOverlay = new FadeOverlay();
    private boolean isTransitioning = false;
    private int currentCoin = 0;
    private JButton exitButton;
    private int currentExp = 0;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public AppFrame() {
        // init backend auth
        UserRepositoryCsv repo = new UserRepositoryCsv("users.csv");
        AuthService = new AuthService(repo);

        setTitle("BloodyRPG");
        setSize(600, 400);
        setMinimumSize(new Dimension(600, 400));
        setMaximumSize(new Dimension(600, 400));
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                requestExit();
            }
        });
        setLocationRelativeTo(null);
        setUndecorated(true);

        // screens
        root.add(new LoginPanel(this, AuthService), "login");
        root.add(new RegisterPanel(this, AuthService), "register");
        root.add(new CreateCharacterPanel(this, AuthService), "createCharacter");

        mapPanel = new MapPanel(this);
        shopPanel = new ShopPanel(this);
        dungeonPanel = new DungeonPanel(this);
        upgradePanel = new UpgradePanel(this);

        root.add(mapPanel, "map");
        root.add(shopPanel, "shop");
        root.add(dungeonPanel, "dungeon");
        root.add(upgradePanel, "upgrade");

        Runtime.getRuntime().addShutdownHook(new Thread(this::persistSessionSilently));

        add(root);
        initExitButton();
        setGlassPane(fadeOverlay);
        fadeOverlay.setVisible(false);
        showScreen("login");
    } // end of constructor

    public void addAppListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }
    public void removeAppListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    private void persistSessionSilently() {
        try {
            if (currentUsername == null || currentUsername.trim().isEmpty()) return;

            AuthService.setCoin(currentUsername, currentCoin);
            AuthService.setExp(currentUsername, currentExp);

        } catch (Exception ignored) {}
    }


    public int getCurrentExp() { return currentExp; }
    public void setCurrentCoin(int c) {
        int old = this.currentCoin;
        this.currentCoin = Math.max(0, c);
        pcs.firePropertyChange("coin", old, this.currentCoin);
    }

    public void setCurrentExp(int e) {
        int old = this.currentExp;
        this.currentExp = Math.max(0, e);
        pcs.firePropertyChange("exp", old, this.currentExp);

        pcs.firePropertyChange("stats", null, null);
    }


    private void requestExit() {
        if (!showExitConfirmDialog()) return;

        persistSessionSilently();
        dispose();
        System.exit(0);
    }

    private boolean showExitConfirmDialog() {
        UIManager.put("OptionPane.background", new Color(245, 245, 245));
        UIManager.put("Panel.background", new Color(245, 245, 245));
        UIManager.put("Button.background", new Color(240, 240, 240));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setOpaque(false);

        JLabel title = new JLabel("Exit BloodyRPG?");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));

        JLabel subtitle = new JLabel("Your gold will be saved.");
        subtitle.setFont(subtitle.getFont().deriveFont(13f));

        panel.add(title);
        panel.add(Box.createVerticalStrut(6));
        panel.add(subtitle);

        Object[] options = {"Exit", "Cancel"};
        int res = JOptionPane.showOptionDialog(
                this,
                panel,
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]
        );

        return res == 0;
    }

    public boolean spendCoin(int amount) {
        if (amount <= 0 || getCurrentCoin() < amount) return false;
        setCurrentCoin(getCurrentCoin() - amount);
        return true;
    }

    public void addExp(int delta) {
        if (delta > 0) setCurrentExp(getCurrentExp() + delta);
    }

    public void addCoin(int delta) {
        if (delta != 0) setCurrentCoin(getCurrentCoin() + delta);
    }



    private void persistCoinSilently() {
        try {
            if (currentUsername == null || currentUsername.trim().isEmpty()) return;
            // Only save coin on exit
            AuthService.setCoin(currentUsername, currentCoin);
            AuthService.setExp(currentUsername, currentExp);
        } catch (Exception ignored) {
            // Don't block exit if saving fails
        }
    }

    private void initExitButton() {
        exitButton = createExitButton();
        getLayeredPane().add(exitButton, JLayeredPane.POPUP_LAYER);

        // reposition when frame size changes (even though you lock size, still safe)
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                positionExitButton();
            }

            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                positionExitButton();
            }
        });

        SwingUtilities.invokeLater(this::positionExitButton);
    }

    private void positionExitButton() {
        if (exitButton == null) return;

        int margin = 10;
        Dimension pref = exitButton.getPreferredSize();

        JLayeredPane lp = getLayeredPane();
        int x = lp.getWidth() - pref.width - margin;
        int y = margin;

        exitButton.setBounds(x, y, pref.width, pref.height);
        exitButton.repaint();
    }

    private JButton createExitButton() {
        JButton btn = new JButton();
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> requestExit());

        java.net.URL url = getClass().getResource("/images/exit.png");
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image scaled = icon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(scaled));

            // icon-only style
            btn.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            btn.setContentAreaFilled(false);
            btn.setOpaque(false);
        } else {
            // fallback text style
            btn.setText("Exit");
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
            ));
            btn.setBackground(new Color(245, 245, 245));
            btn.setOpaque(true);
        }

        return btn;
    }

    public int getCurrentCoin() { return currentCoin; }

    public void transitionTo(String screenName) {
        transitionTo(screenName, null);
    }

    public void transitionTo(String screenName, Runnable beforeShow) {
        if (isTransitioning) return;
        isTransitioning = true;

        fadeOverlay.play(() -> {
            // fully black here
            if (beforeShow != null) beforeShow.run();
            showScreen(screenName);
        }, () -> {
            isTransitioning = false;
        });
    }

    public void showScreen(String name) {
        if ("map".equals(name)) {
            mapPanel.refreshPlayer(this);
        }
        cardLayout.show(root, name);
    }

    public void setSelectedClass(PlayerClass selectedClass) {
        this.selectedClass = selectedClass;
    }

    public PlayerClass getSelectedClass() {
        return selectedClass;
    }

    public void setCurrentUsername(String u) {
        this.currentUsername = u;
    }
    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentPlayer(Player p) {
        this.currentPlayer = p;
    }
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player buildPlayerFromRole(String username, String role) {
        int exp = getCurrentExp();

        if (role == null) role = "";
        switch (role.toUpperCase()) {
            case "WARRIOR":
            case "KNIGHT":
                return new Warrior(username, exp);
            case "ARCHER":
                return new Archer(username, exp);
            case "MAGE":
                return new Mage(username, exp);
            default:
                return new Warrior(username, exp);
        }
    }

    public void openEntrance(String which) {
        transitionTo(which);
    }

    public void openMap() {
        // fade back to map; respawn happens while screen is black
        transitionTo("map", () -> mapPanel.resetSpawn());
    }

    private static class FadeOverlay extends JComponent {
        private float alpha = 0f;                 // 0 = clear, 1 = black
        private Timer timer;
        private int holdTicks = 0;

        private Runnable midAction;
        private Runnable endAction;

        private enum Phase { FADE_OUT, HOLD_BLACK, FADE_IN }
        private Phase phase;

        FadeOverlay() {
            setOpaque(false);
            setFocusable(false);
        }

        void play(Runnable midAction, Runnable endAction) {
            this.midAction = midAction;
            this.endAction = endAction;

            alpha = 0f;
            phase = Phase.FADE_OUT;
            holdTicks = 0;

            setVisible(true);

            if (timer != null && timer.isRunning()) timer.stop();

            // ~60fps
            timer = new Timer(16, e -> tick());
            timer.start();
        }

        private void tick() {
            final float step = 0.08f; // speed; adjust if you want slower/faster

            switch (phase) {
                case FADE_OUT:
                    alpha = Math.min(1f, alpha + step);
                    repaint();
                    if (alpha >= 1f) {
                        // fully black
                        if (midAction != null) midAction.run();
                        phase = Phase.HOLD_BLACK;
                        holdTicks = 8; // ~128ms hold; adjust for longer black pause
                    }
                    break;

                case HOLD_BLACK:
                    repaint();
                    holdTicks--;
                    if (holdTicks <= 0) {
                        phase = Phase.FADE_IN;
                    }
                    break;

                case FADE_IN:
                    alpha = Math.max(0f, alpha - step);
                    repaint();
                    if (alpha <= 0f) {
                        timer.stop();
                        setVisible(false);
                        if (endAction != null) endAction.run();
                    }
                    break;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (alpha <= 0f) return;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(0, 0, 0, Math.round(alpha * 255)));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }


}

