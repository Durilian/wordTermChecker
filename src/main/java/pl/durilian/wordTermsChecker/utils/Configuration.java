package pl.durilian.wordTermsChecker.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import pl.durilian.wordTermsChecker.entities.Exam;
import pl.durilian.wordTermsChecker.entities.ExamType;
import pl.durilian.wordTermsChecker.entities.InfoCarAccount;
import pl.durilian.wordTermsChecker.services.ConsoleNotifierService;
import pl.durilian.wordTermsChecker.services.NotifierService;
import pl.durilian.wordTermsChecker.services.WirePusherNotifierService;

@Log4j2
@org.springframework.context.annotation.Configuration
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

    //BEANS
    @Bean
    public static Exam getExamFromProperties() {
        String[] cities = ConfigurationManager.getTermCheckerPropertyValue("cities").split(",");
        return new Exam(
                cities,
                ConfigurationManager.getTermCheckerPropertyValue("category"),
                ExamType.get(ConfigurationManager.getTermCheckerPropertyValue("examType"))
        );
    }

    @Bean
    public static InfoCarAccount getInfoCarAccountFromProperties() {
        return new InfoCarAccount(
                ConfigurationManager.getTermCheckerPropertyValue("email"),
                ConfigurationManager.getTermCheckerPropertyValue("password")
        );
    }

    @Bean
    @Profile("!dev")
    public NotifierService getWirePusherNotifierService() {
        return new WirePusherNotifierService();
    }

    @Bean
    @Profile("dev")
    public NotifierService getConsoleNotifierService() {
        return new ConsoleNotifierService();
    }
}
