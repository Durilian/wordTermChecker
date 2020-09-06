package pl.durilian.wordTermsChecker.ui;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.testng.TestNG;
import pl.durilian.wordTermsChecker.entities.ExamType;
import pl.durilian.wordTermsChecker.test.WordTest;
import pl.durilian.wordTermsChecker.utils.Configuration;
import pl.durilian.wordTermsChecker.utils.ConfigurationManager;
import pl.durilian.wordTermsChecker.utils.TestSuiteListener;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Single class responsible for whole UI of the application. </br>
 * It is only a support tool used in jar version of app for those who are not familliar with code
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInterface extends JFrame {
    private static final long serialVersionUID = 1L;
    //consts
    final static int LOGS_DELAY = 450; //milis
    final static int BUTTON_WITDH = 240;
    final static int BUTTON_HEIGHT = 40;
    final static int LABEL_WIDTH = 400;
    final static int LABEL_HEIGHT = 100;
    final static int TEXT_FIELD_WIDTH = 250;
    final static int TEXT_FIELD_HEIGHT = 30;
    final static int LOGS_WIDTH = 850;
    final static int LOGS_HEIGHT = 450;
    final static int FRAME_WIDTH = 1250;
    final static int FRAME_HEIGHT = 850;
    //logs
    final JTextArea textAreaLogs;
    final JScrollPane scroll;
    String logs;
    int prevLogListSize = 0;
    volatile Thread searchThread;

    /**
     * <pre>
     * constructor for whole UI.
     * It may be ugly and contain a lot of anti-patterns.
     * I simply use it as gateway for non-technical user.
     * And yes. I have no idea what I am doing. It is new for me :)
     * </pre>
     */
    public UserInterface() {
        createLogsIfNotExist();
        //inits configuration for default values
        Configuration.initTermCheckerProperties();
        Configuration.initWirePusherProperties();
        String[] defaultCities = loadCitiesForUI();


        JFrame frame = new JFrame("Wyszukiwarka terminów");
        //start button
        JButton runButton = new JButton("Wyszukaj terminów");
        runButton.setBounds(100, 270, BUTTON_WITDH, BUTTON_HEIGHT);

        //Clearing logs
        JButton clearLogsButton = new JButton("Wyczyść logi");
        clearLogsButton.setBounds(890, 730, BUTTON_WITDH, BUTTON_HEIGHT);

        //LABELS
        JLabel labelCity1 = new JLabel();
        labelCity1.setText("Miasto1: ");
        labelCity1.setBounds(10, 15, LABEL_WIDTH, LABEL_HEIGHT);

        JLabel labelCity2 = new JLabel();
        labelCity2.setText("Miasto2: ");
        labelCity2.setBounds(10, 55, LABEL_WIDTH, LABEL_HEIGHT);

        JLabel labelCity3 = new JLabel();
        labelCity3.setText("Miasto3: ");
        labelCity3.setBounds(10, 95, LABEL_WIDTH, LABEL_HEIGHT);

        JLabel labelCategory = new JLabel();
        labelCategory.setText("Kategoria: ");
        labelCategory.setBounds(10, 135, LABEL_WIDTH, LABEL_HEIGHT);

        JLabel labelExamType = new JLabel();
        labelExamType.setText("Typ egzaminu: ");
        labelExamType.setBounds(10, 175, LABEL_WIDTH, LABEL_HEIGHT);

        JLabel labelEmail = new JLabel();
        labelEmail.setText("Email: ");
        labelEmail.setBounds(400, 15, LABEL_WIDTH, LABEL_HEIGHT);

        JLabel labelPassword = new JLabel();
        labelPassword.setText("Hasło: ");
        labelPassword.setBounds(400, 55, LABEL_WIDTH, LABEL_HEIGHT);

        JLabel labelWirePusherID = new JLabel();
        labelWirePusherID.setText("WirePusher ID: ");
        labelWirePusherID.setBounds(400, 95, LABEL_WIDTH, LABEL_HEIGHT);

        JLabel labelPoolingTime = new JLabel();
        labelPoolingTime.setText("Interwał[sek]: ");
        labelPoolingTime.setBounds(400, 135, LABEL_WIDTH, LABEL_HEIGHT);

        JLabel labelNextMonth = new JLabel();
        labelNextMonth.setText("Sprawdź następny miesiąc: ");
        labelNextMonth.setBounds(400, 175, LABEL_WIDTH, LABEL_HEIGHT);

        //TEXT FIELDS
        JTextField textfieldCity1 = new JTextField();
        textfieldCity1.setText(defaultCities[0]);
        textfieldCity1.setBounds(100, 50, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);


        JTextField textfieldCity2 = new JTextField();
        textfieldCity2.setText(defaultCities[1]);
        textfieldCity2.setBounds(100, 90, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);

        JTextField textfieldCity3 = new JTextField();
        textfieldCity3.setText(defaultCities[2]);
        textfieldCity3.setBounds(100, 130, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);

        JTextField textfieldCategory = new JTextField();
        textfieldCategory.setText(ConfigurationManager.getTermCheckerPropertyValue("category"));
        textfieldCategory.setBounds(100, 170, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);

        JTextField textfieldEmail = new JTextField();
        textfieldEmail.setText(ConfigurationManager.getTermCheckerPropertyValue("email"));
        textfieldEmail.setBounds(500, 50, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);

        JPasswordField textfieldPassword = new JPasswordField();
        textfieldPassword.setText(ConfigurationManager.getTermCheckerPropertyValue("password"));
        textfieldPassword.setBounds(500, 90, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);

        JTextField textfieldWirePusherID = new JTextField();
        textfieldWirePusherID.setText(ConfigurationManager.getWirePusherPropertyValue("deviceId"));
        textfieldWirePusherID.setBounds(500, 130, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);

        JFormattedTextField textfieldPoolingTime = new JFormattedTextField(getIntegerFormatter());
        textfieldPoolingTime.setText(ConfigurationManager.getTermCheckerPropertyValue("poolingTime"));
        textfieldPoolingTime.setBounds(500, 170, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);

        //Comboboxes
        String[] examTypes = {ExamType.PRACTICE.getExamType(), ExamType.THEORY.getExamType()};
        JComboBox comboBoxExamType = new JComboBox(examTypes);
        comboBoxExamType.setBounds(100, 210, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);

        //Checkboxes
        JCheckBox checkNextMonth = new JCheckBox();
        checkNextMonth.setBounds(560, 210, 30, 30);

        //TAB order for keyboard/non-mouse users
        textfieldCity1.setNextFocusableComponent(textfieldCity2);
        textfieldCity2.setNextFocusableComponent(textfieldCity3);
        textfieldCity3.setNextFocusableComponent(textfieldCategory);
        textfieldCategory.setNextFocusableComponent(comboBoxExamType);
        comboBoxExamType.setNextFocusableComponent(textfieldEmail);
        textfieldEmail.setNextFocusableComponent(textfieldPassword);
        textfieldPassword.setNextFocusableComponent(textfieldWirePusherID);
        textfieldWirePusherID.setNextFocusableComponent(textfieldPoolingTime);
        textfieldPoolingTime.setNextFocusableComponent(checkNextMonth);
        checkNextMonth.setNextFocusableComponent(runButton);

        //Logs area
        textAreaLogs = new JTextArea();
        textAreaLogs.setEditable(false);
        scroll = new JScrollPane(textAreaLogs);
        scroll.setBounds(10, 330, LOGS_WIDTH, LOGS_HEIGHT);

        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        //add to frame
        frame.add(labelCity1);
        frame.add(labelCity2);
        frame.add(labelCity3);
        frame.add(labelCategory);
        frame.add(labelExamType);
        frame.add(labelEmail);
        frame.add(labelPassword);
        frame.add(labelWirePusherID);
        frame.add(labelPoolingTime);
        frame.add(labelNextMonth);
        frame.add(runButton);
        frame.add(clearLogsButton);
        frame.add(textfieldCity1);
        frame.add(textfieldCity2);
        frame.add(textfieldCity3);
        frame.add(textfieldCategory);
        frame.add(comboBoxExamType);
        frame.add(textfieldEmail);
        frame.add(textfieldPassword);
        frame.add(textfieldWirePusherID);
        frame.add(textfieldPoolingTime);
        frame.add(checkNextMonth);
        frame.getContentPane().add(scroll);

        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /**
         * method updating logs every @{LOGS_DELAY} miliseconds
         */

        /**
         * Add start action to run button.
         * You can launch multiple searches with different configuration in parallel
         */
        runButton.addActionListener(ae -> {
            searchThread = new Thread(() -> {
                Thread thisThread = Thread.currentThread();
                while (searchThread == thisThread) {
                    List<String> cities = new ArrayList<String>();
                    if (!textfieldCity1.getText().isEmpty()) {
                        cities.add(textfieldCity1.getText());
                    }
                    if (!textfieldCity2.getText().isEmpty()) {
                        cities.add(textfieldCity2.getText());
                    }
                    if (!textfieldCity3.getText().isEmpty()) {
                        cities.add(textfieldCity3.getText());
                    }


                    checkFreeTerms(cities.stream().toArray(String[]::new),
                            textfieldCategory.getText(),
                            (String) comboBoxExamType.getSelectedItem(),
                            textfieldEmail.getText(),
                            textfieldPassword.getText(),
                            textfieldWirePusherID.getText(),
                            Integer.parseInt(textfieldPoolingTime.getText()),
                            checkNextMonth.isSelected()
                    );
                }
            });

            searchThread.start();
        });

        /**
         * Add actions for clearing the log file
         */
        clearLogsButton.addActionListener(ae -> {
            Thread clearLogsThread = new Thread(() -> {
                clearLogs();
                textAreaLogs.setText("");
                prevLogListSize = 0;
            });
            clearLogsThread.start();
        });
    }

    private void createLogsIfNotExist() {
        String today = LocalDate.now().toString();
        try {
            Files.readString(Path.of("logs/" + today + "-app.log"));
        } catch (IOException e) {
            try {
                Files.writeString(Path.of("logs/" + today + "-app.log"), "");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * <pre>
     * Key point for the application responsible for launching checker of free terms
     * </pre>
     *
     * @param cities      array of cities passed from UI as Miasto1, Miasto2 etc.
     * @param category    category passed from UI e.g. "B"
     * @param examType    teoria or praktyka String passed from UI
     * @param email       email used as login on info-car passed from UI
     * @param password    password for emmail on info-car passed from UI
     * @param poolingTime interval between running search again
     */
    private void checkFreeTerms(String[] cities, String category, String examType, String email, String
            password, String wirePusherId, int poolingTime, boolean checkNextMonth) {
        String citiesSingleString = String.join(",", cities);

        //logs preparation
        clearLogs();
        ActionListener logAction = timerActionLogs();
        new Timer(LOGS_DELAY, logAction).start();


        String poolingTimeString = String.valueOf(poolingTime);
        ConfigurationManager.setTermCheckerPropertyValue("email", email);
        ConfigurationManager.setTermCheckerPropertyValue("category", category);
        ConfigurationManager.setTermCheckerPropertyValue("password", password);
        ConfigurationManager.setTermCheckerPropertyValue("examType", examType);
        ConfigurationManager.setTermCheckerPropertyValue("category", category);
        ConfigurationManager.setTermCheckerPropertyValue("cities", citiesSingleString);
        ConfigurationManager.setWirePusherPropertyValue("deviceId", wirePusherId);
        ConfigurationManager.setTermCheckerPropertyValue("poolingTime", poolingTimeString);
        ConfigurationManager.setTermCheckerPropertyValue("checkNextMonth", String.valueOf(checkNextMonth));

        TestNG testSuite = new TestNG();
        testSuite.setTestClasses(new Class[]{WordTest.class});
        testSuite.addListener(new TestSuiteListener());
        testSuite.run();
        searchThread.stop();

    }

    /**
     * Method overwrriting file with todays logs with empty String
     */
    private void clearLogs() {
        try {
            String today = LocalDate.now().toString();
            Files.writeString(Path.of("logs/" + today + "-app.log"), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Just to secure that even if configuration contains less than 3 cities
     * UI will get array of 3 strings.
     *
     * @return
     */
    private String[] loadCitiesForUI() {
        String[] citiesFromConfig = ConfigurationManager.getTermCheckerPropertyValue("cities").split(",");
        String[] citiesForUI = new String[3];
        for (int i = 0; i < citiesFromConfig.length; i++) {
            citiesForUI[i] = citiesFromConfig[i];
        }
        return citiesForUI;
    }

    private NumberFormatter getIntegerFormatter() {
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);

        return formatter;
    }

    private ActionListener timerActionLogs() {
        return logAction -> {
            try {
                String today = LocalDate.now().toString();
                List<String> logList = Files.readAllLines(Path.of("logs/" + today + "-app.log"));
                if (logList.size() > prevLogListSize) {
                    logs = "";
                    for (String s : logList) {
                        logs = logs + s + "\n";
                    }
                    textAreaLogs.setText(logs);
                    scroll.updateUI();
                    prevLogListSize = logList.size();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        };
    }
}