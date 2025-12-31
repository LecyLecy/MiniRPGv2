package miniRPG.session;

public final class GameSession {

    private final String username;
    private final String role;
    private final int coin;
    private final int exp;

    public GameSession(String username, String role, int coin, int exp) {
        this.username = username == null ? "" : username;
        this.role = role == null ? "" : role;
        this.coin = Math.max(0, coin);
        this.exp = Math.max(0, exp);
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public int getCoin() {
        return coin;
    }

    public int getExp() {
        return exp;
    }
}
