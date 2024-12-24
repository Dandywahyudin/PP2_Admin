package View;

import Controller.PickupRequestController;
import Model.PickupRequest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DashboardFrame {
    private JFrame frame;
    private PickupRequestController controller;
    private DefaultTableModel tableModel;
    private JTable table;

    public DashboardFrame() {
        this.controller = new PickupRequestController();
        initializeUI();
        loadData(); // Load data awal
    }

    private void loadTableData() {
        try {
            tableModel.setRowCount(0); // Bersihkan data di tabel

            // Ambil data dari database
            List<PickupRequest> requests = controller.getAllRequests();
            for (PickupRequest request : requests) {
                Object[] rowData = {
                        request.getRequestId(),
                        request.getUserId(),
                        request.getCourierId(),
                        request.getStatus(),
                        request.getPoints()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to load data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeUI() {
        frame = new JFrame("Dashboard - E-Waste Management");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Judul
        JLabel lblTitle = new JLabel("Dashboard", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(lblTitle, BorderLayout.NORTH);

        // Tabel untuk menampilkan data
        tableModel = new DefaultTableModel(new String[]{"Request ID", "User ID", "Courier ID", "Status", "Points"}, 0);
        table = new JTable(tableModel); // Gunakan variabel kelas
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panel CRUD
        JPanel crudPanel = new JPanel(new FlowLayout());
        JButton btnCreate = new JButton("Create");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");

        crudPanel.add(btnCreate);
        crudPanel.add(btnUpdate);
        crudPanel.add(btnDelete);

        frame.add(crudPanel, BorderLayout.SOUTH);

        // Tombol kembali ke menu utama
        JButton btnBack = new JButton("Back to Main Menu");
        btnBack.addActionListener(e -> {
            frame.dispose();
            new MainFrame(); // Membuka Main Menu
        });
        crudPanel.add(btnBack);

        // Event handling untuk CRUD
        btnCreate.addActionListener(e -> createRequest());
        btnUpdate.addActionListener(e -> updateRequest());
        btnDelete.addActionListener(e -> deleteRequest());

        frame.setVisible(true);
    }

    private void loadData() {
        try {
            tableModel.setRowCount(0); // Hapus data lama
            for (PickupRequest request : controller.getAllRequests()) {
                tableModel.addRow(new Object[]{
                        request.getRequestId(),
                        request.getUserId(),
                        request.getCourierId(),
                        request.getStatus(),
                        request.getPoints()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to load data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createRequest() {
        JTextField requestIdField = new JTextField();
        JTextField userIdField = new JTextField();
        JTextField courierIdField = new JTextField();
        JTextField statusField = new JTextField();
        JTextField pointsField = new JTextField();

        Object[] fields = {
                "Request ID:", requestIdField,
                "User ID:", userIdField,
                "Courier ID:", courierIdField,
                "Status:", statusField,
                "Points:", pointsField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Create New Request", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String requestId = requestIdField.getText().trim();
                String userId = userIdField.getText().trim();
                String courierId = courierIdField.getText().trim();
                String status = statusField.getText().trim();
                int points = Integer.parseInt(pointsField.getText().trim());

                // Validasi
                if (requestId.isEmpty() || userId.isEmpty() || courierId.isEmpty() || status.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Simpan data ke database
                PickupRequest request = new PickupRequest(requestId, userId, courierId, status, points);
                controller.addRequest(request);

                JOptionPane.showMessageDialog(frame, "Request added successfully!");
                loadTableData(); // Refresh tabel
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Points must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Failed to add request: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateRequest() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a request to update", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ambil data dari tabel berdasarkan baris yang dipilih
        String requestId = (String) table.getValueAt(selectedRow, 0); // Kolom 0: Request ID
        String userId = (String) table.getValueAt(selectedRow, 1);   // Kolom 1: User ID
        String courierId = (String) table.getValueAt(selectedRow, 2); // Kolom 2: Courier ID
        String status = (String) table.getValueAt(selectedRow, 3);    // Kolom 3: Status
        String points = table.getValueAt(selectedRow, 4).toString();  // Kolom 4: Points

        // Buat JTextField untuk pengeditan
        JTextField userIdField = new JTextField(userId);
        JTextField courierIdField = new JTextField(courierId);
        JTextField statusField = new JTextField(status);
        JTextField pointsField = new JTextField(points);

        // Tampilkan dialog input untuk mengedit data
        Object[] fields = {
                "User ID:", userIdField,
                "Courier ID:", courierIdField,
                "Status:", statusField,
                "Points:", pointsField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Update Request", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                // Validasi input
                if (userIdField.getText().trim().isEmpty() ||
                        courierIdField.getText().trim().isEmpty() ||
                        statusField.getText().trim().isEmpty() ||
                        pointsField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int pointsValue;
                try {
                    pointsValue = Integer.parseInt(pointsField.getText().trim());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(frame, "Points must be a number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update data di database
                PickupRequest updatedRequest = new PickupRequest(
                        requestId,
                        userIdField.getText().trim(),
                        courierIdField.getText().trim(),
                        statusField.getText().trim(),
                        pointsValue
                );

                controller.updateRequest(updatedRequest);

                // Perbarui tabel setelah data diupdate
                loadTableData();
                JOptionPane.showMessageDialog(frame, "Request updated successfully!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Failed to update request: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteRequest() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a request to delete", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String requestId = (String) table.getValueAt(selectedRow, 0);
        int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this request?", "Delete Request", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                controller.deleteRequest(requestId);
                loadTableData(); // Refresh data
                JOptionPane.showMessageDialog(frame, "Request deleted successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Failed to delete request: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
