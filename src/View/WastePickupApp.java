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

public class WastePickupApp{

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
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Panel atas untuk filter dan tindakan
        JPanel topPanel = new JPanel(new FlowLayout());
        JButton btnViewAllRequests = new JButton("View All Requests");
        JButton btnViewCollected = new JButton("View Collected Requests");
        JButton btnTrackRequest = new JButton("Track Request");
        JButton btnViewHistory = new JButton("View History");
        topPanel.add(btnViewAllRequests);
        topPanel.add(btnViewCollected);
        topPanel.add(btnTrackRequest);
        topPanel.add(btnViewHistory);

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
        btnViewAllRequests.addActionListener(this::viewAllRequests);
        btnViewCollected.addActionListener(this::viewCollectedRequests);
        btnTrackRequest.addActionListener(this::trackRequest);
        btnViewHistory.addActionListener(this::viewHistory);

        frame.setVisible(true);

        // Load initial data
        viewAllRequests(null);
    }

    private void viewAllRequests(ActionEvent e) {
        try {
            List<PickupRequest> requests = controller.getAllRequests();
            populateTable(requests);
        } catch (SQLException ex) {
            showError(ex.getMessage());
        }
    }

    private void viewCollectedRequests(ActionEvent e) {
        try {
            List<PickupRequest> requests = controller.getAllRequests();
            populateTable(requests.stream().filter(r -> "Completed".equals(r.getStatus())).collect(Collectors.toList()));
            int totalCollected = (int) requests.stream().filter(r -> "Completed".equals(r.getStatus())).count();
            lblTotalCollected.setText("Total Collected: " + totalCollected + " Requests");
            int totalPoints = requests.stream().filter(r -> "Completed".equals(r.getStatus())).mapToInt(PickupRequest::getPoints).sum();
            lblTotalPoints.setText("Total Points: " + totalPoints);
        } catch (SQLException ex) {
            showError(ex.getMessage());
        }
    }

    private void trackRequest(ActionEvent e) {
        String requestId = JOptionPane.showInputDialog(frame, "Enter Request ID to Track:");
        if (requestId != null && !requestId.trim().isEmpty()) {
            try {
                List<PickupRequest> requests = controller.getAllRequests();
                PickupRequest request = requests.stream().filter(r -> r.getRequestId().equals(requestId)).findFirst().orElse(null);
                if (request != null) {
                    JOptionPane.showMessageDialog(frame, "Request Details:\n" +
                            "Request ID: " + request.getRequestId() + "\n" +
                            "User ID: " + request.getUserId() + "\n" +
                            "Courier ID: " + (request.getCourierId() != null ? request.getCourierId() : "Not Assigned") + "\n" +
                            "Status: " + request.getStatus() + "\n" +
                            "Points: " + request.getPoints());
                } else {
                    JOptionPane.showMessageDialog(frame, "Request ID not found.");
                }
            } catch (SQLException ex) {
                showError(ex.getMessage());
            }
        }
    }

    private void viewHistory(ActionEvent e) {
        try {
            List<PickupRequest> requests = controller.getAllRequests();
            populateTable(requests); // For now, assuming all records as history
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
