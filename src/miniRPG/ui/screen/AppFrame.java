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

    public AppFrame() {
        // init backend auth
        UserRepositoryCsv repo = new UserRepositoryCsv("users.csv");
        AuthService = new AuthService(repo);

        setTitle("BloodyRPG");
        setSize(600, 400);
        setMinimumSize(new Dimension(600, 400));
        setMaximumSize(new Dimension(600, 400));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        add(root);
        setGlassPane(fadeOverlay);
        fadeOverlay.setVisible(false);
        showScreen("login");
    }

    public int getCurrentCoin() { return currentCoin; }

    public void setCurrentCoin(int c) { currentCoin = Math.max(0, c); }

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
        if (role == null) role = "";
        switch (role.toUpperCase()) {
            case "WARRIOR":
            case "KNIGHT":
                return new Warrior(username);
            case "ARCHER":
                return new Archer(username);
            case "MAGE":
                return new Mage(username);
            default:
                return new Warrior(username);
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

