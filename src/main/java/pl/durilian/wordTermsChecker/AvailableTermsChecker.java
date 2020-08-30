package pl.durilian.wordTermsChecker;

import com.codeborne.selenide.Selenide;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import pl.durilian.wordTermsChecker.entities.Exam;
import pl.durilian.wordTermsChecker.entities.InfoCarAccount;
import pl.durilian.wordTermsChecker.pages.LoginPage;
import pl.durilian.wordTermsChecker.pages.ReservationPage;
import pl.durilian.wordTermsChecker.services.NotifierService;

@Log4j2
/**
 * Main class of the project responsible for checking free terms available for desired criterias
 */
public class AvailableTermsChecker {
    private final int MIN_POOLING_TIME = 30;
    private final Exam desiredExam;
    private final InfoCarAccount account;
    private final boolean checkNextMonth;
    private int poolingTime;
    private String currentCity;
    private final NotifierService notifier;

    /**
     * Constructor used for setting data needed for searches
     *
     * @param account        combination of email and password used to login into info-car website
     * @param desiredExam    Exam represent set of data describing exam: array of cities, category and exam type
     * @param checkNextMonth boolean describing if AvailavbleTermsChecker should check also terms available in next months
     */
    @Autowired
    public AvailableTermsChecker(InfoCarAccount account, Exam desiredExam, boolean checkNextMonth, NotifierService notifier) {
        this.desiredExam = desiredExam;
        this.account = account;
        this.checkNextMonth = checkNextMonth;
        this.notifier = notifier;
    }

    /**
     * Setter for interval (sleep time) between relaunching search
     *
     * @param seconds
     */
    public void setPoolingTime(int seconds) {
        int actualSeconds = seconds;
        if (actualSeconds < MIN_POOLING_TIME) {
            actualSeconds = MIN_POOLING_TIME;
        }
        log.trace("Ustawiam czas uśpienia aplikacji na: " + actualSeconds + " seconds");
        this.poolingTime = actualSeconds * 1000;

    }

    /**
     * <pre>
     * Key method of whole project.
     * Launches website info-car and logs into it with provided account
     * After logging it goes to reservation and checks every day with
     * at least one available terms if it contains a free term fulfilling
     * the provided requirements if yes it saves a message into logs
     * After checking current month (or current and next month)
     * search stop for poolingTime and the relaunches the search
     *
     * If you added your deviceId from wirePusher to configuration
     * You will receive notification when application find free term
     * </pre>
     */
    public void checkTerm() {
        boolean isAvailableTerm = false;
        ReservationPage reservationPage = new LoginPage()
                .start()
                .login(account.getEmail(), account.getPassword())
                .goToReservations();
        while (true) {
            for (String city : desiredExam.getCities()) {
                this.currentCity = city;
                isAvailableTerm = reservationPage
                        .goToAvailableTerms(currentCity, desiredExam)
                        .checkCurrentMonth(currentCity, desiredExam);
                if (isAvailableTerm) {
                    String termDate = reservationPage.getCurrentTerm();
                    notifier.notify(city + " wolny termin:", termDate);
                    isAvailableTerm = false;
                } else if (this.checkNextMonth) {
                    isAvailableTerm =
                            reservationPage
                                    .goToNextMonth()
                                    .checkCurrentMonth(currentCity, desiredExam);
                    if (isAvailableTerm) {
                        String termDate = reservationPage.getCurrentTerm();
                        notifier.notify(city + ":", termDate);
                        isAvailableTerm = false;
                    }
                }
                Selenide.refresh();
            }
            log.info("Usypiam aplikację na: " + poolingTime / 1000 + " sekund");
            Selenide.sleep(poolingTime);
        }
    }
}
