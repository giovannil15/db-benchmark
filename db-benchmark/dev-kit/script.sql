-- Crea il database 'benchmark' se non esistente
CREATE DATABASE IF NOT EXISTS benchmark;

-- Usa il database 'benchmark' precedentemente creato
USE benchmark;

-- Crea la tabella 'benchmark_table' se non esistente
CREATE TABLE IF NOT EXISTS benchmark_table (
    id INT PRIMARY KEY,
    data VARCHAR(255)
);