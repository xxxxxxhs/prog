package Interface;

import CollectionClasses.Movie;
import Managers.Client;
import Managers.UTF8Control;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedList;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainFrame extends JFrame {

    private ResourceBundle messages;
    private JComboBox<String> languageSelector, filterSelector;
    private JLabel movieCountLabel, filterLabel;
    private JLabel usernameLabel;
    private JTable movieTable;
    private VisualizationWindow visualizationWindow;
    private MovieTableModel movieTableModel;
    private LinkedList<Movie> collection = new LinkedList<>();
    private JButton addBtn, updateBtn, visualizeBtn, commandsBtn, warnBtn;
    private int languageIndex = 0;
    private String username, hashPassword;
    private FrameManager frameManager;
    private Client client;
    private Integer movieTableCount = 0;

    public MainFrame(String username, String hashPassword, Client client, FrameManager frameManager) {
        this.username = username;
        this.client = client;
        this.hashPassword = hashPassword;
        this.frameManager = frameManager;
        // Загрузка начального языкового пакета (русский язык)
        loadResourceBundle(new Locale("ru", "RU"));
        visualizationWindow = new VisualizationWindow(collection, username, hashPassword, client, messages, frameManager);
    }

    private void loadResourceBundle(Locale locale) {
        messages = ResourceBundle.getBundle("Resources.MessagesBundle", locale, new UTF8Control());
    }

    public void initializeUI() {
        setTitle(messages.getString("title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 5, 10));

        movieTableModel = new MovieTableModel(ResourceBundle.getBundle(messages.getBaseBundleName(), Locale.getDefault()));
        movieTable = new JTable(movieTableModel);

        JScrollPane scrollPane = new JScrollPane(movieTable);
        add(scrollPane, BorderLayout.CENTER);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width / 2 + 100, screenSize.height / 2);
        setLocationRelativeTo(null);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createBottomPanel(), BorderLayout.SOUTH);
        setVisible(true);
    }

    public void updateTable(LinkedList<Movie> movies) {
        movieTableModel.setMovies(movies);
        movieTableCount = movies.size();
        this.collection = movies;
        updateMovieCountLabel();
        movieTableModel.filterTable(filterSelector.getSelectedIndex());
        visualizationWindow.updateMovies(movies);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        movieCountLabel = new JLabel(messages.getString("movie_count") + " " + movieTableCount.toString());
        movieCountLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        usernameLabel = new JLabel(username);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        String[] languages = {messages.getString("language_ru"),
                messages.getString("language_no"),
                messages.getString("language_hu"),
                messages.getString("language_es")};

        String[] filters = {messages.getString("id"),
                messages.getString("name"),
                messages.getString("x-cor"),
                messages.getString("y-cor"),
                messages.getString("oscars_count"),
                messages.getString("golden_palm_count"),
                messages.getString("total_box_office"),
                messages.getString("creation_date"),
                messages.getString("mpaa_rating"),
                messages.getString("screenwriter"),
                messages.getString("location_name")};

        languageSelector = new JComboBox<>(languages);
        languageSelector.addActionListener(e -> changeLanguage(languageSelector.getSelectedIndex()));
        filterSelector = new JComboBox<>(filters);
        filterSelector.addActionListener(e -> movieTableModel.filterTable(filterSelector.getSelectedIndex()));
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterLabel = new JLabel(messages.getString("filter_by"));
        filterLabel.setBorder(new EmptyBorder(0, 5, 0, 0));
        leftPanel.add(movieCountLabel);
        leftPanel.add(filterLabel);
        leftPanel.add(filterSelector);

        topPanel.add(leftPanel, BorderLayout.WEST);
        usernameLabel.setHorizontalAlignment(JLabel.CENTER);
        topPanel.add(usernameLabel, BorderLayout.CENTER);
        topPanel.add(languageSelector, BorderLayout.EAST);
        return topPanel;
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
        visualizationWindow.changeLanguage(messages);
        movieTableModel.updateLanguage(messages);
    }

    private void updateTexts() {
        // Обновление текстовых элементов после смены языка
        setTitle(messages.getString("title"));
        updateMovieCountLabel();
        updateFilterlabel();
        addBtn.setText(messages.getString("add"));
        updateBtn.setText(messages.getString("update"));
        visualizeBtn.setText(messages.getString("visualize"));
        commandsBtn.setText(messages.getString("commands"));


        // Обновляем выпадающий список языков
        languageSelector.setModel(new DefaultComboBoxModel<>(
                new String[] {
                        messages.getString("language_ru"),
                        messages.getString("language_no"),
                        messages.getString("language_hu"),
                        messages.getString("language_es")
                }
        ));

        filterSelector.setModel(new DefaultComboBoxModel<>(
                new String[] {
                        messages.getString("id"),
                        messages.getString("name"),
                        messages.getString("x-cor"),
                        messages.getString("y-cor"),
                        messages.getString("oscars_count"),
                        messages.getString("golden_palm_count"),
                        messages.getString("total_box_office"),
                        messages.getString("creation_date"),
                        messages.getString("mpaa_rating"),
                        messages.getString("screenwriter"),
                        messages.getString("location_name")
                }
        ));
        // Установка выбранного элемента селектора, если требуется сохранять выбранный язык при смене
        languageSelector.setSelectedIndex(languageIndex);
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        warnBtn = createWarnButton();
        addBtn = createAddButton();
        updateBtn = createUpdateButton();
        visualizeBtn = new JButton(messages.getString("visualize"));
        visualizeBtn.addActionListener(e -> visualizationWindow.setVisible(true));
        commandsBtn = createCommandsButton();

        bottomPanel.add(warnBtn);
        bottomPanel.add(addBtn);
        bottomPanel.add(updateBtn);
        bottomPanel.add(visualizeBtn);
        bottomPanel.add(commandsBtn);

        return bottomPanel;
    }
    private JButton createAddButton() {
        JButton addButton = new JButton(messages.getString("add"));
        addButton.addActionListener(e -> showAddMovieDialog());
        return addButton;
    }

    private JButton createUpdateButton() {
        JButton updateButton = new JButton(messages.getString("update"));
        updateButton.addActionListener(e -> showUpdateMovieDialog());
        return updateButton;
    }

    private JButton createCommandsButton() {
        JButton commandsButton = new JButton(messages.getString("commands"));
        commandsButton.addActionListener(e -> showCommandsDialog());
        return commandsButton;
    }
    private JButton createWarnButton() {
        JButton warnButton = new JButton(messages.getString("warn"));
        warnButton.addActionListener(e -> showWarnFrame());
        return warnButton;
    }
    private void showWarnFrame() {
        WarnFrame warnFrame = new WarnFrame(messages);
        warnFrame.setVisible(true);
    }

    private void showUpdateMovieDialog() {
        UpdateMovieDialog updateDialog = new UpdateMovieDialog(this, messages, username, hashPassword, client, frameManager);
        updateDialog.setVisible(true);
    }
    private void showAddMovieDialog() {
        AddMovieDialog dialog = new AddMovieDialog(this, messages, username, hashPassword, client, frameManager);
        dialog.setVisible(true);
    }

    private void showCommandsDialog() {
        CommandsDialog commandsDialog = new CommandsDialog(this, messages, username, hashPassword, client, frameManager);
        commandsDialog.setVisible(true);
    }
    private void updateMovieCountLabel() {
        movieCountLabel.setText(messages.getString("movie_count") + " " + movieTableCount.toString());
    }
    private void updateFilterlabel() {
        filterLabel.setText(messages.getString("filter_by"));
    }
    public LinkedList<Movie> getCollection() {return collection;}
}
