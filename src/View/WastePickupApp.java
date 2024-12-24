package View;

import Controller.PickupRequestController;
import Model.PickupRequest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class WastePickupApp {

    private JFrame frame;
    private JTable tableRequests;
    private JLabel lblTotalPoints, lblTotalCollected;
    private DefaultTableModel tableModel;
    private PickupRequestController controller;

    public WastePickupApp() {
        controller = new PickupRequestController();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Admin Dashboard - Waste Pickup Management");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Panel atas untuk tindakan CRUD
        JPanel topPanel = new JPanel(new FlowLayout());
        JButton btnAddRequest = new JButton("Add New Request");
        JButton btnEditRequest = new JButton("Edit Request");
        JButton btnDeleteRequest = new JButton("Delete Request");
        JButton btnViewAllRequests = new JButton("View All Requests");

        topPanel.add(btnAddRequest);
        topPanel.add(btnEditRequest);
        topPanel.add(btnDeleteRequest);
        topPanel.add(btnViewAllRequests);
        frame.add(topPanel, BorderLayout.NORTH);

        // Tabel untuk menampilkan data
        tableModel = new DefaultTableModel(new String[]{"Request ID", "User ID", "Courier ID", "Status", "Points"}, 0);
        tableRequests = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableRequests);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panel bawah untuk informasi total
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        lblTotalCollected = new JLabel("Total Collected: 0 Requests");
        lblTotalPoints = new JLabel("Total Points: 0");
        bottomPanel.add(lblTotalCollected);
        bottomPanel.add(lblTotalPoints);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Event handling
        btnAddRequest.addActionListener(this::addNewRequest);
        btnEditRequest.addActionListener(this::editRequest);
        btnDeleteRequest.addActionListener(this::deleteRequest);
        btnViewAllRequests.addActionListener(this::viewAllRequests);

        frame.setVisible(true);
        viewAllRequests(null);
    }

    private void addNewRequest(ActionEvent e) {
        String userId = JOptionPane.showInputDialog(frame, "Enter User ID:");
        String courierId = JOptionPane.showInputDialog(frame, "Enter Courier ID (optional):");
        String status = JOptionPane.showInputDialog(frame, "Enter Status (Pending/Completed):");
        String pointsInput = JOptionPane.showInputDialog(frame, "Enter Points:");

        try {
            int points = Integer.parseInt(pointsInput);
            if (userId == null || userId.isEmpty() || status == null || status.isEmpty()) {
                showError("User ID and Status are required!");
                return;
            }

            PickupRequest newRequest = new PickupRequest(null, userId, courierId, status, points);
            controller.addRequest(newRequest);
            JOptionPane.showMessageDialog(frame, "New request added successfully!");
            viewAllRequests(null);
        } catch (NumberFormatException ex) {
            showError("Points must be a valid number!");
        } catch (SQLException ex) {
            showError(ex.getMessage());
        }
    }

    private void editRequest(ActionEvent e) {
        int selectedRow = tableRequests.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a request to edit.");
            return;
        }

        String requestId = tableModel.getValueAt(selectedRow, 0).toString();
        String newStatus = JOptionPane.showInputDialog(frame, "Enter New Status (Pending/Completed):");
        String pointsInput = JOptionPane.showInputDialog(frame, "Enter New Points:");

        try {
            int newPoints = Integer.parseInt(pointsInput);
            if (newStatus == null || newStatus.isEmpty()) {
                showError("Status is required!");
                return;
            }

            controller.updateRequest(requestId, newStatus, newPoints);
            JOptionPane.showMessageDialog(frame, "Request updated successfully!");
            viewAllRequests(null);
        } catch (NumberFormatException ex) {
            showError("Points must be a valid number!");
        } catch (SQLException ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteRequest(ActionEvent e) {
        int selectedRow = tableRequests.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a request to delete.");
            return;
        }

        String requestId = tableModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this request?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.deleteRequest(requestId);
                JOptionPane.showMessageDialog(frame, "Request deleted successfully!");
                viewAllRequests(null);
            } catch (SQLException ex) {
                showError(ex.getMessage());
            }
        }
    }

    private void viewAllRequests(ActionEvent e) {
        try {
            List<PickupRequest> requests = controller.getAllRequests();
            populateTable(requests);
        } catch (SQLException ex) {
            showError(ex.getMessage());
        }
    }

    private void populateTable(List<PickupRequest> requests) {
        tableModel.setRowCount(0);
        for (PickupRequest request : requests) {
            tableModel.addRow(new Object[]{
                    request.getRequestId(),
                    request.getUserId(),
                    request.getCourierId(),
                    request.getStatus(),
                    request.getPoints()
            });
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, "Error: " + message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WastePickupApp::new);
    }
}
