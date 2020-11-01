package pl.durilian.wordTermsChecker;

import pl.durilian.wordTermsChecker.ui.UserInterface;

public class Runner {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new UserInterface();
        });
    }
}
