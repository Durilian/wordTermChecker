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
            stop();
            log.debug("thread kurna interrupted!");
        }

    }

    public void stop() {
        exit = true;
    }
}