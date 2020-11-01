package pl.durilian.wordTermsChecker.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigurationManager {

    static ConfigurationManager instance = null;
    Map<String, String> configuration;

    String SELENIDE_PREFIX = "selenide.";
    String WIREPUSHER_PREFIX = "wirepusher.";
    String TERMCHECKER_PREFIX = "termchecker.";

    private ConfigurationManager() {
        configuration = createConfiguration();
    }

    synchronized public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    /**
     * Loads properties from configuration that starts with SELENIDE_PREFIX
     *
     * @return Map with key-value pairs of selenide properties
     */
    public Map<String, String> getSelenideProperties() {
        Map<String, String> selenideProperties = new HashMap<>();
        for (Map.Entry<String, String> entry : configuration.entrySet()) {
            if (entry.getKey().startsWith(SELENIDE_PREFIX)) {
                selenideProperties.put(entry.getKey(), entry.getValue());
            }
        }
        return selenideProperties;
    }

    /**
     * Get value of property from configuration. Configuration need to bede initialized first!
     *
     * @param name full name of property e.g. "termchecker.email"
     * @return String value of property
     */
    public String getValue(String name) {
        return configuration.get(name);
    }

    /**
     * Get value of property from configuration. Configuration need to bede initialized first!
     *
     * @param name of selenide property e.g. "timeout"
     * @return String value of property
     */
    public String geSelenidePropertyValue(String name) {
        return getValue(SELENIDE_PREFIX + name);
    }

    /**
     * Get value of property from configuration. Configuration need to bede initialized first!
     *
     * @param name of TermChecker property e.g. "cities"
     * @return String value of property
     */
    public String getTermCheckerPropertyValue(String name) {
        return getValue(TERMCHECKER_PREFIX + name);

    }

    /**
     * Get value of property from configuration. Configuration need to bede initialized first!
     *
     * @param name of wirepusher property e.g. "deviceId"
     * @return String value of property
     */
    public String getWirePusherPropertyValue(String name) {
        return getValue(WIREPUSHER_PREFIX + name);
    }

    /**
     * Add or update configuration value
     *
     * @param name  key of the value
     * @param value
     */
    public void setValue(String name, String value) {
        configuration.put(name, value);
    }

    /**
     * Add or update configuration value
     *
     * @param name  key of the value
     * @param value
     */
    public void setTermCheckerPropertyValue(String name, String value) {
        setValue(TERMCHECKER_PREFIX + name, value);
    }

    /**
     * Add or update configuration value
     *
     * @param name  key of the value
     * @param value
     */
    public void setWirePusherPropertyValue(String name, String value) {
        setValue(WIREPUSHER_PREFIX + name, value);
    }

    /**
     * Loads properties from configuration that starts with WIREPUSHER_PREFIX
     *
     * @return Map with key-value pairs of wirepusher properties
     */
    public Map<String, String> getWirePusherProperties() {
        return getProperties(WIREPUSHER_PREFIX);
    }

    /**
     * Loads properties from configuration that starts with TERMCHECKER_PREFIX
     *
     * @return Map with key-value pairs of termchecker properties
     */
    public Map<String, String> getTermCheckerProperties() {
        return getProperties(TERMCHECKER_PREFIX);
    }

    /**
     * Loads properties from configuration that starts with desired prefix
     *
     * @param prefix of the property
     * @return Map with properties
     */
    private Map<String, String> getProperties(String prefix) {
        Map<String, String> properties = new HashMap<>();
        for (Map.Entry<String, String> entry : configuration.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                properties.put(entry.getKey(), entry.getValue());
            }
        }
        return properties;
    }

    /**
     * Loads all properties from configuration file and converts it into Map
     *
     * @return Map with key-value pairs of properties
     */
    private Map<String, String> createConfiguration() {
        final Map<String, String> configuration = new HashMap<>();

        loadConfigurationFile("configuration/configuration.properties").forEach((key, value) -> configuration.put((String) key, (String) value));

        return configuration;
    }

    /**
     * Loads all properties from file
     *
     * @param filename path to configuration file in resources
     * @return set of properties
     */
    private Properties loadConfigurationFile(String filename) {
        try {
            return PropertiesLoaderUtils.loadAllProperties(filename);
        } catch (IOException exception) {
            log.error("Could not load configuration from file: {}", filename);
            throw new UncheckedIOException(exception);
        }
    }
}
