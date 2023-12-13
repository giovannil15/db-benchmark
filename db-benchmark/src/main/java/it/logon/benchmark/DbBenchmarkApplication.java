package it.logon.benchmark;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.logon.benchmark.dao.BenchmarkDao;
import it.logon.benchmark.dao.Dao;
import it.logon.benchmark.service.ApplicationProperties;
import it.logon.benchmark.utility.DbBenchmarkUtility;

public class DbBenchmarkApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbBenchmarkApplication.class);
    private static int numRows = ApplicationProperties.getInstance().getNumRows();
    private static int commitRate = ApplicationProperties.getInstance().getCommitRate();
    private static BenchmarkDao benchmarkDao = new BenchmarkDao();

    public static void main(String[] args) throws Exception {

        try {

            benchmarkDao.createTable();
            benchmarkDao.truncateTable();
            executeInsertBenchmark();
            executeSelectBenchmark();

        } catch (Exception ex) {
            LOGGER.error("Error during main method execution due to [{}]", ex.getMessage());
            throw ex;
        } finally {
            benchmarkDao.closeConnection();
        }
    }

    private static void executeInsertBenchmark() throws SQLException {
        long startTime = System.nanoTime();
        long minExecutionTime = Long.MAX_VALUE;
        long maxExecutionTime = Long.MIN_VALUE;

        Connection connection = Dao.getConnection();
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

    private static void executeSelectBenchmark() throws SQLException {
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
