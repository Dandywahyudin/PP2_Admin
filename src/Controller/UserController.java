package Controller;

import Database.DatabaseConnection;
import Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserController {
    private Connection connection;

    public UserController() {
        this.connection = DatabaseConnection.getConnection(); // Reuse existing connection
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            User user = new User(
                    resultSet.getString("user_id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("phone_number"),
                    resultSet.getString("address")
            );
            users.add(user);
        }

        return users;
    }

    public void addUser(User user) throws SQLException {
        validateUser(user);

        String query = "INSERT INTO users (user_id, name, email, phone_number, address) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, user.getUserId());
        preparedStatement.setString(2, user.getName());
        preparedStatement.setString(3, user.getEmail());
        preparedStatement.setString(4, user.getPhoneNumber());
        preparedStatement.setString(5, user.getAddress());
        preparedStatement.executeUpdate();
    }

    public void updateUser(User user) throws SQLException {
        if (user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID tidak boleh kosong.");
        }

        validateUser(user);

        String query = "UPDATE users SET name = ?, email = ?, phone_number = ?, address = ? WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getPhoneNumber());
        preparedStatement.setString(4, user.getAddress());
        preparedStatement.setString(5, user.getUserId());
        preparedStatement.executeUpdate();
    }

    public void deleteUser(String userId) throws SQLException {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID tidak boleh kosong.");
        }

        String query = "DELETE FROM users WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, userId);
        preparedStatement.executeUpdate();
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong.");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty() || !isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email tidak valid.");
        }

        if (user.getPhoneNumber() == null || user.getPhoneNumber().trim().isEmpty() || !isValidPhoneNumber(user.getPhoneNumber())) {
            throw new IllegalArgumentException("Nomor telepon tidak valid. Nomor harus terdiri dari 10-15 digit.");
        }

        if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Alamat tidak boleh kosong.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^[0-9]{10,15}$";
        return Pattern.matches(phoneRegex, phoneNumber);
    }
}
