package pl.durilian.wordTermsChecker.entities;

import lombok.Getter;
import lombok.Setter;

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
}
