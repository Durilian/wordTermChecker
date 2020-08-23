package pl.durilian.wordTermsChecker.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Log4j2
public class ConfigurationManager {
    private static final String SELENIDE_PREFIX = "selenide.";
    private static final String WIREPUSHER_PREFIX = "wirepusher.";
    private static final String TERMCHECKER_PREFIX = "termchecker.";

    private static final Map<String, String> configuration = createConfiguration();

    /**
     * Loads properties from configuration that starts with SELENIDE_PREFIX
     *
     * @return Map with key-value pairs of selenide properties
     */
    public static Map<String, String> getSelenideProperties() {
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
    public static String getValue(String name) {
        return configuration.get(name);
    }

    /**
     * Get value of property from configuration. Configuration need to bede initialized first!
     *
     * @param name of selenide property e.g. "timeout"
     * @return String value of property
     */
    public static String geSelenidePropertyValue(String name) {
        return getValue(SELENIDE_PREFIX + name);
    }

    /**
     * Get value of property from configuration. Configuration need to bede initialized first!
     *
     * @param name of TermChecker property e.g. "cities"
     * @return String value of property
     */
    public static String getTermCheckerPropertyValue(String name) {
        return getValue(TERMCHECKER_PREFIX + name);

    }

    /**
     * Get value of property from configuration. Configuration need to bede initialized first!
     *
     * @param name of wirepusher property e.g. "deviceId"
     * @return String value of property
     */
    public static String getWirePusherPropertyValue(String name) {
        return getValue(WIREPUSHER_PREFIX + name);
    }

    /**
     * Add or update configuration value
     *
     * @param name  key of the value
     * @param value
     */
    public static void setValue(String name, String value) {
        configuration.put(name, value);
    }

    /**
     * Add or update configuration value
     *
     * @param name  key of the value
     * @param value
     */
    public static void setTermCheckerPropertyValue(String name, String value) {
        setValue(TERMCHECKER_PREFIX + name, value);
    }

    /**
     * Add or update configuration value
     *
     * @param name  key of the value
     * @param value
     */
    public static void setWirePusherPropertyValue(String name, String value) {
        setValue(WIREPUSHER_PREFIX + name, value);
    }

    /**
     * Loads properties from configuration that starts with WIREPUSHER_PREFIX
     *
     * @return Map with key-value pairs of wirepusher properties
     */
    public static Map<String, String> getWirePusherProperties() {
        Map<String, String> wirePusherProperties = new HashMap<>();
        for (Map.Entry<String, String> entry : configuration.entrySet()) {
            if (entry.getKey().startsWith(WIREPUSHER_PREFIX)) {
                wirePusherProperties.put(entry.getKey(), entry.getValue());
            }
        }
        return wirePusherProperties;
    }

    /**
     * Loads properties from configuration that starts with TERMCHECKER_PREFIX
     *
     * @return Map with key-value pairs of termchecker properties
     */
    public static Map<String, String> getTermCheckerProperties() {
        Map<String, String> termCheckerProperties = new HashMap<>();
        for (Map.Entry<String, String> entry : configuration.entrySet()) {
            if (entry.getKey().startsWith(TERMCHECKER_PREFIX)) {
                termCheckerProperties.put(entry.getKey(), entry.getValue());
            }
        }
        return termCheckerProperties;
    }

    /**
     * Loads all properties from configuration file and converts it into Map
     *
     * @return Map with key-value pairs of properties
     */
    private static Map<String, String> createConfiguration() {
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
    private static Properties loadConfigurationFile(String filename) {
        try {
            return PropertiesLoaderUtils.loadAllProperties(filename);
        } catch (IOException exception) {
            log.fatal("Could not load configuration from file: {}", filename);
            throw new UncheckedIOException(exception);
        }
    }
}
