package Managers;

import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class SqlUserManager {
    private final Connection connection;
    public SqlUserManager(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean checkUserExists(String username) throws SQLException {
        String sql = "SELECT EXISTS(SELECT 1 FROM users WHERE username = ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        }
        return false;
    }
    public boolean checkPasswordCorrect(String username, String password) throws SQLException, NoSuchAlgorithmException {
        String sql = "SELECT hash_password FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedHashPassword = rs.getString("hash_password");
                return storedHashPassword.equals(password);
            }
        }
        return false;
    }
    public void registerNewUser(String username, String password) {
        String sql = "INSERT INTO users (username, hash_password) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
