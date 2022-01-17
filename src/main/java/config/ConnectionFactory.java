package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private ConnectionFactory() {
    }

    public static Connection getConnection() {

        ConnectionConfig connectionConfig = ConnectionConfig.getInstance();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    connectionConfig.getUrl(),
                    connectionConfig.getUsername(),
                    connectionConfig.getPassword());
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("mysql connection is missing!");
        }
    }

}
