package View;

import Controller.PickupRequestController;
import Model.PickupRequest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RequestFrame extends JPanel {
    private JPanel mainPanel;
    private PickupRequestController controller;
    private DefaultTableModel tableModel;

    public RequestFrame() {
        this.mainPanel = mainPanel;  // Menyimpan panel utama dari MainFrame
        this.controller = new PickupRequestController();
        initializeUI();
        loadData(null);
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JLabel lblRequest = new JLabel("Permintaan Penjemputan Sampah", SwingConstants.CENTER);
        lblRequest.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblRequest, BorderLayout.NORTH);  // Menambahkan JLabel ke panel ini

        tableModel = new DefaultTableModel(
                new String[]{
                        "ID Permintaan",
                        "ID Pengguna",
                        "ID Kurir",
                        "Status",
                        "Poin",
                        "Jenis Sampah"
                },
                0
        );
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);  // Menambahkan JTable ke panel

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnViewAll = new JButton("View All Requests");
        JButton btnViewCompleted = new JButton("View Completed Requests");
        JButton btnViewPending = new JButton("View Pending Requests");
        JButton btnViewOngoing = new JButton("View Ongoing Requests");
        JButton btnTrackRequest = new JButton("Track Request");

        buttonPanel.add(btnViewAll);
        buttonPanel.add(btnViewCompleted);
        buttonPanel.add(btnViewPending);
        buttonPanel.add(btnViewOngoing);
        buttonPanel.add(btnTrackRequest);

        add(buttonPanel, BorderLayout.SOUTH);  // Menambahkan button panel ke panel utama

        // Event Handling
        btnViewAll.addActionListener(e -> loadData(null));
        btnViewCompleted.addActionListener(e -> loadData("Completed"));
        btnViewPending.addActionListener(e -> loadData("Pending"));
        btnViewOngoing.addActionListener(e -> loadData("Ongoing"));
        btnTrackRequest.addActionListener(e -> trackRequest());
    }

    private void loadData(String statusFilter) {
        try {
            List<PickupRequest> requests;
            if (statusFilter == null) {
                requests = controller.getAllRequests();
            } else {
                requests = controller.getRequestsByStatus(statusFilter);
            }
            tableModel.setRowCount(0);  // Menghapus data lama
            for (PickupRequest request : requests) {
                tableModel.addRow(new Object[]{
                        request.getRequestId(),
                        request.getUserId(),
                        request.getCourierId(),
                        request.getStatus(),
                        request.getPoints(),
                        request.getWasteType()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void trackRequest() {
        JTextField requestIdField = new JTextField();
        JComboBox<String> userIdComboBox = new JComboBox<>();
        JComboBox<String> courierIdComboBox = new JComboBox<>();
        JTextField wasteTypeField = new JTextField();

        try {
            List<String> userIds = controller.getAllUserIds();
            List<String> courierIds = controller.getAllCourierIds();

            userIdComboBox.addItem(""); // Allow empty selection
            courierIdComboBox.addItem("");

            for (String userId : userIds) {
                userIdComboBox.addItem(userId);
            }
            for (String courierId : courierIds) {
                courierIdComboBox.addItem(courierId);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load users or couriers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Object[] fields = {
                "Request ID:", requestIdField,
                "User ID:", userIdComboBox,
                "Courier ID:", courierIdComboBox,
                "Waste Type:", wasteTypeField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Track Request", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String requestId = requestIdField.getText().trim();
                String userId = (String) userIdComboBox.getSelectedItem();
                String courierId = (String) courierIdComboBox.getSelectedItem();
                String wasteType = wasteTypeField.getText().trim();

                if (requestId.isEmpty() && (userId == null || userId.isEmpty()) && (courierId == null || courierId.isEmpty()) && wasteType.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "At least one field (Request ID, User ID, Courier ID, or Waste Type) must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<PickupRequest> results = controller.trackRequest(requestId, userId, courierId, wasteType);
                tableModel.setRowCount(0);  // Clear the table
                for (PickupRequest request : results) {
                    tableModel.addRow(new Object[]{
                            request.getRequestId(),
                            request.getUserId(),
                            request.getCourierId(),
                            request.getStatus(),
                            request.getPoints(),
                            request.getWasteType()
                    });
                }

                if (results.isEmpty()) {
                    StringBuilder criteria = new StringBuilder();
                    if (!requestId.isEmpty()) criteria.append("Request ID: ").append(requestId).append("; ");
                    if (userId != null && !userId.isEmpty()) criteria.append("User ID: ").append(userId).append("; ");
                    if (courierId != null && !courierId.isEmpty())
                        criteria.append("Courier ID: ").append(courierId).append("; ");
                    if (!wasteType.isEmpty()) criteria.append("Waste Type: ").append(wasteType).append("; ");

                    JOptionPane.showMessageDialog(this, "No matching requests found for the following criteria:\n" + criteria.toString(), "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Failed to track request: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
