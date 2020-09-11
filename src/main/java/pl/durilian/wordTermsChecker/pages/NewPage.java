package pl.durilian.wordTermsChecker.pages;

import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import static com.codeborne.selenide.Selenide.$;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NewPage extends AbstractBasePage<NewPage> {
    public static String URI = "/new";

    //locators
    SelenideElement searchField = $("input");
    SelenideElement searchResult = $(".search-results-box");
    SelenideElement signForExamSearchResult = searchResult.find("[href*='rezerwacja']");

    /**
     * <pre>
     * Method use search text field to find link to ReservationPage
     *
     * WARNING: due to some website bug method has to do
     * this twice to reach working ReservationPage!
     * </pre>
     *
     * @return ReservationPage
     */
    public ReservationPage goToReservations() {

        searchField.scrollIntoView(true).sendKeys("egzamin");
        signForExamSearchResult.scrollIntoView(true).click();

        new ReservationPage().clickReservationButton();

        searchField.scrollIntoView(true).sendKeys("egzamin");
        signForExamSearchResult.scrollIntoView(true).click();

        return new ReservationPage();
    }
}
