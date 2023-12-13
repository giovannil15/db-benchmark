DATABASE BENCHMARK
L'applicazione ha lo scopo di calcolare le performance di inserimenti e letture su un Database MySQL.

Installazione e Prerequisiti
Prima di avviare l'applicazione sarà necessario configurare la connessione a Database e i parametri di benchmark 
presenti nell'application.properties (src\main\resources\application.properties)

All'interno della cartella dev-kit è presente un semplice script sql da importare sul proprio dbms per la creazione 
del database 'benchmark' e della tabella 'benchmark_table'. 

Configurazione
Il programma permette una minima customizzazione secondo quanto configurato a livello di application.properties:

datasource.driver=com.mysql.cj.jdbc.Driver              --> Driver MySQL JDBC per la connessione al database
datasource.url=jdbc:mysql://localhost:3306/benchmark    --> URL (impostare url del proprio dbms. 'benchmark' è il nome del database)
datasource.username=username                            --> Username per la connessione al database
datasource.password=password                            --> Password per la connessione al database

benchmark.tableName=benchmark_table                     --> Nome della tabella
benchmark.numRows=100                                   --> Numero di righe che verranno inserite in tabella                                      
benchmark.commitRate=5                                  --> Frequenza con cui verrà eseguita la commit

Avvio
Runnare il metodo main della classe DbBenchmarkApplication.
Verrà creata la tabella - se non già esistente - verrà svuotata, e verranno eseguite le operazioni di INSERT e SELECT
con relativo calcolo delle prestazioni.

!!! WARNING !!!
ATTENZIONE: Ad ogni avvio si è previsto di svuotare la tabella con un comando TRUNCATE.

Output atteso
Se correttamente configurata, l'applicazione mostrerà a console i risultati del benchmark per quanto riguarda le operazioni di 
INSERT e SELECT, riportando quanto segue:
Total Time: durata totale dell'esecuzione delle operazioni
Min Time: durata minima di una singola operazione
Max Time: durata massima di una singola operazione
Average Time: tempo medio dell'operazione calcolato dividendo la durata totale per il numero di righe configurate.

I dati saranno mostrati sia in millisecondi che in nanosecondi.

Di seguito un esempio di output:

INSERT Benchmark Results:
|                 |     Value [ms]   |    Value [ns]    |
|-----------------|------------------|------------------|
|      Total Time |              130 |        130000000 |
|        Min Time |                0 |           397800 |
|        Max Time |               20 |         20109000 |
|    Average Time |         1,300000 |   1300000,000000 |

SELECT Benchmark Results:
|                 |     Value [ms]   |    Value [ns]    |
|-----------------|------------------|------------------|
|      Total Time |             2659 |       2659000000 |
|        Min Time |               20 |         20171899 |
|        Max Time |               70 |         70443501 |
|    Average Time |        26,590000 |  26590000,000000 |


Dettagli tecnici:

- DbBenchmarkApplication: La classe main svolge il compito di orchestrare le diverse operazioni a Database: creazione, inserimento e lettura.
Le prestazioni di inserimento e lettura sono calcolate facendo la differenza tra la fine dell'operazione e l'inizio.

- Dao: Si preoccupa di creare la connessione al database.

- BenchmarkDao: Data Access Object utilizzato per "nascondere" le prepared statement utilizzate.

- DbBenchmarkUtility: Classe di utility usata per il calcolo della durata delle operazioni e per la stampa dei risultati.

- ApplicationProperties: Service con l'owner di leggere e caricare in memoria i dati di configurazione presenti su application.properties.

- PropertiesLoadException: Esempio di eccezione custom per gestire errori in fase di lettura/caricamento delle properties.