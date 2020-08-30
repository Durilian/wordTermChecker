import org.testng.annotations.Test;
import pl.durilian.wordTermsChecker.services.WirePusherNotifierService;

public class WirePusherNotifierServiceTest {
    @Test
    public static void restTest() {
        WirePusherNotifierService client = new WirePusherNotifierService();
        client.notify("tylko testuje", "testowa wiadomość");
    }
}
