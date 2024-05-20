package Interface;

import CollectionClasses.Movie;
import Managers.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;

public class VisualizationWindow extends JFrame {
    private LinkedList<Movie> movies;
    VisualizationPanel panel;
    private JButton updateBtn;
    private JPanel bottomPanel;
    private Client client;
    private ResourceBundle messages;
    private FrameManager frameManager;
    private String username, hashPassword;

    public VisualizationWindow(LinkedList<Movie> movies, String username, String hashPassword, Client client, ResourceBundle messages, FrameManager frameManager) {
        this.movies = movies;
        this.username = username;
        this.hashPassword = hashPassword;
        this.client = client;
        this.frameManager = frameManager;
        this.messages = messages;
        initializeUI();
    }

    private void initializeUI() {
        setTitle(messages.getString("visualize"));
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // Измените на HIDE_ON_CLOSE

        panel = new VisualizationPanel(movies);
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        updateBtn = createUpdateButton();
        bottomPanel.add(updateBtn);
        add(panel);
        add(bottomPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false); // Скрытие окна вместо закрытия
            }
        });
    }
    private JButton createUpdateButton() {
        JButton updateButton = new JButton(messages.getString("update"));
        updateButton.addActionListener(e -> showUpdateMovieDialog());
        return updateButton;
    }
    private void showUpdateMovieDialog() {
        UpdateMovieDialog updateDialog = new UpdateMovieDialog(this, messages, username, hashPassword, client, frameManager);
        updateDialog.setVisible(true);
    }
    public void updateMovies(LinkedList<Movie> movies) {
        this.movies = movies;
        panel.updateMovies(movies);
        //repaint(); // Перерисовка с новыми данными
    }

    public void changeLanguage(ResourceBundle messages) {
        setTitle(messages.getString("visualize"));
        updateBtn.setText(messages.getString("update"));
        repaint();
    }
    private static class VisualizationPanel extends JPanel {
        private LinkedList<Movie> movies;
        private Map<Movie, Point> targetPositions;
        private Map<Movie, Point> currentPositions;
        private Map<Movie, Integer> currentAngles;
        private Timer animationTimer;
        public VisualizationPanel(LinkedList<Movie> movies) {
            this.movies = movies;
            setPreferredSize(new Dimension(700, 600));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    handleMouseClick(e);
                }
            });
        }
        private void handleMouseClick(MouseEvent e) {
            int mx = e.getX();
            int my = e.getY();
            for (Movie movie : movies) {
                int x = (int) movie.getCoordinates().getX() + 350;  // Смещение для нормализации координат
                int y = 300 - (int) movie.getCoordinates().getY(); // Смещение для нормализации координат
                if (Math.sqrt(Math.pow(x - mx, 2) + Math.pow(y - my, 2)) <= 10) {
                    // Попадание в радиус 10 пикселей от центра иконки
                    JOptionPane.showMessageDialog(this, movie.toString(), "details", JOptionPane.INFORMATION_MESSAGE);
                    break; // Выходим из цикла после обработки первого попавшегося фильма
                }
            }
        }
        private void initAnimation() {
            animationTimer = new Timer(10, e -> animateMovies());
            animationTimer.start();
        }

        private void animateMovies() {
            boolean allInPlace = true;
            for (Movie movie : movies) {
                Point target = targetPositions.get(movie);
                Point current = currentPositions.get(movie);
                if (current.x < target.x) {
                    current.x += Math.min(5, target.x - current.x); // Move 5 pixels each frame or less if close to target
                    currentAngles.put(movie, (currentAngles.get(movie) + 10) % 360); // Rotate
                    allInPlace = false;
                }
            }
            if (allInPlace) {
                animationTimer.stop();
            }
            repaint();
        }
        public void updateMovies(LinkedList<Movie> movies) {
            this.movies = new LinkedList<>(movies);
            initAnimationTargets();  // Initialize targets for the animation
            if (animationTimer != null) {
                animationTimer.stop();
            }
            initAnimation();  // Start the animation
        }
        private void initAnimationTargets() {
            targetPositions = new HashMap<>();
            currentPositions = new HashMap<>();
            currentAngles = new HashMap<>();
            for (Movie movie : movies) {
                int targetX = (int) movie.getCoordinates().getX() + 350;
                int targetY = 300 - (int) movie.getCoordinates().getY();
                targetPositions.put(movie, new Point(targetX, targetY));
                currentPositions.put(movie, new Point(0, targetY)); // Start from the left edge
                currentAngles.put(movie, 0);
            }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawAxis(g);
            movies.forEach(movie -> {
                int x = currentPositions.get(movie).x;
                int y = currentPositions.get(movie).y;
                int angle = currentAngles.get(movie);
                Color color = getColorForMovie(movie);
                drawFilmReel(g, x, y, angle, color);
            });
        }

        private void drawAxis(Graphics g) {
            // Рисование оси X
            g.drawLine(50, 300, 650, 300);
            // Стрелка для оси X
            g.drawLine(650, 300, 640, 290);
            g.drawLine(650, 300, 640, 310);
            g.drawString("X", 660, 310);

            // Рисование оси Y
            g.drawLine(350, 50, 350, 550);
            // Стрелка для оси Y
            g.drawLine(350, 50, 340, 60);
            g.drawLine(350, 50, 360, 60);
            g.drawString("Y", 360, 40);

            // Добавление отметок на оси
            for (int i = 20; i < 300; i += 40) {
                    // Отметки на оси X
                g.drawLine(350 + i, 295, 350 + i, 305);
                g.drawString(String.valueOf(i), 350 + i - 5, 320);
                g.drawLine(350 - i, 295, 350 - i, 305);
                g.drawString(String.valueOf(-i), 350 - i - 15, 320);
                    // Отметки на оси Y
                if (i != 260) {
                    g.drawLine(345, 300 + i, 355, 300 + i);
                    g.drawString(String.valueOf(-i), 360, 300 + i + 15);
                    g.drawLine(345, 300 - i, 355, 300 - i);
                    g.drawString(String.valueOf(i), 360, 300 - i + 5);
                }
            }
        }
/*
        private void drawMovies(Graphics g) {
            for (Movie movie : movies) {
                int x = (int) movie.getCoordinates().getX() + 350; // Adjusting coordinate system
                int y = 300 - (int) movie.getCoordinates().getY(); // Adjusting coordinate system
                Color color = getColorForMovie(movie);
                g.setColor(color);
                drawFilmReel(g, x, y);
            }
        }
*/
        private void drawFilmReel(Graphics g, int x, int y, int angle, Color color) {
            int radius = 10;  // Основной радиус
            int smallRadius = 2;  // Радиус маленьких отверстий
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.rotate(Math.toRadians(angle), x, y);
            g2d.setColor(color);
            g2d.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
            g2d.setColor(Color.WHITE);
            for (int i = 0; i < 360; i += 45) {
                double rad = Math.toRadians(i);
                int smallX = x + (int) ((radius - smallRadius) * Math.cos(rad));
                int smallY = y + (int) ((radius - smallRadius) * Math.sin(rad));
                g2d.fillOval(smallX - smallRadius, smallY - smallRadius, 2 * smallRadius, 2 * smallRadius);
            }
            g2d.setColor(Color.WHITE);
            g2d.fillOval(x - 3, y - 3, 6, 6);
            g2d.dispose();
        }
        private Color getColorForMovie(Movie movie) {
            // This method should return different colors based on the movie's creator or other properties
            if (movie.getCreatorName().hashCode() % 6 == 0) return Color.BLUE;
            else if (movie.getCreatorName().hashCode() % 6 == 1) return Color.GREEN;
            else if (movie.getCreatorName().hashCode() % 6 == 2) return Color.RED;
            else if (movie.getCreatorName().hashCode() % 6 == 3) return Color.YELLOW;
            else if (movie.getCreatorName().hashCode() % 6 == 4) return Color.BLACK;
            else if (movie.getCreatorName().hashCode() % 6 == 5) return Color.MAGENTA;
            return Color.GRAY;
        }
    }
}
