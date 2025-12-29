package miniRPG.auth;

public class AuthService {

    private final miniRPG.auth.UserRepositoryCsv repo;

    public AuthService(miniRPG.auth.UserRepositoryCsv repo) {
        this.repo = repo;
    }

    public int getExp(String username) {
        return repo.getExp(username);
    }

    public boolean setExp(String username, int newExp) {
        return repo.setExp(username, newExp);
    }

    public boolean login(String username, String password) {
        if (isBlank(username) || isBlank(password)) return false;

        return repo.findByUsername(username)
                .map(u -> u.getPassword().equals(password))
                .orElse(false);
    }

    public RegisterResult register(String username, String password, String confirmPassword) {
        if (isBlank(username) || isBlank(password) || isBlank(confirmPassword)) {
            return RegisterResult.EMPTY_FIELDS;
        }
        if (!password.equals(confirmPassword)) {
            return RegisterResult.PASSWORD_MISMATCH;
        }
        if (repo.existsByUsername(username)) {
            return RegisterResult.USERNAME_TAKEN;
        }

        repo.save(new miniRPG.auth.User(username, password, "", 0, 0));
        return RegisterResult.SUCCESS;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public enum RegisterResult {
        SUCCESS,
        EMPTY_FIELDS,
        PASSWORD_MISMATCH,
        USERNAME_TAKEN
    }

    public boolean assignRoleOnce(String username, String role) {
        return repo.setRoleIfEmpty(username, role);
    }

    public String getRole(String username) {
        return repo.findByUsername(username)
                .map(User::getRole)
                .orElse("");
    }

    public int getCoin(String username) {
        return repo.getCoin(username);
    }

    public boolean setCoin(String username, int newCoin) {
        return repo.setCoin(username, newCoin);
    }

}

