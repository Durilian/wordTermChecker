package pl.durilian.wordTermsChecker.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class NewPage extends AbstractBasePage<NewPage> {
    public final static String URI = "/new";

    //locators
    final private SelenideElement searchField = $("input");
    final private SelenideElement searchResult = $(".search-results-box");
    final private SelenideElement signForExamSearchResult = searchResult.find("[href*='rezerwacja']");

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
