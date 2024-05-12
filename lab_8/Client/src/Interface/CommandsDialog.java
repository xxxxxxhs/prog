package Interface;

import CollectionClasses.Movie;
import Exceptions.IncorrectValueException;
import Managers.Client;
import Managers.CommandResolver;
import Managers.Request;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class CommandsDialog extends JDialog {
    private ResourceBundle messages;
    private LinkedList<Movie> collection;
    private String username, hashPassword;
    private Client client;

    public CommandsDialog(Frame owner, ResourceBundle messages, String username, String hashPassword, LinkedList<Movie> collection, Client client) {
        super(owner, messages.getString("add"), true);
        this.messages = messages;
        this.client = client;
        this.username = username;
        this.hashPassword = hashPassword;
        this.collection = collection;
        initializeUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initializeUI() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));  // Вертикальное упорядочивание компонентов
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Команды с аргументом
        createExecuteScriptCommand(panel, "execute_script");
        createCommandWithArgument(panel, "remove_by_id");
        createCommandWithArgument(panel, "count_by_golden_palm_count");

        // Команды без аргумента
        createCommand(panel, "help");
        createCommand(panel, "clear");
        createCommand(panel, "remove_first");
        createCommand(panel, "print_field_descending_mpaa_rating");
        createCommand(panel, "print_unique_oscars_count");
        createCommand(panel, "head");
        createCommand(panel, "history");

        setContentPane(panel);
    }

    private void createCommandWithArgument(JPanel panel, String commandKey) {
        JPanel commandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        commandPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));  // Универсальные отступы для панели

        JButton commandButton = new JButton(messages.getString(commandKey));
        JTextField argumentField = new JTextField(10);
        commandButton.addActionListener(e -> {
            String argument = argumentField.getText().trim();
            if (!argument.isEmpty()) {
                try {
                    String answer = client.send(CommandResolver.resolve((commandKey + " " + argument).split(" "), null, username, hashPassword)).getAnswer();
                    JOptionPane.showMessageDialog(this, answer);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, messages.getString("argument_required"),
                        messages.getString("error_title"), JOptionPane.ERROR_MESSAGE);
            }
        });
        commandPanel.add(commandButton);
        commandPanel.add(argumentField);
        panel.add(commandPanel);
    }

    private void createCommand(JPanel panel, String commandKey) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));  // Универсальные отступы для панели

        JButton commandButton = new JButton(messages.getString(commandKey));
        commandButton.addActionListener(e -> {
            try {
                String answer = client.send(CommandResolver.resolve(commandKey.split(" "), null, username, hashPassword)).getAnswer();
                JOptionPane.showMessageDialog(this, answer);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttonPanel.add(commandButton);
        panel.add(buttonPanel);
    }

    private void createExecuteScriptCommand(JPanel panel, String commandKey) {
        JPanel commandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        commandPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));  // Универсальные отступы для панели

        JButton commandButton = new JButton(messages.getString(commandKey));
        JTextField argumentField = new JTextField(10);
        commandButton.addActionListener(e -> {
            String argument = argumentField.getText().trim();
            if (!argument.isEmpty()) {
                LinkedList<Request> requests = CommandResolver.executeScript(argument, username, hashPassword);
                String answer = "";
                for (Request rq : requests) {
                    try {
                        answer = answer + (client.send(rq).getAnswer() + "\n");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } JOptionPane.showMessageDialog(this, answer);
            } else {
                JOptionPane.showMessageDialog(this, messages.getString("argument_required"),
                        messages.getString("error_title"), JOptionPane.ERROR_MESSAGE);
            }
        });
        commandPanel.add(commandButton);
        commandPanel.add(argumentField);
        panel.add(commandPanel);
    }
}
