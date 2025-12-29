package miniRPG.ui.screen;

import miniRPG.auth.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    public LoginPanel(AppFrame frame, AuthService auth) {
        setLayout(new GridLayout(0, 1, 8, 8));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        JButton toRegisterBtn = new JButton("Create an Account");

        add(new JLabel("Username"));
        add(usernameField);
        add(new JLabel("Password"));
        add(passwordField);
        add(loginBtn);
        add(toRegisterBtn);

        loginBtn.addActionListener(e -> {
            String u = usernameField.getText();
            String p = new String(passwordField.getPassword());

            boolean ok = auth.login(u, p);
            if (ok) {
                System.out.println("DEBUG role for " + u + " = [" + auth.getRole(u) + "]");
                frame.showScreen("createCharacter");
                frame.setCurrentUsername(u);
                frame.setCurrentCoin(auth.getCoin(u));
                frame.setCurrentExp(auth.getExp(u));
                String role = auth.getRole(u);
                if (role != null && !role.isEmpty()) {
                    // role already chosen -> create player + go map
                    frame.setCurrentPlayer(frame.buildPlayerFromRole(u, role)); // we’ll add helper
                    frame.showScreen("map");
                } else {
                    // no role -> choose role
                    frame.showScreen("createCharacter");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username/password.");
            }
        });

        toRegisterBtn.addActionListener(e -> frame.showScreen("register"));
    }
}

