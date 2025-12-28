package miniRPG.auth;

public class User {
    private final String username;
    private final String password; // plain text
    private final String role;     // "" if not set
    private final int coin;        // NEW

    public User(String username, String password, String role, int coin) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.coin = coin;
    }

    // Backward-compatible constructor (old calls)
    public User(String username, String password, String role) {
        this(username, password, role, 0);
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public int getCoin() { return coin; }
}
