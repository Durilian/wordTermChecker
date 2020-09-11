package pl.durilian.wordTermsChecker.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.testng.TestNG;
import pl.durilian.wordTermsChecker.test.WordTest;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@Slf4j
/**
 * It is not a thread itself but it contains a thread that is responsible for running searches.
 * Currently application create only 1 search thread and this thread need to be stopped to run new search.
 */
public class SearchThread implements Runnable {
    final String name = "searchThread";
    final Thread thread;
    boolean exit;
    String[] cities;
    String category;
    String examType;
    String email;
    String password;
    String wirePusherId;
    int poolingTime;
    boolean checkNextMonth;
    ConfigurationManager configurationManager = ConfigurationManager.getInstance();

    /**
     * <pre>
     * Loads data to thread responsible for launching searches
     * </pre>
     *
     * @param cities      array of cities passed from UI as Miasto1, Miasto2 etc.
     * @param category    category passed from UI e.g. "B"
     * @param examType    teoria or praktyka String passed from UI
     * @param email       email used as login on info-car passed from UI
     * @param password    password for email on info-car passed from UI
     * @param poolingTime interval between running search again
     */
    public SearchThread(String[] cities, String category, String examType, String email, String
            password, String wirePusherId, int poolingTime, boolean checkNextMonth) {
        thread = new Thread(this, name);
        log.info("New thread: " + thread);

        exit = false;
        this.cities = cities;
        this.category = category;
        this.examType = examType;
        this.email = email;
        this.password = password;
        this.wirePusherId = wirePusherId;
        this.poolingTime = poolingTime;
        this.checkNextMonth = checkNextMonth;

    }

    /**
     * Run the test that looks for a free term. Method uses data stored in thread to find desired exam
     */
    public void run() {
        while (!exit) {
            String citiesSingleString = String.join(",", cities);

            String poolingTimeString = String.valueOf(poolingTime);
            configurationManager.setTermCheckerPropertyValue("email", email);
            configurationManager.setTermCheckerPropertyValue("category", category);
            configurationManager.setTermCheckerPropertyValue("password", password);
            configurationManager.setTermCheckerPropertyValue("examType", examType);
            configurationManager.setTermCheckerPropertyValue("category", category);
            configurationManager.setTermCheckerPropertyValue("cities", citiesSingleString);
            configurationManager.setWirePusherPropertyValue("deviceId", wirePusherId);
            configurationManager.setTermCheckerPropertyValue("poolingTime", poolingTimeString);
            configurationManager.setTermCheckerPropertyValue("checkNextMonth", String.valueOf(checkNextMonth));

            TestNG testSuite = new TestNG();
            testSuite.setTestClasses(new Class[]{WordTest.class});
            testSuite.addListener(new TestSuiteListener());
            testSuite.run();
        }

    }

    /**
     * Sets flag responsible for stopping search
     */
    public void stop() {
        exit = true;
    }
}