package pl.durilian.wordTermsChecker.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import pl.durilian.wordTermsChecker.utils.Configuration;

import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.$;

@Getter
@Setter
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)

/**
 * contains fields and elements shared between all pages
 */
public abstract class AbstractBasePage<T> {
    final String baseURL = "https://info-car.pl";

    final SelenideElement cookiesAcceptanceButton = $(byId("checkButton"));

    protected String URI;

    /**
     * Method accepting cookies if cookies acceptance button is displayed
     *
     * @return Page object which called this method
     */
    private T acceptCookies() {
        if (cookiesAcceptanceButton.isDisplayed()) {
            cookiesAcceptanceButton.click();
        }
        return (T) this;
    }

    /**
     * Start point of browser. It launches browser with properties from configuration file.
     * Accepts cookies if there are displayed after reaching URL
     *
     * @return Pagge object which called this method
     */
    public T start() {
        Configuration.initSelenideProperties();
        log.trace("Uruchamiam przeglądarkę");
        Selenide.open(baseURL + getURI());
        acceptCookies();

        return (T) this;
    }

    /**
     * Goes to page which called this method
     *
     * @return Pagge object which called this method
     */
    public T open() {
        Selenide.open(baseURL + getURI());
        return (T) this;
    }
}
