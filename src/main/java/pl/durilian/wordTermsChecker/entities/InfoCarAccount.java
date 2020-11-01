package pl.durilian.wordTermsChecker.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.durilian.wordTermsChecker.utils.ConfigurationManager;

/**
 * Class containg data set needed to loging into info-car website
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InfoCarAccount {

    String email;
    String password;

    public InfoCarAccount(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static InfoCarAccount getInfoCarAccountFromProperties() {
        ConfigurationManager configurationManager = ConfigurationManager.getInstance();
        String email = configurationManager.getTermCheckerPropertyValue("email");
        String password = configurationManager.getTermCheckerPropertyValue("password");

        return new InfoCarAccount(
                email,
                password
        );
    }
}
