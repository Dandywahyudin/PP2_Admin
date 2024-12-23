package Controller;

import Database.DatabaseConnection;
import Model.PickupRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PickupRequestController {
    public void addRequest(PickupRequest request) throws SQLException {
        String query = "INSERT INTO pickup_requests (request_id, user_id, courier_id, status, points) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, request.getRequestId());
            stmt.setString(2, request.getUserId());
            stmt.setString(3, request.getCourierId());
            stmt.setString(4, request.getStatus());
            stmt.setInt(5, request.getPoints());
            stmt.executeUpdate();
        }
    }

    public void updateRequest(PickupRequest request) throws SQLException {
        String query = "UPDATE pickup_requests SET user_id = ?, courier_id = ?, status = ?, points = ? WHERE request_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, request.getUserId());
            stmt.setString(2, request.getCourierId());
            stmt.setString(3, request.getStatus());
            stmt.setInt(4, request.getPoints());
            stmt.setString(5, request.getRequestId());
            stmt.executeUpdate();
        }
    }

    public void deleteRequest(String requestId) throws SQLException {
        String query = "DELETE FROM pickup_requests WHERE request_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, requestId);
            stmt.executeUpdate();
        }
    }

    public List<PickupRequest> getAllRequests() throws SQLException {
        String query = "SELECT * FROM pickup_requests";
        List<PickupRequest> requests = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                PickupRequest request = new PickupRequest(
                        rs.getString("request_id"),
                        rs.getString("user_id"),
                        rs.getString("courier_id"),
                        rs.getString("status"),
                        rs.getInt("points")
                );
                requests.add(request);
            }
        }
        return requests;
    }
}