package pl.durilian.wordTermsChecker.test;

import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import pl.durilian.wordTermsChecker.TermsChecker;
import pl.durilian.wordTermsChecker.entities.Exam;
import pl.durilian.wordTermsChecker.entities.InfoCarAccount;
import pl.durilian.wordTermsChecker.utils.Configuration;
import pl.durilian.wordTermsChecker.utils.ConfigurationManager;

import static java.util.Optional.ofNullable;


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
        ConfigurationManager config = ConfigurationManager.getInstance();

        desiredExam = ofNullable(desiredExam).orElse(Exam.getExamFromProperties());
        account = ofNullable(account).orElse(InfoCarAccount.getInfoCarAccountFromProperties());
        int poolingTime = Integer.parseInt(config.getTermCheckerPropertyValue("poolingTime"));
        boolean checkNextMonth = Boolean.parseBoolean(config.getTermCheckerPropertyValue("checkNextMonth"));

        TermsChecker termChecker = new TermsChecker(account, desiredExam, checkNextMonth);
        termChecker.setPoolingTime(poolingTime);

        termChecker.checkTerm();
    }
}
