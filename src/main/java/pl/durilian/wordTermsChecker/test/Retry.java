package pl.durilian.wordTermsChecker.test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import lombok.extern.slf4j.Slf4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

@Slf4j
/**<pre>
 * Class responsible for evaluating if search and browser should be restarted
 * In cases like: webElement not visible or no internet connection etc. It will restart browser and search
 * In case of closing or crashing browser it will quit the search.
 * </pre>
 */
public class Retry implements IRetryAnalyzer {

    /**
     * Analyze if browser is closed or not.
     *
     * @param result of the test
     * @return true if browser is still open. false if browser crashed or was closed.
     */
    public boolean retry(ITestResult result) {
        try {
            WebDriverRunner.url();
            log.warn("Coś poszło nie tak, restartuję program i przeglądarkę");
            Selenide.closeWindow();
            return true;
        } catch (Exception ex) {
            log.info("Przeglądarka została zamknięta, przestaję ponawiać wyszukiwanie");
            result.setStatus(ITestResult.FAILURE);
            return false;
        }
    }
}