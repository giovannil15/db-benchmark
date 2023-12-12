package it.logon.benchmark.utility;

import java.util.concurrent.TimeUnit;

public class DbBenchmarkUtility {

    private DbBenchmarkUtility() {
        throw new IllegalStateException("Utility class");
    }

    public static void printBenchmarkResults(String statement, long totalExecutionTime, long minExecutionTime,
            long maxExecutionTime, int numberOfStatements) {

        double avgExecutionTime = (double) totalExecutionTime / numberOfStatements;

        System.out.println("\n" + statement + " Benchmark Results:");
        System.out.println("|                 |     Value [ms]   |    Value [ns]    |");
        System.out.println("|-----------------|------------------|------------------|");
        System.out.printf("| %15s | %16d | %16d |\n",
                "Total Time",
                totalExecutionTime, TimeUnit.MILLISECONDS.toNanos(totalExecutionTime));
        System.out.printf("| %15s | %16d | %16d |\n",
                "Min Time",
                TimeUnit.NANOSECONDS.toMillis(minExecutionTime), minExecutionTime);
        System.out.printf("| %15s | %16d | %16d |\n",
                "Max Time",
                TimeUnit.NANOSECONDS.toMillis(maxExecutionTime), maxExecutionTime);
        System.out.printf("| %15s | %16f | %16f |\n",
                "Average Time",
                avgExecutionTime, avgExecutionTime * 1_000_000);
    }

    public static long calculateDurationInMillis(long start, long end) {
        return TimeUnit.NANOSECONDS.toMillis(end - start);
    }

    public static long calculateDurationInNanoSec(long start, long end) {
        return end - start;
    }
}
