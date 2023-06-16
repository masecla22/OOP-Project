package nl.rug.oop.rts.protocol.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password") // To prevent logging the password
public class User {
    private int id;

    private String name;
    private String password;
    private int elo;

    public User(ResultSet set) throws SQLException {
        this.id = set.getInt("id");
        this.name = set.getString("username");
        this.password = set.getString("password");
        this.elo = set.getInt("elo");
    }
}
