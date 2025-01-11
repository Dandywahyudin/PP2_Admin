package View;

import Controller.PickupRequestController;
import Model.PickupRequest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RequestCourierView extends JPanel {
    private DefaultTableModel tableModel;
    private PickupRequestController controller;

    public RequestCourierView() {
        this.controller = new PickupRequestController();
        initializeUI();
        loadData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Penjemputan Kurir", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // Table Model with only ID Kurir
        tableModel = new DefaultTableModel(
                new String[]{"ID Permintaan", "ID Kurir", "Status", "Poin", "Jenis Sampah"},
                0
        );
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

    }

    private void loadData() {
        try {
            List<PickupRequest> requests = controller.getAllRequests(); // Retrieve all requests
            tableModel.setRowCount(0); // Clear table
            for (PickupRequest request : requests) {
                tableModel.addRow(new Object[]{
                        request.getRequestId(),
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
}
