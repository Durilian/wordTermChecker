package pl.durilian.wordTermsChecker.test;

import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import pl.durilian.wordTermsChecker.AvailableTermsChecker;
import pl.durilian.wordTermsChecker.entities.Exam;
import pl.durilian.wordTermsChecker.entities.InfoCarAccount;
import pl.durilian.wordTermsChecker.utils.Configuration;
import pl.durilian.wordTermsChecker.utils.ConfigurationManager;


public class WordTest {

    /**
     * <pre>
     * Run term checker as test - it allows the possibility to restart it easily with retryAnalyzer
     * checkNextMonth and poolingTime variables are read from properties
     * </pre>
     *
     * @param desiredExam if null then created from configuration.properties
     * @param account     if null then created from configuration.properties
     */
    @Test(retryAnalyzer = Retry.class)
    public static void checkTermsForMe(@Optional() Exam desiredExam, @Optional() InfoCarAccount account) {
        Configuration.initTermCheckerProperties();

        desiredExam = java.util.Optional.ofNullable(desiredExam).orElse(Exam.getExamFromProperties());
        account = java.util.Optional.ofNullable(account).orElse(InfoCarAccount.getInfoCarAccountFromProperties());
        boolean checkNextMonth = Boolean.parseBoolean(
                ConfigurationManager.getTermCheckerPropertyValue("checkNextMonth"));

        AvailableTermsChecker termChecker = new AvailableTermsChecker(account, desiredExam, checkNextMonth);

        termChecker.setPoolingTime(
                Integer.parseInt(ConfigurationManager.getTermCheckerPropertyValue("poolingTime")));

        termChecker.checkTerm();
    }
}
