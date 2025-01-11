package View;

import Controller.PickupRequestController;
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

    public DashboardFrame() {
        this.mainPanel = mainPanel;
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
        contentPanel = new JPanel(new BorderLayout());

        // Title
        JLabel lblTitle = new JLabel("Dashboard", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        contentPanel.add(lblTitle, BorderLayout.NORTH);

        // Table
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
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // CRUD Panel
        JPanel crudPanel = new JPanel(new FlowLayout());
        JButton btnCreate = new JButton("Create");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");

        crudPanel.add(btnCreate);
        crudPanel.add(btnUpdate);
        crudPanel.add(btnDelete);

        // Report Panel
        JPanel reportPanel = new JPanel(new FlowLayout());
        JButton btnPrintPreview = new JButton("Print Preview");
        JButton btnPrint = new JButton("Print Report");
        JButton btnExportPDF = new JButton("Export to PDF");

        reportPanel.add(btnPrintPreview);
        reportPanel.add(btnPrint);
        reportPanel.add(btnExportPDF);

        // Combine panels
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(reportPanel, BorderLayout.NORTH);
        southPanel.add(crudPanel, BorderLayout.SOUTH);

        contentPanel.add(southPanel, BorderLayout.SOUTH);

        // Add event listeners
        btnCreate.addActionListener(e -> createRequest());
        btnUpdate.addActionListener(e -> updateRequest());
        btnDelete.addActionListener(e -> deleteRequest());
        btnPrintPreview.addActionListener(e -> showPrintPreview());
        btnPrint.addActionListener(e -> printReport());
        btnExportPDF.addActionListener(e -> exportToPDF());

        // Add content panel to main panel
        add(contentPanel, BorderLayout.CENTER);
    }

    // The rest of the methods remain the same, but replace 'this' with 'parentFrame'
    // in JOptionPane.showMessageDialog calls

    private void loadData() {
        try {
            tableModel.setRowCount(0);
            for (PickupRequest request : controller.getAllRequests()) {
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
            JOptionPane.showMessageDialog(mainPanel, "Failed to load data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createRequest() {
        try {
            List<String> userIds = controller.getAllUserIds();
            List<String> courierIds = controller.getAllCourierIds();

            JComboBox<String> userIdComboBox = new JComboBox<>(userIds.toArray(new String[0]));
            JComboBox<String> courierIdComboBox = new JComboBox<>(courierIds.toArray(new String[0]));
            JTextField requestIdField = new JTextField();
            JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Pending", "Ongoing", "Completed"});
            JTextField pointsField = new JTextField();
            JTextField wasteTypeField = new JTextField();

            Object[] fields = {
                    "Request ID:", requestIdField,
                    "User ID:", userIdComboBox,
                    "Courier ID:", courierIdComboBox,
                    "Status:", statusComboBox,
                    "Points:", pointsField,
                    "Waste Type:", wasteTypeField
            };

            int option = JOptionPane.showConfirmDialog(mainPanel, fields, "Create New Request", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String requestId = requestIdField.getText().trim();
                String userId = (String) userIdComboBox.getSelectedItem();
                String courierId = (String) courierIdComboBox.getSelectedItem();
                String status = (String) statusComboBox.getSelectedItem();
                String wasteType = wasteTypeField.getText().trim();

                if (requestId.isEmpty() || userId == null || courierId == null || status.isEmpty() || wasteType.isEmpty()) {
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

                PickupRequest request = new PickupRequest(requestId, userId, courierId, status, points, wasteType);
                controller.addRequest(request);

                JOptionPane.showMessageDialog(mainPanel, "Request added successfully!");
                loadTableData();
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

            // Replace JTextField with JComboBox for status
            JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Pending", "Ongoing", "Completed"});
            statusComboBox.setSelectedItem(status);  // Set current status as selected
            
            JTextField pointsField = new JTextField(points);
            JTextField wasteTypeField = new JTextField(wasteType);

            // Menampilkan form untuk update
            Object[] fields = {
                    "User ID:", userIdComboBox,
                    "Courier ID:", courierIdComboBox,
                    "Status:", statusComboBox,
                    "Points:", pointsField,
                    "Waste Type:", wasteTypeField
            };

            int option = JOptionPane.showConfirmDialog(mainPanel, fields, "Update Request", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                if (userIdComboBox.getSelectedItem() == null || courierIdComboBox.getSelectedItem() == null ||
                        statusComboBox.getSelectedItem() == null ||
                        pointsField.getText().trim().isEmpty() ||
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
    private void showPrintPreview() {
        try {
            List<PickupRequest> requests = controller.getAllRequests();
            StringBuilder reportContent = new StringBuilder("Pickup Requests Report:\n\n");

            for (PickupRequest request : requests) {
                reportContent.append("Request ID: ").append(request.getRequestId()).append("\n");
                reportContent.append("User ID: ").append(request.getUserId()).append("\n");
                reportContent.append("Courier ID: ").append(request.getCourierId()).append("\n");
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
                reportContent.append("Request ID: ").append(request.getRequestId()).append("\n");
                reportContent.append("User ID: ").append(request.getUserId()).append("\n");
                reportContent.append("Courier ID: ").append(request.getCourierId()).append("\n");
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
                    document.add(new com.itextpdf.text.Paragraph("Request ID: " + request.getRequestId()));
                    document.add(new com.itextpdf.text.Paragraph("User ID: " + request.getUserId()));
                    document.add(new com.itextpdf.text.Paragraph("Courier ID: " + request.getCourierId()));
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