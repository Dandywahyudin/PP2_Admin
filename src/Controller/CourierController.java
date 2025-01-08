package Controller;

import Database.DatabaseConnection;
import Model.Courier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourierController {
    // Method untuk menambah courier baru
    public void addCourier(Courier courier) throws SQLException {
        String sql = "INSERT INTO couriers (courier_id, name, email, phone_number, vehicle_number, sim_number) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, courier.getCourierId());  // ID sudah otomatis
            stmt.setString(2, courier.getName());
            stmt.setString(3, courier.getEmail());
            stmt.setString(4, courier.getPhoneNumber());
            stmt.setString(5, courier.getVehicleNumber());
            stmt.setString(6, courier.getSimNumber());

            stmt.executeUpdate();
        }
    }

    // Method untuk mendapatkan semua data courier
    public List<Courier> getAllCouriers() throws SQLException {
        List<Courier> couriers = new ArrayList<>();
        String sql = "SELECT * FROM couriers";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Courier courier = new Courier(
                        rs.getString("courier_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("vehicle_number"),
                        rs.getString("sim_number")
                );
                couriers.add(courier);
            }
        }
        return couriers;
    }

    // Method untuk mengubah data courier
    public void updateCourier(Courier courier) throws SQLException {
        String sql = "UPDATE couriers SET name = ?, email = ?, phone_number = ?, vehicle_number = ?, sim_number = ? WHERE courier_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, courier.getName());
            stmt.setString(2, courier.getEmail());
            stmt.setString(3, courier.getPhoneNumber());
            stmt.setString(4, courier.getVehicleNumber());
            stmt.setString(5, courier.getSimNumber());
            stmt.setString(6, courier.getCourierId());

            stmt.executeUpdate();
        }
    }

    // Method untuk menghapus data courier
    public void deleteCourier(String courierId) throws SQLException {
        String sql = "DELETE FROM couriers WHERE courier_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, courierId);
            stmt.executeUpdate();
        }
    }
}
