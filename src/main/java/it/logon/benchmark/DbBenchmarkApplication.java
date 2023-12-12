package it.logon.benchmark;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import it.logon.benchmark.dao.BenchmarkDao;
import it.logon.benchmark.utility.DbBenchmarkUtility;

@SpringBootApplication
@ComponentScan(basePackages = "it.logon.benchmark.configuration")
public class DbBenchmarkApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbBenchmarkApplication.class);

    @Autowired
    private BenchmarkDao benchmarkDao;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${benchmark.numRows}")
    private int numRows;

    @Value("${benchmark.commitRate}")
    private int commitRate;

    public static void main(String[] args) {
        SpringApplication.run(DbBenchmarkApplication.class, args);
    }

    @Override
    public void run(String... args) {

        try {
            benchmarkDao.createTable();
            benchmarkDao.truncateTable();
            // Usa TransactionTemplate per eseguire il benchmark in una
            // transazione. Necessario per poter forzare auto-commit a false.
            transactionTemplate.execute(status -> {
                try {
                    executeInsertBenchmark();
                } catch (SQLException ex) {
                    LOGGER.error("Error during insert [{}]", ex.getMessage());
                }
                return null;
            });
            executeSelectBenchmark();
        } catch (Exception ex) {
            LOGGER.error("Error during benchmark statement [{}]", ex.getMessage());
            throw ex;
        }

    }

    private void executeInsertBenchmark() throws SQLException {
        long startTime = System.nanoTime();
        long minExecutionTime = Long.MAX_VALUE;
        long maxExecutionTime = Long.MIN_VALUE;

        Connection connection = jdbcTemplate.getDataSource().getConnection();
        connection.setAutoCommit(false); // Imposta l'autocommit a false

        for (int i = 1; i <= numRows; i++) {
            long startExecutionTime = System.nanoTime();
            benchmarkDao.insert(i, "Riga" + i);
            long endExecutionTime = System.nanoTime();
            long executionTime = DbBenchmarkUtility.calculateDurationInNanoSec(startExecutionTime, endExecutionTime);
            minExecutionTime = Math.min(minExecutionTime, executionTime);
            maxExecutionTime = Math.max(maxExecutionTime, executionTime);
            if (i % commitRate == 0) {
                connection.commit(); // Commit ogni commitRate operazioni
            }
        }

        // Commit per i restanti dati da inserire
        connection.commit();

        // Ripristina l'autocommit a true
        connection.setAutoCommit(true);

        long endTime = System.nanoTime();
        long totalExecutionTime = DbBenchmarkUtility.calculateDurationInMillis(startTime, endTime);
        DbBenchmarkUtility.printBenchmarkResults("INSERT", totalExecutionTime, minExecutionTime, maxExecutionTime,
                numRows);
    }

    private void executeSelectBenchmark() {
        long startTime = System.nanoTime();
        long minExecutionTime = Long.MAX_VALUE;
        long maxExecutionTime = Long.MIN_VALUE;

        for (int i = 1; i <= numRows; i++) {
            long startExecutionTime = System.nanoTime();
            benchmarkDao.selectDataById(i);
            long endExecutionTime = System.nanoTime();
            long executionTime = DbBenchmarkUtility.calculateDurationInNanoSec(startExecutionTime, endExecutionTime);
            minExecutionTime = Math.min(minExecutionTime, executionTime);
            maxExecutionTime = Math.max(maxExecutionTime, executionTime);
        }

        long endTime = System.nanoTime();
        long totalExecutionTime = DbBenchmarkUtility.calculateDurationInMillis(startTime, endTime);
        DbBenchmarkUtility.printBenchmarkResults("SELECT", totalExecutionTime, minExecutionTime, maxExecutionTime,
                numRows);
    }

}
