package pl.durilian.wordTermsChecker.pages;

import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.$;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginPage extends AbstractBasePage<LoginPage> {

    static String URI = "/oauth2/login";

    //locators
    SelenideElement usernameField = $(byId("username"));
    SelenideElement passwordField = $(byId("password"));
    SelenideElement loginButton = $(byId("register-button"));

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
