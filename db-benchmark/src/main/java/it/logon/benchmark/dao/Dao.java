package it.logon.benchmark.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.logon.benchmark.service.ApplicationProperties;

public class Dao {

    private Dao() {
        // empty constructor
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Dao.class);
    private static String driver = ApplicationProperties.getInstance().getDriver();
    private static String url = ApplicationProperties.getInstance().getUrl();
    private static String username = ApplicationProperties.getInstance().getUsername();
    private static String password = ApplicationProperties.getInstance().getPassword();

    public static Connection getConnection() throws SQLException {

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            LOGGER.error("Databse driver not found [{}]", ex.getMessage());
            throw new SQLException("Database driver not found", ex);
        }
        return DriverManager.getConnection(url, username, password);
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                LOGGER.error("Unable to close connection [{}]", ex.getMessage());
                throw new SQLException("Unable to close connection");
            }
        }
    }
}
