package pl.durilian.wordTermsChecker.services;

import org.springframework.stereotype.Service;

@Service
public class ConsoleNotifierService implements NotifierService {

    @Override
    public void notify(String title, String message) {
        System.out.println("Title: " + title + " message: " + message);
    }
}
