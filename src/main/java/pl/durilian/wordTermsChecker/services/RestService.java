package pl.durilian.wordTermsChecker.services;

import io.restassured.response.ValidatableResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import pl.durilian.wordTermsChecker.utils.Configuration;
import pl.durilian.wordTermsChecker.utils.ConfigurationManager;

import java.util.Map;

import static io.restassured.RestAssured.given;

@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestService {
    static ConfigurationManager configurationManager = ConfigurationManager.getInstance();

    public RestService() {
        Configuration.initWirePusherProperties();
    }

    /**
     * Method sending post request to WirePusher launching push notification
     * to comma separated devices ids' from configuration.
     * To use it you need WirePusher application for android phone
     *
     * @param title   of the push notification
     * @param message of the push notification
     */
    public ValidatableResponse notify(String title, String message) {
        final String baseURL = "https://wirepusher.com/send";
        log.info(String.format("Wysyłam wiadomość: %s o treści %s", title, message));
        return given()
                .when()
                .queryParams(Map.of
                        (
                                "id", configurationManager.getValue("wirepusher.deviceId"),
                                "title", title,
                                "message", message,
                                "type", "WORD",
                                "action", "https://info-car.pl/infocar/konto/word/rezerwacja.html"
                        ))
                .post(baseURL)
                .then()
                .log()
                .status();
    }
}
