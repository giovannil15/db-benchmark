package it.logon.benchmark.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository
public class BenchmarkDao {

    private final JdbcTemplate jdbcTemplate;

    @Value("${benchmark.tableName}")
    private String tableName;

    @Autowired
    public BenchmarkDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTable() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS " + tableName + " (id INT PRIMARY KEY, data VARCHAR(255))");
    }

    public void truncateTable() {
        jdbcTemplate.execute("TRUNCATE " + tableName);
    }

    public void insert(int id, String data) throws SQLException {
        jdbcTemplate.update("INSERT INTO " + tableName + "(id, data) VALUES (?, ?)", id, data);
    }

    public String selectDataById(int id) {
        return jdbcTemplate.queryForObject("SELECT data FROM " + tableName + " WHERE id = ?", String.class, id);
    }

}
