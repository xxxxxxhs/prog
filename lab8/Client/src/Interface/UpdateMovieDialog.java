package Interface;

import CollectionClasses.*;
import CollectionClasses.Color;
import Exceptions.IncorrectValueException;
import Managers.Client;
import Managers.CommandResolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class UpdateMovieDialog extends JDialog{

    private ResourceBundle messages;
    private JTextField nameField, screenwriterNameField, passportIDField, locationNameField;
    private JTextField xField, yField, oscarsCountField, goldenPalmCountField, totalBoxOfficeField, locationXField, locationYField, locationZField, idField;
    private JComboBox<MpaaRating> mpaaRatingComboBox;
    private JComboBox<Color> hairColorComboBox, eyeColorComboBox;
    private FrameManager frameManager;
    private Client client;
    private String username, hashPassword;

    public UpdateMovieDialog(Frame owner, ResourceBundle messages, String username, String hashPassword, Client client, FrameManager frameManager) {
        super(owner, messages.getString("update"), true);
        this.messages = messages;
        this.username = username;
        this.client = client;
        this.frameManager = frameManager;
        this.hashPassword = hashPassword;
        initializeUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;

        // Поля для ввода данных
        addField(gbc, "id", idField = new JTextField(10));
        addField(gbc, "name", nameField = new JTextField(20));
        addField(gbc, "x-cor", xField = new JTextField(10));
        addField(gbc, "y-cor", yField = new JTextField(10));
        addField(gbc, "oscars_count", oscarsCountField = new JTextField(10));
        addField(gbc, "golden_palm_count", goldenPalmCountField = new JTextField(10));
        addField(gbc, "total_box_office", totalBoxOfficeField = new JTextField(10));
        addField(gbc, "mpaa_rating", mpaaRatingComboBox = new JComboBox<>(MpaaRating.values()));
        addField(gbc, "screenwriter_name", screenwriterNameField = new JTextField(20));
        addField(gbc, "passport_id", passportIDField = new JTextField(20));
        addField(gbc, "hair_color", hairColorComboBox = new JComboBox<>(Color.values()));
        addField(gbc, "eye_color", eyeColorComboBox = new JComboBox<>(Color.values()));
        addField(gbc, "location_name", locationNameField = new JTextField(20));
        addField(gbc, "location_x", locationXField = new JTextField(10));
        addField(gbc, "location_y", locationYField = new JTextField(10));
        addField(gbc, "location_z", locationZField = new JTextField(10));

        // Кнопка добавления
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton updateButton = new JButton(messages.getString("update"));
        updateButton.addActionListener(this::onUpdateButtonClicked);
        add(updateButton, gbc);
    }

    private void addField(GridBagConstraints gbc, String labelKey, Component field) {
        add(new JLabel(messages.getString(labelKey)), gbc);
        gbc.gridx = 1;
        add(field, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }

    private void onUpdateButtonClicked(ActionEvent e) {
        if (validateInputFields()) {
            try {
                Movie movie = createMovieFromInputFields();
                frameManager.stopPolling();
                SwingWorker<String, Void> worker = new SwingWorker<>() {
                    @Override
                    protected String doInBackground() throws Exception {
                        return client.send(CommandResolver.resolve(("update " + idField.getText()).split(" "), movie, username, hashPassword)).getAnswer();
                    }

                    @Override
                    protected void done() {
                        try {
                            String result = get();
                            JOptionPane.showMessageDialog(UpdateMovieDialog.this, result);
                            clearInputFields(); // Очистить поля после успешного обновления
                        } catch (InterruptedException | ExecutionException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(UpdateMovieDialog.this, "Failed to update movie: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        } finally {
                            frameManager.startPolling();
                        }
                    }
                };
                worker.execute();
            } catch (IncorrectValueException r) {r.printStackTrace();}
        } else {
            JOptionPane.showMessageDialog(this, messages.getString("input_error"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private boolean validateInputFields() {
        try {
            return Movie.staticValidate(
                    nameField.getText(),
                    Float.parseFloat(xField.getText()),
                    Double.parseDouble(yField.getText()),
                    Integer.parseInt(oscarsCountField.getText()),
                    Long.parseLong(goldenPalmCountField.getText()),
                    Float.parseFloat(totalBoxOfficeField.getText()),
                    (MpaaRating) mpaaRatingComboBox.getSelectedItem(),
                    screenwriterNameField.getText(),
                    passportIDField.getText(),
                    (Color) hairColorComboBox.getSelectedItem(),
                    (Color) eyeColorComboBox.getSelectedItem(),
                    Double.parseDouble(locationXField.getText()),
                    Float.parseFloat(locationYField.getText()),
                    Long.parseLong(locationZField.getText()),
                    locationNameField.getText());
        } catch (NumberFormatException ex) {
            return false; // Возвращает false, если конвертация не удалась
        }
    }

    private void clearInputFields() {
        idField.setText("");
        nameField.setText("");
        xField.setText("");
        yField.setText("");
        oscarsCountField.setText("");
        goldenPalmCountField.setText("");
        totalBoxOfficeField.setText("");
        mpaaRatingComboBox.setSelectedIndex(0);
        screenwriterNameField.setText("");
        passportIDField.setText("");
        hairColorComboBox.setSelectedIndex(0);
        eyeColorComboBox.setSelectedIndex(0);
        locationXField.setText("");
        locationYField.setText("");
        locationZField.setText("");
        locationNameField.setText("");
    }
    private Movie createMovieFromInputFields() throws IncorrectValueException {
        // Создаем объект Movie из данных формы
        return new Movie(
                nameField.getText(),
                new Coordinates(
                        Float.parseFloat(xField.getText()),
                        Double.parseDouble(yField.getText())
                ),
                parseInteger(oscarsCountField.getText()),
                Long.parseLong(goldenPalmCountField.getText()),
                parseFloat(totalBoxOfficeField.getText()),
                (MpaaRating) mpaaRatingComboBox.getSelectedItem(),
                new Person(
                        screenwriterNameField.getText(),
                        passportIDField.getText(),
                        (Color) hairColorComboBox.getSelectedItem(),
                        (Color) eyeColorComboBox.getSelectedItem(),
                        new Location(
                                Double.parseDouble(locationXField.getText()),
                                Float.parseFloat(locationYField.getText()),
                                Long.parseLong(locationZField.getText()),
                                locationNameField.getText()
                        )
                ),
                username
        );
    }

    private Integer parseInteger(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        return Integer.parseInt(text.trim());
    }

    private Float parseFloat(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        return Float.parseFloat(text.trim());
    }
}
