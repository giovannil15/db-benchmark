package it.logon.benchmark.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.logon.benchmark.service.ApplicationProperties;

public class BenchmarkDao {

    private final String tableName = ApplicationProperties.getInstance().getTableName();

    private Connection getConnection() throws SQLException {
        return Dao.getConnection();
    }

    public void closeConnection(Connection connection) throws SQLException {
        Dao.closeConnection(connection);
    }

    public void closeConnection() throws SQLException {
        Dao.closeConnection(Dao.getConnection());
    }

    public void createTable() throws SQLException {
        String operation = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INT PRIMARY KEY, data VARCHAR(255))";
        executeUpdate(operation);
    }

    public void truncateTable() throws SQLException {
        String operation = "TRUNCATE " + tableName;
        executeUpdate(operation);
    }

    public void insert(int id, String data) throws SQLException {
        String operation = "INSERT INTO " + tableName + "(id, data) VALUES (?, ?)";
        executeUpdate(operation, id, data);
    }

    public ResultSet selectDataById(int id) throws SQLException {
        String operation = "SELECT data FROM " + tableName + " WHERE id = ?";
        return executeQuery(operation, id);
    }

    private void executeUpdate(String operation, Object... parameters) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement(operation);
        setParameters(preparedStatement, parameters);
        preparedStatement.executeUpdate();

    }

    private ResultSet executeQuery(String operation, Object... parameters) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement(operation);
        setParameters(preparedStatement, parameters);
        return preparedStatement.executeQuery();
    }

    private PreparedStatement getPreparedStatement(String operation) throws SQLException {
        Connection connection = getConnection();
        return connection.prepareStatement(operation);
    }

    private void setParameters(PreparedStatement preparedStatement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            preparedStatement.setObject(i + 1, parameters[i]);
        }
    }
}
