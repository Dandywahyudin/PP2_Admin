package View;

import Controller.PickupRequestController;
import Model.PickupRequest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RequestFrame {
    private JFrame parentFrame;
    private JFrame frame;
    private PickupRequestController controller;
    private DefaultTableModel tableModel;

    public RequestFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.controller = new PickupRequestController();
        initializeUI();
        loadData(null);
    }

    private void initializeUI() {
        frame = new JFrame("Request - E-Waste Management");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel lblRequest = new JLabel("Requests", SwingConstants.CENTER);
        lblRequest.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(lblRequest, BorderLayout.NORTH);

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
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        JButton btnViewAll = new JButton("View All Requests");
        JButton btnViewCompleted = new JButton("View Completed Requests");
        JButton btnViewPending = new JButton("View Pending Requests");
        JButton btnViewOngoing = new JButton("View Ongoing Requests");
        JButton btnTrackRequest = new JButton("Track Request");
        JButton btnBack = new JButton("Back to Dashboard");

        buttonPanel.add(btnViewAll);
        buttonPanel.add(btnViewCompleted);
        buttonPanel.add(btnViewPending);
        buttonPanel.add(btnViewOngoing);
        buttonPanel.add(btnTrackRequest);
        buttonPanel.add(btnBack);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        btnViewAll.addActionListener(e -> loadData(null));
        btnViewCompleted.addActionListener(e -> loadData("Completed"));
        btnViewPending.addActionListener(e -> loadData("Pending"));
        btnViewOngoing.addActionListener(e -> loadData("Ongoing"));
        btnTrackRequest.addActionListener(e -> trackRequest());
        btnBack.addActionListener(e -> {
            frame.dispose();
            parentFrame.setVisible(true);
        });

        frame.setVisible(true);
    }

    private void loadData(String statusFilter) {
        try {
            List<PickupRequest> requests;
            if (statusFilter == null) {
                requests = controller.getAllRequests();
            } else {
                requests = controller.getRequestsByStatus(statusFilter);
            }
            tableModel.setRowCount(0);
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
            JOptionPane.showMessageDialog(frame, "Failed to load data: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void trackRequest() {
        JTextField requestIdField = new JTextField();
        JTextField userIdField = new JTextField();
        JTextField courierIdField = new JTextField();
        JTextField wasteTypeField = new JTextField();

        Object[] fields = {
                "Request ID:", requestIdField,
                "User ID:", userIdField,
                "Courier ID:", courierIdField,
                "Waste Type:", wasteTypeField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Track Request", 
            JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String requestId = requestIdField.getText();
                String userId = userIdField.getText();
                String courierId = courierIdField.getText();

                List<PickupRequest> results = controller.trackRequest(requestId, userId, courierId);
                tableModel.setRowCount(0);
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
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Failed to track request: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
