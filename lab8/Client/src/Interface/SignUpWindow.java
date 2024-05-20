package Interface;

import Managers.Client;
import Managers.Encryptor;
import Managers.Response;
import Managers.UTF8Control;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SignUpWindow extends JFrame {

    private ResourceBundle messages;
    private JComboBox<String> languageSelector;
    private int languageIndex = 0;
    private JLabel titleLabel, usernameLabel, passwordLabel;
    private JTextField usernameField, passwordField;
    private JButton signUpButton;
    private RegistrationListener listener;
    private Client client;

    public SignUpWindow(Client client) {
        loadResourceBundle(new Locale("ru", "RU"));
        this.client = client;
    }
    public void setRegistrationListener(RegistrationListener registrationListener) {
        this.listener = registrationListener;
    }
    public void initializeUI() {
        setTitle(messages.getString("authorization"));
        setLayout(new BorderLayout());
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 5, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width / 4, screenSize.height / 2);
        setLocationRelativeTo(null);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        String[] languages = {messages.getString("language_ru"),
                messages.getString("language_no"),
                messages.getString("language_hu"),
                messages.getString("language_es")};

        languageSelector = new JComboBox<>(languages);
        languageSelector.addActionListener(e -> changeLanguage(languageSelector.getSelectedIndex()));
        topPanel.add(languageSelector, BorderLayout.EAST);
        return topPanel;
    }
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());  // Используем GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;  // Заполнение по горизонтали
        gbc.insets = new Insets(5, 5, 5, 5);  // Отступы
        gbc.weightx = 1;  // Вес по горизонтали для всех компонентов
        gbc.gridwidth = 1;  // Количество занимаемых ячеек в строке по горизонтали

        // Добавление метки и текстового поля для имени пользователя
        gbc.gridx = 0;  // Столбец 0
        gbc.gridy = 0;  // Строка 0
        usernameLabel = new JLabel(messages.getString("username"));
        centerPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;  // Столбец 1
        usernameField = new JTextField(15);
        centerPanel.add(usernameField, gbc);

        // Добавление метки и текстового поля для пароля
        gbc.gridx = 0;  // Столбец 0
        gbc.gridy = 1;  // Строка 1
        passwordLabel = new JLabel(messages.getString("password"));
        centerPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;  // Столбец 1
        passwordField = new JPasswordField(15);
        centerPanel.add(passwordField, gbc);

        return centerPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));  // FlowLayout для центрирования кнопки
        // Создание и добавление кнопки "Sign Up"
        signUpButton = new JButton(messages.getString("sign_up"));
        signUpButton.addActionListener(this::onAuthorizeButtonClicked);
        bottomPanel.add(signUpButton);
        return bottomPanel;
    }

    private void loadResourceBundle(Locale locale) {
        messages = ResourceBundle.getBundle("Resources.MessagesBundle", locale, new UTF8Control());
    }
    private void changeLanguage(int index) {
        Locale locale;
        this.languageIndex = index;
        switch (index) {
            case 0:
                locale = new Locale("ru", "RU");
                break;
            case 1:
                locale = new Locale("no", "NO");
                break;
            case 2:
                locale = new Locale("hu", "HU");
                break;
            case 3:
                locale = new Locale("es", "EC");
                break;
            default:
                throw new IllegalArgumentException("Unknown language index: " + index);
        }
        loadResourceBundle(locale);
        updateTexts();
    }

    private void updateTexts() {
        setTitle(messages.getString("authorization"));
        languageSelector.setModel(new DefaultComboBoxModel<>(
                new String[] {
                        messages.getString("language_ru"),
                        messages.getString("language_no"),
                        messages.getString("language_hu"),
                        messages.getString("language_es")
                }
        ));
        usernameLabel.setText(messages.getString("username"));
        passwordLabel.setText(messages.getString("password"));
        signUpButton.setText(messages.getString("sign_up"));

        languageSelector.setSelectedIndex(languageIndex);
    }
    private void onAuthorizeButtonClicked(ActionEvent e) {
        if (validateInputFields()) {
            try {
                Response response = client.authenticateUser(usernameField.getText(), passwordField.getText());
                String responseText = response.getAnswer();

                if (responseText.equals("Registered as " + usernameField.getText()) ||
                        responseText.equals("Authorized as " + usernameField.getText())) {
                    JOptionPane.showMessageDialog(this, responseText);
                    listener.onRegistrationSuccess(usernameField.getText(), Encryptor.getHash(passwordField.getText()));
                    //this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                } else {
                    JOptionPane.showMessageDialog(this, responseText, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, messages.getString("input_error"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private boolean validateInputFields() {
        if (!usernameField.getText().equals("") && !passwordField.getText().equals("")) return true;
        else return false;
    }
}
