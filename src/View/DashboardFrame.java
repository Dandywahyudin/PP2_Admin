package View;

import Controller.PickupRequestController;
import Helpers.RequestUtil;
import Model.PickupRequest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DashboardFrame extends JPanel {
    private PickupRequestController controller;
    private DefaultTableModel tableModel;
    private JTable table;
    private JPanel contentPanel;
    private JPanel mainPanel;
    private JLabel totalPointsLabel;
    private JComboBox<String> statusComboBox;

    public DashboardFrame() {
        this.controller = new PickupRequestController();
        initializeUI();
        loadData();
    }

    private void loadTableData() {
        try {
            tableModel.setRowCount(0);
            List<PickupRequest> requests = controller.getAllRequests();
            for (PickupRequest request : requests) {
                Object[] rowData = {
                        request.getRequestId(),
                        request.getUserId(),
                        request.getCourierId(),
                        request.getStatus(),
                        request.getPoints(),
                        request.getWasteType()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainPanel, "Failed to load data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JLabel lblDashboard = new JLabel("Dashboard", SwingConstants.CENTER);
        lblDashboard.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblDashboard, BorderLayout.NORTH);

        // Tabel utama
        tableModel = new DefaultTableModel(
                new String[]{
                        "ID Permintaan",
                        "ID Masyarakat",
                        "ID Kurir",
                        "Status",
                        "Poin",
                        "Jenis Sampah"
                },
                0
        );
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel untuk bagian bawah
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // Panel untuk total points dengan border line di atas dan bawah
        JPanel totalPointsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        totalPointsLabel = new JLabel("Total Points: 0");
        totalPointsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        totalPointsPanel.add(totalPointsLabel);
        totalPointsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK), // Line border atas dan bawah
                BorderFactory.createEmptyBorder(5, 0, 5, 0)  // Padding
        ));
        bottomPanel.add(totalPointsPanel, BorderLayout.NORTH);

        // Panel untuk tombol-tombol
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        
        // Sub panel untuk tombol print dan export
        JPanel printPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnPrintPreview = new JButton("Print Preview");
        JButton btnPrintReport = new JButton("Print Report");
        JButton btnExportPDF = new JButton("Export to PDF");
        printPanel.add(btnPrintPreview);
        printPanel.add(btnPrintReport);
        printPanel.add(btnExportPDF);
        
        // Sub panel untuk tombol CRUD
        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnCreate = new JButton("Tambah Data");
        JButton btnUpdate = new JButton("Ubah Data");
        JButton btnDelete = new JButton("Hapus Data");
        crudPanel.add(btnCreate);
        crudPanel.add(btnUpdate);
        crudPanel.add(btnDelete);
        
        buttonPanel.add(printPanel);
        buttonPanel.add(crudPanel);
        
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        
        add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnCreate.addActionListener(e -> createRequest());
        btnUpdate.addActionListener(e -> updateRequest());
        btnDelete.addActionListener(e -> deleteRequest());
        btnPrintPreview.addActionListener(e -> printPreview());
        btnPrintReport.addActionListener(e -> printReport());
        btnExportPDF.addActionListener(e -> exportToPDF());

        loadData();
    }

    private void loadData() {
        try {
            List<PickupRequest> requests = controller.getAllRequests();
            tableModel.setRowCount(0);
            int totalPoints = 0;
            
            for (PickupRequest request : requests) {
                tableModel.addRow(new Object[]{
                        request.getRequestId(),
                        request.getUserId(),
                        request.getCourierId(),
                        request.getStatus(),
                        request.getPoints(),
                        request.getWasteType()
                });
                totalPoints += request.getPoints();
            }
            
            // Update total points label
            totalPointsLabel.setText("Total Points: " + totalPoints);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createRequest() {
        try {
            // Ambil data userId dan courierId dari database
            List<String> userIds = controller.getAllUserIds(); // Anda perlu membuat metode untuk ini
            List<String> courierIds = controller.getAllCourierIds(); // Anda perlu membuat metode untuk ini

            JComboBox<String> userIdComboBox = new JComboBox<>(userIds.toArray(new String[0]));
            JComboBox<String> courierIdComboBox = new JComboBox<>(courierIds.toArray(new String[0]));
            JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Pending", "Ongoing", "Completed"});
            JTextField pointsField = new JTextField();
            JTextField wasteTypeField = new JTextField();

            String requestIdField = RequestUtil.generateRequestId();

            Object[] fields = {
                    "Request ID:", requestIdField,
                    "ID Masyarakat:", userIdComboBox,
                    "ID Kurir:", courierIdComboBox,
                    "Status:", statusComboBox,
                    "Points:", pointsField,
                    "Jenis Sampah:", wasteTypeField
            };

            int option = JOptionPane.showConfirmDialog(mainPanel, fields, "Create New Request", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String userId = (String) userIdComboBox.getSelectedItem();
                String courierId = (String) courierIdComboBox.getSelectedItem();
                String status = (String) statusComboBox.getSelectedItem();
                String wasteType = wasteTypeField.getText().trim();

                if (userId == null || courierId == null || status.isEmpty() || wasteType.isEmpty()) {
                    JOptionPane.showMessageDialog(mainPanel, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int points;
                try {
                    points = Integer.parseInt(pointsField.getText().trim());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(mainPanel, "Points must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PickupRequest request = new PickupRequest(requestIdField, userId, courierId, status, points, wasteType);
                controller.addRequest(request);

                JOptionPane.showMessageDialog(mainPanel, "Request added successfully!");
                loadTableData();
                loadData();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainPanel, "Failed to load data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRequest() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainPanel, "Please select a request to update", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ambil data dari row yang dipilih
        String requestId = (String) table.getValueAt(selectedRow, 0);
        String userId = (String) table.getValueAt(selectedRow, 1);
        String courierId = (String) table.getValueAt(selectedRow, 2);
        String status = (String) table.getValueAt(selectedRow, 3);
        String points = table.getValueAt(selectedRow, 4).toString();
        String wasteType = (String) table.getValueAt(selectedRow, 5);

        try {
            // Ambil data userId dan courierId yang ada di database
            List<String> userIds = controller.getAllUserIds(); // Ambil daftar userId
            List<String> courierIds = controller.getAllCourierIds(); // Ambil daftar courierId

            // Buat JComboBox untuk userId dan courierId
            JComboBox<String> userIdComboBox = new JComboBox<>(userIds.toArray(new String[0]));
            JComboBox<String> courierIdComboBox = new JComboBox<>(courierIds.toArray(new String[0]));

            // Set nilai default pada JComboBox sesuai dengan data yang ada pada tabel
            userIdComboBox.setSelectedItem(userId);
            courierIdComboBox.setSelectedItem(courierId);

            JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Pending", "Ongoing", "Completed"});
            statusComboBox.setSelectedItem(status);

            JTextField pointsField = new JTextField(points);
            JTextField wasteTypeField = new JTextField(wasteType);

            // Menampilkan form untuk update
            Object[] fields = {
                    "ID Masyarakat:", userIdComboBox,
                    "Id Kurir:", courierIdComboBox,
                    "Status:", statusComboBox,
                    "Points:", pointsField,
                    "Waste Type:", wasteTypeField
            };

            int option = JOptionPane.showConfirmDialog(mainPanel, fields, "Ubah Data", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                if (userIdComboBox.getSelectedItem() == null || courierIdComboBox.getSelectedItem() == null ||
                        statusComboBox.getSelectedItem() == null || pointsField.getText().trim().isEmpty() ||
                        wasteTypeField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(mainPanel, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int pointsValue;
                try {
                    pointsValue = Integer.parseInt(pointsField.getText().trim());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(mainPanel, "Points must be a number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Buat objek PickupRequest dengan data yang telah diperbarui
                PickupRequest updatedRequest = new PickupRequest(
                        requestId,
                        (String) userIdComboBox.getSelectedItem(),
                        (String) courierIdComboBox.getSelectedItem(),
                        (String) statusComboBox.getSelectedItem(),
                        pointsValue,
                        wasteTypeField.getText().trim()
                );

                // Panggil controller untuk memperbarui data di database
                controller.updateRequest(updatedRequest);
                loadTableData(); // Memuat data yang telah diperbarui
                JOptionPane.showMessageDialog(mainPanel, "Request updated successfully!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainPanel, "Failed to update request: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRequest() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainPanel, "Please select a request to delete",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String requestId = (String) table.getValueAt(selectedRow, 0);
        int option = JOptionPane.showConfirmDialog(mainPanel,
                "Are you sure you want to delete this request?",
                "Delete Request",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            try {
                controller.deleteRequest(requestId);
                loadTableData();
                JOptionPane.showMessageDialog(mainPanel, "Request deleted successfully!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(mainPanel, "Failed to delete request: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //show print preview
    private void printPreview() {
        try {
            List<PickupRequest> requests = controller.getAllRequests();
            StringBuilder reportContent = new StringBuilder("Pickup Requests Report:\n\n");

            for (PickupRequest request : requests) {
                reportContent.append("ID Permintaan: ").append(request.getRequestId()).append("\n");
                reportContent.append("ID Masyarakat: ").append(request.getUserId()).append("\n");
                reportContent.append("ID Kurir: ").append(request.getCourierId()).append("\n");
                reportContent.append("Status: ").append(request.getStatus()).append("\n");
                reportContent.append("Points: ").append(request.getPoints()).append("\n");
                reportContent.append("----------------------------\n");
            }

            JTextArea textArea = new JTextArea(reportContent.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));

            JOptionPane.showMessageDialog(mainPanel, scrollPane, "Print Preview", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainPanel, "Failed to generate report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //print report
    private void printReport() {
        try {
            List<PickupRequest> requests = controller.getAllRequests();
            StringBuilder reportContent = new StringBuilder("Pickup Requests Report:\n\n");

            for (PickupRequest request : requests) {
                reportContent.append("ID Permintaan: ").append(request.getRequestId()).append("\n");
                reportContent.append("ID Masyarakat: ").append(request.getUserId()).append("\n");
                reportContent.append("ID Kurir: ").append(request.getCourierId()).append("\n");
                reportContent.append("Status: ").append(request.getStatus()).append("\n");
                reportContent.append("Points: ").append(request.getPoints()).append("\n");
                reportContent.append("----------------------------\n");
            }

            JTextArea textArea = new JTextArea(reportContent.toString());
            textArea.print(); // Print dialog will open
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, "Failed to print report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //export ke pdf langsung
    private void exportToPDF() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save as PDF");
            int userSelection = fileChooser.showSaveDialog(mainPanel);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath() + ".pdf";

                com.itextpdf.text.Document document = new com.itextpdf.text.Document();
                com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(filePath));

                document.open();
                document.add(new com.itextpdf.text.Paragraph("Pickup Requests Report\n\n"));

                List<PickupRequest> requests = controller.getAllRequests();
                for (PickupRequest request : requests) {
                    document.add(new com.itextpdf.text.Paragraph("ID Permintaan: " + request.getRequestId()));
                    document.add(new com.itextpdf.text.Paragraph("ID Masyarakat: " + request.getUserId()));
                    document.add(new com.itextpdf.text.Paragraph("ID Kurir: " + request.getCourierId()));
                    document.add(new com.itextpdf.text.Paragraph("Status: " + request.getStatus()));
                    document.add(new com.itextpdf.text.Paragraph("Points: " + request.getPoints()));
                    document.add(new com.itextpdf.text.Paragraph("----------------------------"));
                }

                document.close();
                JOptionPane.showMessageDialog(mainPanel, "Report exported to PDF successfully!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, "Failed to export report to PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}