package pl.durilian.wordTermsChecker.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import pl.durilian.wordTermsChecker.utils.Configuration;

import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.$;

@Getter
@Setter
@Log4j2
/**
 * contains fields and elements shared between all pages
 */
public abstract class AbstractBasePage<T> {
    private final String baseURL = "https://info-car.pl";
    //locators
    private final SelenideElement cookiesAcceptanceButton = $(byId("checkButton"));
    protected String URI;

    /**
     * Method accepting cookies if cookies acceptance button is displayed
     *
     * @return Pagge object which called this method
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
