package pl.durilian.wordTermsChecker.utils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Configuration {

    /**
     * Loads Selenide properties and sets them as System properties
     */
    public static void initSelenideProperties() {
        ConfigurationManager.getSelenideProperties().forEach((key, value) -> {
            log.info("Setting selenide property: {}={}", key, value);
            System.setProperty(key, value);
        });
    }

    public static void initWirePusherProperties() {
        ConfigurationManager.getWirePusherProperties().forEach((key, value) -> {
            log.info("Setting wirepusher property: {}={}", key, value);
            System.setProperty(key, value);
        });
    }

    public static void initTermCheckerProperties() {
        ConfigurationManager.getTermCheckerProperties().forEach((key, value) -> {
            if (!key.endsWith("password")) {
                log.info("Setting termchecker property: {}={}", key, value);
            }
            System.setProperty(key, value);
        });
    }
}
