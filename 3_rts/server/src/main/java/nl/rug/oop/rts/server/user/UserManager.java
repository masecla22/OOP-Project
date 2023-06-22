package nl.rug.oop.rts.server.user;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import nl.rug.oop.rts.protocol.user.User;

/**
 * This class manages all the users in the game.
 */
@RequiredArgsConstructor
public class UserManager {
    @NonNull
    private Connection connection;

    private Map<UUID, User> activeTokens = new HashMap<>();

    /**
     * Initialize the user manager. This will create the table if it doesn't
     * exist yet.
     * 
     * @throws SQLException - If an error occurs while creating the table
     */
    public void initialize() throws SQLException {
        ensureTable();
    }

    private void ensureTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS `rts_users` (" +
                "`id` INT NOT NULL AUTO_INCREMENT," +
                "`username` TEXT NOT NULL," +
                "`password` TEXT NOT NULL," +
                "`salt` TEXT NOT NULL," +
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
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return set.getInt(1) > 0;
            } else {
                return false;
            }
        }
    }

    /**
     * Creates a user with the given username and password. The password will be
     * hashed before it is stored in the database.
     * 
     * @param username - The username
     * @param password - The password
     * @return - The created user
     * @throws SQLException - If an error occurs while creating the user
     */
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

        if (!checkIfAllowed(username)) {
            throw new IllegalArgumentException("Username contains illegal characters");
        }

        if (!checkIfAllowed(password)) {
            throw new IllegalArgumentException("Password contains illegal characters");
        }

        if (userExists(username)) {
            throw new IllegalArgumentException("User already exists with that username");
        }

        String salt = getSalt();
        String hashedPassword = hashPassword(password, salt);

        String query = "INSERT INTO `rts_users` (`username`, `password`, `salt`) VALUES (?, ?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.setString(3, salt);
            statement.execute();
        }

        return getUser(username);
    }

    /**
     * Sets the ELO of the given user.
     * 
     * @param user - The user
     * @param elo  - The ELO
     * @throws SQLException - If an error occurs while setting the ELO
     */
    public void setElo(User user, int elo) throws SQLException {
        String query = "UPDATE `rts_users` SET `elo` = ? WHERE `id` = ?;";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, elo);
            statement.setInt(2, user.getId());
            statement.execute();
        }
    }

    /**
     * Gets the user with the given username.
     * 
     * @param username - The username
     * @return - The user
     * @throws SQLException - If an error occurs while getting the user
     */
    public User getUser(String username) throws SQLException {
        String query = "SELECT * FROM `rts_users` WHERE `username` = ?;";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return new User(set);
            } else {
                return null;
            }
        }
    }

    /**
     * Gets the user with the given session ID.
     * 
     * @param token - The session ID
     * @return - The user
     */
    public User getUser(UUID token) {
        return activeTokens.get(token);
    }

    /**
     * Logs out the user with the given session ID.
     * 
     * @param token - The session ID
     */
    public void logout(UUID token) {
        activeTokens.remove(token);
    }

    /**
     * Logs in the given user and returns a session ID.
     * 
     * @param user - The user
     * @return - The session ID
     */
    public UUID login(User user) {
        UUID token = UUID.randomUUID();
        activeTokens.put(token, user);
        return token;
    }

    /**
     * Checks the given password against the stored password for the given user.
     * and returns a session ID if the password is correct.
     * 
     * @param username - The username
     * @param password - The password
     * 
     * @return - The session ID, or null if the password is incorrect
     */
    public UUID login(String username, String password) throws SQLException {
        if (username == null || password == null) {
            return null;
        }

        // Fetch the salt of the user first
        String query = "SELECT `salt` FROM `rts_users` WHERE `username` = ?;";
        String salt = null;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                salt = set.getString(1);
            }
        }

        if (salt == null) {
            return null;
        }

        // Hash the password with the salt
        String hashedPassword = hashPassword(password, salt);

        query = "SELECT * FROM `rts_users` WHERE `username` = ? AND `password` = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, hashedPassword);

            ResultSet set = statement.executeQuery();

            if (set.next()) {
                User user = new User(set);
                return login(user);
            } else {
                return null;
            }
        }
    }

    private static String bytesToHex(byte[] hash) {
        // Source: https://www.baeldung.com/sha-256-hashing-java
        // Needed to convert SHA-256 hash to string
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @SneakyThrows(NoSuchAlgorithmException.class) // I swear SHA-256 exists
    private String hashPassword(String password, String salt) {
        if (password == null) {
            return null;
        }

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] saltBytes = Base64.getDecoder().decode(salt);
        digest.update(saltBytes);

        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        String hashResult = bytesToHex(hash);

        return hashResult;
    }

    private boolean checkIfAllowed(String value) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(value);

        return !matcher.find();
    }

    private String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        String base64 = Base64.getEncoder().encodeToString(salt);
        return base64;
    }

    private double calculateEloProbability(double rating1, double rating2) {
        return 1.0 / (1.0 + Math.pow(10, (rating2 - rating1) / 400.0));
    }

    /**
     * Updates the ELO of the given users based on the result of a game.
     * 
     * @param winner - The winner
     * @param looser - The looser
     */
    public void updateRatings(User winner, User looser) {
        int eloConstant = 32;

        double winnerRating = winner.getElo();
        double looserRating = looser.getElo();

        double winnerProbability = calculateEloProbability(winnerRating, looserRating);
        double looserProbability = calculateEloProbability(looserRating, winnerRating);

        double winnerNewRating = winnerRating + eloConstant * (1 - winnerProbability);
        double looserNewRating = looserRating + eloConstant * (0 - looserProbability);

        winner.setElo((int) winnerNewRating);
        looser.setElo((int) looserNewRating);

        try {
            this.setElo(winner, (int) winnerNewRating);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            this.setElo(looser, (int) looserNewRating);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the leaderboard.
     * 
     * @return - The leaderboard
     * @throws SQLException - If an error occurs while getting the leaderboard
     */
    public List<Entry<String, Integer>> getLeaderboard() throws SQLException {
        String query = "SELECT * FROM `rts_users` ORDER BY `elo` DESC LIMIT 10;";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet set = statement.executeQuery();
            List<Entry<String, Integer>> leaderboard = new ArrayList<>();
            while (set.next()) {
                leaderboard.add(new SimpleEntry<>(set.getString("username"), set.getInt("elo")));
            }
            return leaderboard;
        }
    }

}
