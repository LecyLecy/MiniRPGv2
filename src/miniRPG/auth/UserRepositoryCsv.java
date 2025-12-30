package miniRPG.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryCsv {

    private final Path csvPath;

    public UserRepositoryCsv(String filePath) {
        this.csvPath = Paths.get(filePath);
        ensureFileExists();
    }

    private void ensureRoleCoinExpColumns(java.util.List<String> lines) {
        if (lines.isEmpty()) return;

        String header = lines.get(0).toLowerCase();
        boolean hasRole = header.contains("role");
        boolean hasCoin = header.contains("coin");
        boolean hasExp  = header.contains("exp");

        String newHeader = lines.get(0);

        if (!hasRole) newHeader += ",role";
        if (!hasCoin) newHeader += ",coin";
        if (!hasExp)  newHeader += ",exp";
        lines.set(0, newHeader);

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty()) continue;

            String[] p = line.split(",", -1);
            if (p.length == 2) lines.set(i, line + ",,0,0");
            else if (p.length == 3) lines.set(i, line + ",0,0");
            else if (p.length == 4) lines.set(i, line + ",0");
        }
    }


    private void ensureFileExists() {
        try {
            if (!Files.exists(csvPath)) {
                Files.createFile(csvPath);
                // header
                Files.write(csvPath, java.util.Arrays.asList("username,password,role,coin,exp"), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create users.csv: " + e.getMessage(), e);
        }
    }

    public List<miniRPG.auth.User> findAll() {
        List<miniRPG.auth.User> users = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(csvPath)) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; } // skip header
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", -1);
                if (parts.length < 2) continue;

                // biasa di ganti
                String username = parts[0].trim();
                String password = parts[1].trim();
                String role = (parts.length >= 3) ? parts[2].trim() : "";

                int coin = 0;
                if (parts.length >= 4) {
                    try { coin = Integer.parseInt(parts[3].trim()); } catch (Exception ignored) {}
                }

                int exp = 0;
                if (parts.length >= 5) {
                    try { exp = Integer.parseInt(parts[4].trim()); } catch (Exception ignored) {}
                }

                users.add(new miniRPG.auth.User(username, password, role, coin, exp));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read users.csv: " + e.getMessage(), e);
        }
        return users;
    }

    public boolean existsByUsername(String username) {
        return findAll().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }

    public void save(User user) {
        try {
            // create file if not exists
            if (!Files.exists(csvPath)) {
                Files.createDirectories(csvPath.getParent() == null ? Paths.get(".") : csvPath.getParent());
                Files.createFile(csvPath);
            }

            if (Files.size(csvPath) == 0) {
                Files.write(csvPath,
                        java.util.Collections.singletonList("username,password,role,coin,exp"),
                        StandardOpenOption.APPEND);
            }

            // disallow comma so CSV jadi simple
            if (user.getUsername().contains(",") || user.getPassword().contains(",")) {
                throw new IllegalArgumentException("Username/password cannot contain comma.");
            }

            String role = (user.getRole() == null) ? "" : user.getRole();
            String row = user.getUsername() + "," + user.getPassword() + "," + role + "," + user.getCoin() + "," + user.getExp();

            Files.write(csvPath,
                    java.util.Collections.singletonList(row),
                    StandardOpenOption.APPEND);

        } catch (IOException e) {
            throw new RuntimeException("Failed to write users.csv: " + e.getMessage(), e);
        }
    }

    public Optional<miniRPG.auth.User> findByUsername(String username) {
        return findAll().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    public boolean setRoleIfEmpty(String username, String role) {
        try {
            java.util.List<String> lines = java.nio.file.Files.readAllLines(csvPath);
            if (lines.isEmpty()) return false;
            ensureRoleCoinExpColumns(lines);

            String headerLower = lines.get(0).toLowerCase();
            boolean hasRole = headerLower.contains("role");
            boolean hasCoin = headerLower.contains("coin");

            if (!hasRole && !hasCoin) {
                lines.set(0, lines.get(0) + ",role,coin");
            } else if (hasRole && !hasCoin) {
                lines.set(0, lines.get(0) + ",coin");
            } else if (!hasRole && hasCoin) {
                lines.set(0, lines.get(0) + ",role");
            }
            for (int i = 1; i < lines.size(); i++) {
                if (lines.get(i).trim().isEmpty()) continue;
                String[] p = lines.get(i).split(",", -1);

                if (p.length == 2) {
                    lines.set(i, lines.get(i) + ",,0");
                } else if (p.length == 3) {
                    lines.set(i, lines.get(i) + ",0");
                }
            }

            boolean updated = false;

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", -1);
                if (parts.length < 2) continue;

                String u = parts[0].trim();
                if (!u.equalsIgnoreCase(username)) continue;

                if (parts.length == 2) parts = new String[]{parts[0], parts[1], "", "0"};
                if (parts.length == 3) parts = new String[]{parts[0], parts[1], parts[2], "0"};

                String existingRole = parts[2].trim();
                if (!existingRole.isEmpty()) {
                    return false;
                }

                parts[2] = role;
                lines.set(i, String.join(",", parts));
                updated = true;
                break;
            }

            if (updated) {
                java.nio.file.Files.write(csvPath, lines);
            }
            return updated;

        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to update role in users.csv: " + e.getMessage(), e);
        }
    }

    public int getCoin(String username) {
        return findByUsername(username).map(User::getCoin).orElse(0);
    }

    public boolean setCoin(String username, int newCoin) {
        if (newCoin < 0) newCoin = 0;

        try {
            List<String> lines = Files.readAllLines(csvPath);
            if (lines.isEmpty()) return false;
            ensureRoleCoinExpColumns(lines);

            String headerLower = lines.get(0).toLowerCase();
            if (!headerLower.contains("coin")) {
                lines.set(0, lines.get(0) + ",coin");
                for (int i = 1; i < lines.size(); i++) {
                    if (lines.get(i).trim().isEmpty()) continue;
                    String[] p = lines.get(i).split(",", -1);
                    if (p.length == 2) lines.set(i, lines.get(i) + ",,0");
                    else if (p.length == 3) lines.set(i, lines.get(i) + ",0");
                }
            }

            boolean updated = false;

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", -1);
                if (parts.length == 2) parts = new String[]{parts[0], parts[1], "", "0"};
                if (parts.length == 3) parts = new String[]{parts[0], parts[1], parts[2], "0"};

                if (!parts[0].trim().equalsIgnoreCase(username)) continue;

                parts[3] = String.valueOf(newCoin);
                lines.set(i, String.join(",", parts));
                updated = true;
                break;
            }

            if (updated) Files.write(csvPath, lines);
            return updated;

        } catch (IOException e) {
            throw new RuntimeException("Failed to update coin in users.csv: " + e.getMessage(), e);
        }
    }

    public int getExp(String username) {
        return findByUsername(username).map(User::getExp).orElse(0);
    }

    public boolean setExp(String username, int newExp) {
        if (newExp < 0) newExp = 0;

        try {
            java.util.List<String> lines = java.nio.file.Files.readAllLines(csvPath);
            if (lines.isEmpty()) return false;

            ensureRoleCoinExpColumns(lines);

            boolean updated = false;

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue;

                if (!parts[0].trim().equalsIgnoreCase(username)) continue;

                parts[4] = String.valueOf(newExp); // exp column
                lines.set(i, String.join(",", parts));
                Files.write(csvPath, lines);
                updated = true;
                break;
            }

            if (updated) java.nio.file.Files.write(csvPath, lines);
            return updated;

        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to update exp in users.csv: " + e.getMessage(), e);
        }
    }

}

