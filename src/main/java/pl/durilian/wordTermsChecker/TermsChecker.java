package pl.durilian.wordTermsChecker;

import com.codeborne.selenide.Selenide;
import lombok.extern.log4j.Log4j2;
import pl.durilian.wordTermsChecker.entities.Exam;
import pl.durilian.wordTermsChecker.entities.InfoCarAccount;
import pl.durilian.wordTermsChecker.pages.LoginPage;
import pl.durilian.wordTermsChecker.pages.ReservationPage;
import pl.durilian.wordTermsChecker.services.RestService;

@Log4j2
/**
 * Main class of the project responsible for checking free terms available for desired criterias
 */
public class TermsChecker {
    private final RestService client = new RestService();
    private final int MIN_POOLING_TIME = 30;
    private final Exam desiredExam;
    private final InfoCarAccount account;
    private final boolean checkNextMonth;
    private int poolingTime;

    /**
     * Constructor used for setting data needed for searches
     *
     * @param account        combination of email and password used to login into info-car website
     * @param desiredExam    Exam represent set of data describing exam: array of cities, category and exam type
     * @param checkNextMonth boolean describing if AvailavbleTermsChecker should check also terms available in next months
     */
    public TermsChecker(InfoCarAccount account, Exam desiredExam, boolean checkNextMonth) {
        this.desiredExam = desiredExam;
        this.account = account;
        this.checkNextMonth = checkNextMonth;
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
        boolean isAvailableTerm;
        ReservationPage reservationPage = new LoginPage()
                .start()
                .login(account.getEmail(), account.getPassword())
                .goToReservations();
        while (true) {
            for (String city : desiredExam.getCities()) {
                reservationPage.goToAvailableTerms(city, desiredExam);
                isAvailableTerm = isAvailableTermInCurrentMonth(city, reservationPage);
                if (!isAvailableTerm && this.checkNextMonth) {
                    reservationPage.goToNextMonth();
                    isAvailableTermInCurrentMonth(city, reservationPage);
                }
                Selenide.refresh();
            }
            log.info("Usypiam aplikację na: " + poolingTime / 1000 + " sekund");
            Selenide.sleep(poolingTime);
        }
    }

    private boolean isAvailableTermInCurrentMonth(String city, ReservationPage reservationPage) {
        boolean isAvailable;
        isAvailable = reservationPage
                .checkCurrentMonth(city, desiredExam.getExamType());
        if (isAvailable) {
            String termDate = reservationPage.getCurrentTerm();
            client.notify(city + " wolny termin:", termDate);
        }
        return isAvailable;
    }


}
