package miniRPG.ui.screen;


import miniRPG.auth.AuthService;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {

    public RegisterPanel(AppFrame frame, AuthService auth) {
        setLayout(new GridLayout(0, 1, 8, 8));

        JTextField usernameField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JPasswordField confirmField = new JPasswordField();

        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back to Login");

        add(new JLabel("Username"));
        add(usernameField);
        add(new JLabel("Password"));
        add(passField);
        add(new JLabel("Confirm Password"));
        add(confirmField);
        add(registerBtn);
        add(backBtn);

        registerBtn.addActionListener(e -> {
            String u = usernameField.getText();
            String p = new String(passField.getPassword());
            String c = new String(confirmField.getPassword());

            AuthService.RegisterResult res = auth.register(u, p, c);
            if (res == AuthService.RegisterResult.SUCCESS) {
                JOptionPane.showMessageDialog(this, "Registered! Please login.");
                frame.showScreen("login");
            }

            switch (res) {
                case SUCCESS:
                    JOptionPane.showMessageDialog(this, "Registered! Please login.");
                    frame.showScreen("login");
                    break;
                case EMPTY_FIELDS:
                    JOptionPane.showMessageDialog(this, "Fill all fields.");
                    break;
                case PASSWORD_MISMATCH:
                    JOptionPane.showMessageDialog(this, "Password mismatch.");
                    break;
                case USERNAME_TAKEN:
                    JOptionPane.showMessageDialog(this, "Username already taken.");
                    break;
            }
        });

        backBtn.addActionListener(e -> frame.showScreen("login"));
    }
}

