package pl.durilian.wordTermsChecker.entities;

import lombok.Getter;
import lombok.Setter;
import pl.durilian.wordTermsChecker.utils.ConfigurationManager;

/**
 * Class containg data set needed to loging into info-car website
 */
@Getter
@Setter
public class InfoCarAccount {
    private String email;
    private String password;

    public InfoCarAccount(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static InfoCarAccount getInfoCarAccountFromProperties() {
        String email = ConfigurationManager.getTermCheckerPropertyValue("email");
        String password = ConfigurationManager.getTermCheckerPropertyValue("password");

        return new InfoCarAccount(
                email,
                password
        );
    }
}
