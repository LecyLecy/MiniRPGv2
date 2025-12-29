package miniRPG.auth;

public class User {
    private final String username;
    private final String password;
    private final String role;
    private final int coin;
    private final int exp;   // NEW

    public User(String username, String password, String role, int coin, int exp) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.coin = coin;
        this.exp = exp;
    }

    public User(String username, String password, String role) {
        this(username, password, role, 0, 0);
    }

    public User(String username, String password, String role, int coin) {
        this(username, password, role, coin, 0);
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public int getCoin() { return coin; }
    public int getExp() { return exp; }
}
