package pl.durilian.wordTermsChecker.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import pl.durilian.wordTermsChecker.entities.Exam;
import pl.durilian.wordTermsChecker.entities.ExamType;

import static com.codeborne.selenide.Condition.and;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReservationPage extends AbstractBasePage<ReservationPage> {
    public final static String URI = "/infocar/konto/word/rezerwacja.html";
    //locators
    SelenideElement reserveTermButtonBeforeLogin = $(byId("rt"));
    SelenideElement reserveTermButtonAfterLogin = $(".redBtn");
    SelenideElement monthTitleBar = $(byId("titleBarText"));
    SelenideElement blurredArea = $(".https.blurred");
    SelenideElement nextMonth = $(".icon.right");
    SelenideElement wordSelector = $(byId("wordSelector"));
    SelenideElement currentMonthTitle = $(byId("scheduleCurrentMonth"));

    ElementsCollection wordSelects = wordSelector.findAll("select");
    ElementsCollection availableDays = $$(".available");
    ElementsCollection selectedDayTitile = $$(byId("selectedDayTitle"));
    ElementsCollection examCategories = $$(".examCategory");
    ElementsCollection wordSelectLinks = $$(".wordSelectButton");

    String availableExamTypeLocator = "//td[not(@class='notAvailable')]//div[contains(text(), '%s')]";

    int VOIVODESHIP_SELECT_INDEX = 0;
    int CITY_SELECT_INDEX = 1;

    public ReservationPage() {
        setUri(URI);
    }

    /**
     * Method clicking Reservation button. Result page depends on login status
     */
    public void clickReservationButton() {
        reserveTermButtonBeforeLogin.click();
    }

    /**
     * <pre>
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
            term.scrollIntoView(true).click();
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
            log.error(city + ": " + termDate + "!!!!!!!!! JEST TERMIN !!!!!!!!!!");
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
}
