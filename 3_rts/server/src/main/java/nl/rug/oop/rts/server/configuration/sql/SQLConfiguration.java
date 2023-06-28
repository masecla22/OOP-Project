package nl.rug.oop.rts.server.configuration.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class represents the SQL configuration.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SQLConfiguration {
    private String host = "localhost";
    private int port = 3306;

    private String username = "root";
    private String password = "";

    private String database = "rts";

    @Getter(AccessLevel.NONE)
    private transient Connection connection;

    private transient Timer sqlAliver = new Timer();

    /**
     * Open a connection to the database.
     * 
     * @return - the connection
     * @throws ClassNotFoundException - if the SQL driver could not be found
     * @throws SQLException           - if the connection could not be opened
     */
    public Connection openConnection() throws ClassNotFoundException, SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }

        // Cancel any previous tasks
        sqlAliver.cancel();
        sqlAliver = new Timer();

        // Load SQL driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = String.format("jdbc:mysql://%s:%d/", host, port);
        Connection connection = DriverManager.getConnection(url, username, password);

        // Ensure database exsists
        connection.prepareStatement("CREATE DATABASE IF NOT EXISTS " + database).execute();

        // Select database
        connection.prepareStatement("USE " + database).execute();

        sqlAliver.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    connection.prepareStatement("SELECT 1").execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);

        return connection;
    }

    /**
     * Close the connection to the database.
     * 
     * @throws SQLException - if the connection could not be closed
     */
    public void closeConnection() throws SQLException {
        this.sqlAliver.cancel();
        this.connection.close();
    }

}
