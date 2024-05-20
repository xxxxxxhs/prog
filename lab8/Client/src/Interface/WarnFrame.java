package Interface;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class WarnFrame extends JFrame {
    private ResourceBundle messages;
    private final String picToShow = "/warning.JPG";
    public WarnFrame(ResourceBundle messages) {
        this.messages = messages;
        initializeUI();
        pack();
        setLocation(750, 300);
        displayImage();
    }
    private void initializeUI() {
        setTitle(messages.getString("warn"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
    private void displayImage() {
        // Загрузка изображения
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(picToShow));
        if (originalIcon.getIconWidth() == -1) {
            // Если изображение не найдено, покажем сообщение об ошибке
            JOptionPane.showMessageDialog(this, "Image not found: " + picToShow, "Image Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Масштабируем изображение, сохраняя пропорции
        Image scaledImage = originalIcon.getImage().getScaledInstance(originalIcon.getIconWidth() / 2, originalIcon.getIconHeight() / 2, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Создаем метку для отображения изображения
        JLabel label = new JLabel(scaledIcon);

        // Добавляем метку на панель содержимого
        setContentPane(label);
        pack();  // Адаптируем размер окна под размер метки
    }
}
