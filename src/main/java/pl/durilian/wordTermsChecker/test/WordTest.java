package pl.durilian.wordTermsChecker.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import pl.durilian.wordTermsChecker.AvailableTermsChecker;
import pl.durilian.wordTermsChecker.entities.Exam;
import pl.durilian.wordTermsChecker.entities.InfoCarAccount;
import pl.durilian.wordTermsChecker.services.NotifierService;
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
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(Configuration.class);
        context.refresh();

        Configuration.initTermCheckerProperties();

        desiredExam = ofNullable(desiredExam).orElse(context.getBean(Exam.class));
        account = ofNullable(account).orElse(context.getBean(InfoCarAccount.class));
        boolean checkNextMonth = Boolean.parseBoolean(
                ConfigurationManager.getTermCheckerPropertyValue("checkNextMonth"));

        AvailableTermsChecker termChecker = new AvailableTermsChecker(account, desiredExam, checkNextMonth, context.getBean(NotifierService.class));

        termChecker.setPoolingTime(
                Integer.parseInt(ConfigurationManager.getTermCheckerPropertyValue("poolingTime")));

        termChecker.checkTerm();
    }
}
