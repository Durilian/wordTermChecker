package pl.durilian.wordTermsChecker.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Configuration {
    private final static ConfigurationManager configurationManager = ConfigurationManager.getInstance();
    /**
     * Loads Selenide properties and sets them as System properties
     */
    public static void initSelenideProperties() {
        configurationManager.getSelenideProperties().forEach((key, value) -> {
            log.info("Setting selenide property: {}={}", key, value);
            System.setProperty(key, value);
        });
    }

    public static void initWirePusherProperties() {
        configurationManager.getWirePusherProperties().forEach((key, value) -> {
            log.info("Setting wirepusher property: {}={}", key, value);
            System.setProperty(key, value);
        });
    }

    public static void initTermCheckerProperties() {
        configurationManager.getTermCheckerProperties().forEach((key, value) -> {
            if (!key.endsWith("password")) {
                log.info("Setting termchecker property: {}={}", key, value);
            }
            System.setProperty(key, value);
        });
    }
}
