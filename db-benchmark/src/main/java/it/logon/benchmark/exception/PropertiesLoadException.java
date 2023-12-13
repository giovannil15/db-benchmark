package it.logon.benchmark.exception;

public class PropertiesLoadException extends RuntimeException {

    public PropertiesLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertiesLoadException(String message) {
        super(message);
    }
}
