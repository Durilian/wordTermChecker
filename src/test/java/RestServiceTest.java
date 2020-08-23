import org.testng.annotations.Test;
import pl.durilian.wordTermsChecker.services.RestService;

public class RestServiceTest {
    @Test
    public static void restTest() {
        RestService client = new RestService();
        var result = client.notify("tylko testuje", "testowa wiadomość");
        result.statusCode(200);
    }
}
