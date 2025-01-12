package Controller;

import Database.DatabaseConnection;
import Model.Courier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourierController {

    private void validateCourier(Courier courier) throws IllegalArgumentException {
        if (courier.getName() == null || courier.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong.");
        }
        if (courier.getEmail() == null || !courier.getEmail().matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("Email tidak valid.");
        }
        if (courier.getPhoneNumber() == null || !courier.getPhoneNumber().matches("\\d{10,}")) {
            throw new IllegalArgumentException("Nomor handphone tidak valid. Harus berupa angka dan minimal 10 digit.");
        }
        if (courier.getVehicleNumber() == null || courier.getVehicleNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Nomor kendaraan tidak boleh kosong.");
        }
        if (courier.getSimNumber() == null || !courier.getSimNumber().matches("\\d{12}")) {
            throw new IllegalArgumentException("Nomor SIM tidak valid. Harus berupa angka 12 digit.");
        }
    }

    // Method untuk menambahkan courier dengan validasi
    public void addCourier(Courier courier) throws SQLException {
        // Validasi sebelum eksekusi
        validateCourier(courier);

        String sql = "INSERT INTO couriers (courier_id, name, email, phone_number, vehicle_number, sim_number) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, courier.getCourierId());
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

    // Method untuk mengubah data courier dengan validasi
    public void updateCourier(Courier courier) throws SQLException {
        // Validasi sebelum eksekusi
        validateCourier(courier);

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
        if (courierId == null || courierId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID Courier tidak boleh kosong.");
        }

        String sql = "DELETE FROM couriers WHERE courier_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, courierId);
            stmt.executeUpdate();
        }
    }
}
