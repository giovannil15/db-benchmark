package it.logon.benchmark.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import it.logon.benchmark.exception.PropertiesLoadException;

public class ApplicationProperties {

    private static final String FILE_PATH = "application.properties";
    private static final ApplicationProperties INSTANCE = new ApplicationProperties();

    private final Properties properties;

    private ApplicationProperties() {
        this.properties = loadProperties(FILE_PATH);
    }

    public static ApplicationProperties getInstance() {
        return INSTANCE;
    }

    public String getDriver() {
        return getProperty("datasource.driver");
    }

    public String getUrl() {
        return getProperty("datasource.url");
    }

    public String getUsername() {
        return getProperty("datasource.username");
    }

    public String getPassword() {
        return getProperty("datasource.password");
    }

    public String getTableName() {
        return getProperty("benchmark.tableName");
    }

    public int getNumRows() {
        return getIntProperty("benchmark.numRows");
    }

    public int getCommitRate() {
        return getIntProperty("benchmark.commitRate");
    }

    private Properties loadProperties(String filePath) {
        Properties loadedProperties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (input == null) {
                throw new PropertiesLoadException("Unable to find " + FILE_PATH);
            }
            loadedProperties.load(input);
        } catch (IOException ex) {
            throw new PropertiesLoadException("Unable to load properties from " + FILE_PATH, ex);
        }
        return loadedProperties;
    }

    private String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new PropertiesLoadException("Property not found: " + key);
        }
        return value;
    }

    private int getIntProperty(String key) {
        try {
            return Integer.parseInt(getProperty(key));
        } catch (NumberFormatException ex) {
            throw new PropertiesLoadException("Invalid property format for key: " + key, ex);
        }
    }
}
