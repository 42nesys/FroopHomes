package insolventer.froopHomes.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


public class MySqlUtil {

    private Connection connection;

    public MySqlUtil(String host, String port, String database, String username, String password) {
        if (!isConnected()) {
            try {
                String login = "jdbc:mysql://" + host + ":" + port + "/" + database;
                Properties connectionProperties = new Properties();

                connectionProperties.put("user", username);
                connectionProperties.put("password", password);
                connectionProperties.put("autoReconnect", "true");

                connection = DriverManager.getConnection(login, connectionProperties);
            } catch (SQLException exception) {
                // Slay
            }
        }
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void update(String query) {
        if (isConnected()) {
            try {
                connection.createStatement().executeUpdate(query);
            } catch (SQLException e) {
                // Slay
            }
        }
    }

    public ResultSet getResult(String query) {
        if (isConnected()) {
            try {
                return connection.createStatement().executeQuery(query);
            } catch (SQLException e) {
                // Slay
            }
        }
        return null;
    }


    public String get(String database, String value, String where) {
        if (isConnected()) {
            String query = "SELECT " + value + " FROM " + database +
                    (where == null || where.equalsIgnoreCase("") ? "" : " WHERE " + where);

            ResultSet resultSet = getResult(query);

            if (resultSet != null) {
                try {
                    if (resultSet.next()) {
                        return resultSet.getString(value);
                    }
                } catch (SQLException e) {
                    // Slay
                }
            }
        }
        return "";
    }
}