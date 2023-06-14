package nl.rug.oop.rts.protocol.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;

    private String name;
    private String password;
    private int elo;

    public User(ResultSet set) throws SQLException {
        this.id = set.getInt("id");
        this.name = set.getString("name");
        this.password = set.getString("password");
        this.elo = set.getInt("elo");
    }
}
