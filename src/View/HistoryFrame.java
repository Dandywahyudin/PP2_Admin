package View;

import Controller.PickupRequestController;
import Model.PickupRequest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class HistoryFrame extends JPanel {
    private PickupRequestController controller;
    private DefaultTableModel tableModel;

    public HistoryFrame() {
        this.controller = new PickupRequestController();
        initializeUI();
        loadData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JLabel lblHistory = new JLabel("Completed Requests", SwingConstants.CENTER);
        lblHistory.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblHistory, BorderLayout.NORTH);

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
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() {
        try {
            List<PickupRequest> requests = controller.getRequestsByStatus("Completed");
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
}
