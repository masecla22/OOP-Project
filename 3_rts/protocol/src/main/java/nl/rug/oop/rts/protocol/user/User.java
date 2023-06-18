package nl.rug.oop.rts.protocol.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A user for the game.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password") // To prevent logging the password
public class User {
    private int id;

    private String name;
    private String password;
    private int elo;

    /**
     * Create a new user from a result set.
     * 
     * @param set - the result set
     * @throws SQLException - if the result set is invalid
     */
    public User(ResultSet set) throws SQLException {
        this.id = set.getInt("id");
        this.name = set.getString("username");
        this.password = set.getString("password");
        this.elo = set.getInt("elo");
    }
}
