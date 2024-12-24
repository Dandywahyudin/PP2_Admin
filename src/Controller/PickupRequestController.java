package Controller;

import Database.DatabaseConnection;
import Model.PickupRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PickupRequestController {

    private final Connection connection;

    public PickupRequestController() {
        this.connection = DatabaseConnection.getConnection();
    }

    // CREATE: Tambah permintaan baru
    public void addRequest(PickupRequest request) throws SQLException {
        String query = "INSERT INTO pickup_requests (request_id, user_id, courier_id, status, points) " +
                "VALUES (UUID(), ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, request.getUserId());
            statement.setString(2, request.getCourierId());
            statement.setString(3, request.getStatus());
            statement.setInt(4, request.getPoints());
            statement.executeUpdate();
        }
    }

    // READ: Ambil semua data permintaan
    public List<PickupRequest> getAllRequests() throws SQLException {
        List<PickupRequest> requests = new ArrayList<>();
        String query = "SELECT * FROM pickup_requests";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                PickupRequest request = new PickupRequest(
                        resultSet.getString("request_id"),
                        resultSet.getString("user_id"),
                        resultSet.getString("courier_id"),
                        resultSet.getString("status"),
                        resultSet.getInt("points")
                );
                requests.add(request);
            }
        }
        return requests;
    }

    // UPDATE: Ubah data permintaan berdasarkan request_id
    public void updateRequest(String requestId, String newStatus, int newPoints) throws SQLException {
        String query = "UPDATE pickup_requests SET status = ?, points = ? WHERE request_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newStatus);
            statement.setInt(2, newPoints);
            statement.setString(3, requestId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("No request found with ID: " + requestId);
            }
        }
    }

    // DELETE: Hapus data permintaan berdasarkan request_id
    public void deleteRequest(String requestId) throws SQLException {
        String query = "DELETE FROM pickup_requests WHERE request_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, requestId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("No request found with ID: " + requestId);
            }
        }
    }
}
