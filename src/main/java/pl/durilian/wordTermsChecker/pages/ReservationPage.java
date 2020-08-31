package pl.durilian.wordTermsChecker.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.extern.log4j.Log4j2;
import pl.durilian.wordTermsChecker.entities.Exam;
import pl.durilian.wordTermsChecker.entities.ExamType;

import static com.codeborne.selenide.Condition.and;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Log4j2
public class ReservationPage extends AbstractBasePage<ReservationPage> {
    public final static String URI = "/infocar/konto/word/rezerwacja.html";
    //locators
    final private SelenideElement reserveTermButtonBeforeLogin = $(byId("rt"));
    final private SelenideElement reserveTermButtonAfterLogin = $(".redBtn");
    final private SelenideElement monthTitleBar = $(byId("titleBarText"));
    final private SelenideElement blurredArea = $(".https.blurred");
    final private SelenideElement nextMonth = $(".icon.right");
    final private SelenideElement wordSelector = $(byId("wordSelector"));
    final private SelenideElement currentMonthTitle = $(byId("scheduleCurrentMonth"));

    final private ElementsCollection wordSelects = wordSelector.findAll("select");
    final private ElementsCollection availableDays = $$(".available");
    final private ElementsCollection selectedDayTitile = $$(byId("selectedDayTitle"));
    final private ElementsCollection examCategories = $$(".examCategory");
    final private ElementsCollection wordSelectLinks = $$(".wordSelectButton");

    final private String availableExamTypeLocator = "//td[not(@class='notAvailable')]//div[contains(text(), '%s')]";

    final private int VOIVODESHIP_SELECT_INDEX = 0;
    final private int CITY_SELECT_INDEX = 1;

    public ReservationPage() {
        setURI(URI);
    }

    /**
     * Method clicking Reservation button. Result page depends on login status
     */
    public void clickReservationButton() {
        reserveTermButtonBeforeLogin.click();
    }

    /**
     * <pre>>
     * Use only after successfully loging into info-car website
     * This method picks desired city and exam category to
     * reach current month view with available terms
     * </pre>
     *
     * @param city where it will be checking available terms
     * @param exam to get category from it
     * @return ReservationPage
     */
    public ReservationPage goToAvailableTerms(String city, Exam exam) {
        log.trace("Otwieram dostępne terminy");

        reserveTermButtonAfterLogin.click();
        chooseCity(city);
        chooseCategory(exam.getCategory());

        monthTitleBar.shouldHave(Condition.text("Wybierz dzień,"));

        return this;
    }

    /**
     * Method gets list of elements with at least one available term.
     * Then it clicks each element and checks if it has a free terms in desired
     * exam category. After that it closes day view and go to next element
     *
     * @param city     passed to checkSingleDay
     * @param examType passed to checkSingleDay
     * @return true if there is free term available
     */
    public boolean checkCurrentMonth(String city, ExamType examType) {
        log.trace("Sprawdzam aktualny miesiąc");
        boolean isAvailableTerm;

        for (SelenideElement term : availableDays) {
            term.click();
            isAvailableTerm = checkSingleDay(city, examType);
            if (isAvailableTerm)
                return isAvailableTerm;
            blurredArea.pressEscape();
        }
        return false;
    }

    /**
     * Method reaching next month of current view.
     * Waits untill month title is visible and different than previous month title
     *
     * @return ReservationPage
     */
    public ReservationPage goToNextMonth() {
        String previousMonthTitle = currentMonthTitle.getText();
        Condition differentThanPrevious = and("different and visible", Condition.visible, not(Condition.text(previousMonthTitle)));

        nextMonth.click();
        currentMonthTitle.shouldBe(differentThanPrevious);
        return this;
    }

    /**
     * <pre>
     * Method checking currently opened day for free terms
     * in desired exam type.
     * Checks if the cell has not class "notavailable"
     * Found free term is logged as fatal level so it goes to additional
     * log file: yyyy-MM-dd-wolneTerminy.log
     * </pre>
     *
     * @param city     only for logging/notification purpose
     * @param examType for getting exam type needed for checks
     * @return true if there is free hour with desired exam type
     */
    private boolean checkSingleDay(String city, ExamType examType) {
        log.trace("Sprawdzam dostępne godziny");

        String desiredExamTypeLocator = String.format(availableExamTypeLocator, examType.getExamType());
        ElementsCollection availableHours = $$(byXpath(desiredExamTypeLocator));

        String termDate = getCurrentTerm();
        if (availableHours.size() == 0) {
            log.info(city + ": " + termDate + ": Nie ma wolnych terminów");
            return false;
        } else {
            log.fatal(city + ": " + termDate + "!!!!!!!!! JEST TERMIN !!!!!!!!!!");
            return true;
        }
    }

    /**
     * Finds desired category and click it
     *
     * @param examCategory
     * @return ReservationPage
     */
    private ReservationPage chooseCategory(String examCategory) {
        examCategories
                .filter(Condition.text(examCategory))
                .first()
                .click();

        return this;
    }

    /**
     * Finds desired city in city select and click it
     *
     * @param city
     * @return ReservationPage
     */
    private ReservationPage chooseCity(String city) {
        SelenideElement citySelect = wordSelects.get(CITY_SELECT_INDEX);
        citySelect.selectOption(city);
        wordSelectLinks
                .filter(Condition.visible)
                .first()
                .click();

        return this;
    }

    /**
     * Method reading description of currently opened date from website
     *
     * @return e.g. "Terminy egzaminów dostępne 16. września 2020"
     */
    public String getCurrentTerm() {
        return selectedDayTitile
                .filter(Condition.visible)
                .first()
                .getText();
    }

}
