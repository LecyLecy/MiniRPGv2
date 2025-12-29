package miniRPG.ui.screen;

import miniRPG.auth.AuthService;
import miniRPG.character.Archer;
import miniRPG.character.Mage;
import miniRPG.character.Player;
import miniRPG.character.Warrior;
import miniRPG.data.PlayerClass;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

public class CreateCharacterPanel extends JPanel {

    private PlayerClass selectedClass = null;
    private final AuthService auth;

    public CreateCharacterPanel(AppFrame frame, AuthService auth) {
        this.auth = auth;
        setLayout(new BorderLayout(12, 12));
        setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Choose Your Class");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        add(title, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 3, 12, 12));
        add(cards, BorderLayout.CENTER);

        JButton knightBtn = createClassButton("Warrior", "/images/warrior.png");
        JButton archerBtn = createClassButton("Archer", "/images/archer.png");
        JButton mageBtn   = createClassButton("Mage",   "/images/mage.png");

        cards.add(wrapCard(knightBtn, "HP: 150", "ATK: 20", "DEF: 15"));
        cards.add(wrapCard(archerBtn, "HP: 120", "ATK: 25", "DEF: 10"));
        cards.add(wrapCard(mageBtn,   "HP: 100", "ATK: 30", "DEF: 8"));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton confirm = new JButton("Confirm");
        JButton back = new JButton("Back");

        confirm.setEnabled(false);

        bottom.add(back);
        bottom.add(confirm);
        add(bottom, BorderLayout.SOUTH);

        // Selection behavior
        knightBtn.addActionListener(e -> {
            selectedClass = PlayerClass.WARRIOR; // Knight = Warrior
            highlightSelected(knightBtn, archerBtn, mageBtn);
            confirm.setEnabled(true);
        });

        archerBtn.addActionListener(e -> {
            selectedClass = PlayerClass.ARCHER;
            highlightSelected(archerBtn, knightBtn, mageBtn);
            confirm.setEnabled(true);
        });

        mageBtn.addActionListener(e -> {
            selectedClass = PlayerClass.MAGE;
            highlightSelected(mageBtn, knightBtn, archerBtn);
            confirm.setEnabled(true);
        });

        back.addActionListener(e -> frame.showScreen("login"));

        confirm.addActionListener(e -> {
            if (selectedClass == null) return;

            String username = frame.getCurrentUsername();
            if (username == null || username.trim().isEmpty()) username = "Player";

            // If role already set, prevent changes
            String existing = auth.getRole(username);
            if (existing != null && !existing.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Your role is already set to " + existing + ".\nIt cannot be changed.",
                        "Role Locked",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to choose " + selectedClass + "?\nThis cannot be changed later.",
                    "Confirm Role",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice != JOptionPane.YES_OPTION) return;

            // Write role to CSV (lock it)
            boolean ok = auth.assignRoleOnce(username, selectedClass.name());
            frame.setCurrentCoin(auth.getCoin(username));
            frame.setCurrentExp(auth.getExp(username));
            if (!ok) {
                JOptionPane.showMessageDialog(this,
                        "Failed to save role (maybe already set).",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create actual Player object
            int exp = frame.getCurrentExp();
            Player p;
            switch (selectedClass) {
                case WARRIOR: p = new Warrior(username, exp); break;
                case ARCHER:  p = new Archer(username, exp); break;
                case MAGE:    p = new Mage(username, exp); break;
                default:      p = new Warrior(username, exp);
            }

            frame.setCurrentPlayer(p);
            frame.showScreen("map");
        });

    }

    private JButton createClassButton(String text, String resourcePath) {
        JButton btn = new JButton(text);

        URL url = getClass().getResource(resourcePath);
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image scaled = icon.getImage().getScaledInstance(140, 140, Image.SCALE_FAST);
            btn.setIcon(new ImageIcon(scaled));
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
            btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        } else {
            btn.setToolTipText("Missing asset: " + resourcePath);
        }

        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(200, 220));
        return btn;
    }

    private JPanel wrapCard(JButton button, String l1, String l2, String l3) {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
                new javax.swing.border.EmptyBorder(10, 10, 10, 10)
        ));

        JPanel desc = new JPanel(new GridLayout(0, 1));
        JLabel a = new JLabel(l1, SwingConstants.CENTER);
        JLabel b = new JLabel(l2, SwingConstants.CENTER);
        JLabel c = new JLabel(l3, SwingConstants.CENTER);

        desc.add(a); desc.add(b); desc.add(c);

        p.add(button, BorderLayout.CENTER);
        p.add(desc, BorderLayout.SOUTH);
        return p;
    }

    private void highlightSelected(JButton selected, JButton... others) {
        selected.setBorder(BorderFactory.createLineBorder(new Color(0x2E7D32), 3));
        for (JButton b : others) {
            b.setBorder(UIManager.getBorder("Button.border"));
        }
    }
}
