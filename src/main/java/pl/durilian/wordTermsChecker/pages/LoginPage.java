package pl.durilian.wordTermsChecker.pages;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.$;

@Log4j2
public class LoginPage extends AbstractBasePage<LoginPage> {

    public final static String URI = "/oauth2/login";

    //locators
    final private SelenideElement usernameField = $(byId("username"));
    final private SelenideElement passwordField = $(byId("password"));
    final private SelenideElement loginButton = $(byId("register-button"));

    public LoginPage() {
        setURI(URI);
    }

    /**
     * Method logging into info-car
     *
     * @param email    used as login
     * @param password to info-car account
     * @return NewPage which is loaded after proper logging
     */
    public NewPage login(String email, String password) {
        log.trace("Loguje się na adres: " + email);
        usernameField.setValue(email);
        passwordField.setValue(password);
        loginButton.click();

        return new NewPage();
    }

}
