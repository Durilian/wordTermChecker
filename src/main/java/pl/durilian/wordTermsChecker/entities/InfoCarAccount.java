package pl.durilian.wordTermsChecker.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.durilian.wordTermsChecker.utils.ConfigurationManager;

/**
 * Class containg data set needed to loging into info-car website
 */
@Getter
@Setter
@NoArgsConstructor
public class InfoCarAccount {
    private String email;
    private String password;

    public InfoCarAccount(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static InfoCarAccount getInfoCarAccountFromProperties() {
        return new InfoCarAccount(
                ConfigurationManager.getTermCheckerPropertyValue("email"),
                ConfigurationManager.getTermCheckerPropertyValue("password")
        );
    }
}
