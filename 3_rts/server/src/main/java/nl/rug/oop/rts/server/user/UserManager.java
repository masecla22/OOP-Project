package nl.rug.oop.rts.server.user;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import nl.rug.oop.rts.protocol.user.User;

@RequiredArgsConstructor
public class UserManager {
    @NonNull
    private Connection connection;

    private Map<UUID, User> activeTokens = new HashMap<>();

    public void initialize() throws SQLException {
        ensureTable();
    }

    private void ensureTable() throws SQLException {
        String query = "CREATE TABLE `rts_users` (" +
                "`id` INT NOT NULL AUTO_INCREMENT," +
                "`username` TEXT NOT NULL," +
                "`password` TEXT NOT NULL," +
                "`elo` INT NOT NULL DEFAULT 1000," +
                "PRIMARY KEY (`id`)" +
                ");";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.execute();
        }
    }

    private boolean userExists(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM `rts_users` WHERE `username` = ?;";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            return statement.executeQuery().getInt(1) > 0;
        }
    }

    public User createUser(String username, String password) throws SQLException {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password cannot be null");
        }

        if (username.length() < 3 || username.length() > 16) {
            throw new IllegalArgumentException("Username must be between 3 and 16 characters");
        }

        if (password.length() < 8 || password.length() > 32) {
            throw new IllegalArgumentException("Password must be between 8 and 32 characters");
        }

        if (userExists(username)) {
            throw new IllegalArgumentException("User already exists with that username");
        }

        String hashedPassword = hashPassword(password);

        String query = "INSERT INTO `rts_users` (`username`, `password`) VALUES (?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.execute();
        }

        return getUser(username);
    }

    public void setElo(User user, int elo) throws SQLException {
        String query = "UPDATE `rts_users` SET `elo` = ? WHERE `id` = ?;";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, elo);
            statement.setInt(2, user.getId());
            statement.execute();
        }
    }

    public User getUser(String username) throws SQLException {
        String query = "SELECT * FROM `rts_users` WHERE `username` = ?;";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet set = statement.executeQuery();
            if (set.first()) {
                return new User(set);
            } else {
                return null;
            }
        }
    }

    public User getUser(UUID token) {
        return activeTokens.get(token);
    }

    public void logout(UUID token) {
        activeTokens.remove(token);
    }

    public UUID login(User user) {
        UUID token = UUID.randomUUID();
        activeTokens.put(token, user);
        return token;
    }

    public UUID login(String username, String password) throws SQLException {
        String query = "SELECT * FROM `rts_users` WHERE `username` = ? AND `password` = ?;";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, hashPassword(password));

            ResultSet set = statement.executeQuery();

            if (set.first()) {
                User user = new User(set);
                return login(user);
            } else {
                return null;
            }
        }
    }

    @SneakyThrows(NoSuchAlgorithmException.class) // I swear SHA-256 exists
    private String hashPassword(String password) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return new String(hash, StandardCharsets.UTF_8);
    }

}
