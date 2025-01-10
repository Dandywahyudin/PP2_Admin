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
    private JComboBox<String> courierComboBox;

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
                        request.getPoints(),
                        request.getWasteType()
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
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panel CRUD
        JPanel crudPanel = new JPanel(new FlowLayout());
        JButton btnCreate = new JButton("Create");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnTotalPoints = new JButton("Total Points");

        crudPanel.add(btnCreate);
        crudPanel.add(btnUpdate);
        crudPanel.add(btnDelete);
        crudPanel.add(btnTotalPoints);

        frame.add(crudPanel, BorderLayout.SOUTH);

        //tambah button print
        JPanel reportPanel = new JPanel(new FlowLayout());
        JButton btnPrintPreview = new JButton("Print Preview");
        JButton btnPrint = new JButton("Print Report");
        JButton btnExportPDF = new JButton("Export to PDF");
        reportPanel.add(btnPrintPreview);
        reportPanel.add(btnPrint);
        reportPanel.add(btnExportPDF);
        frame.add(reportPanel, BorderLayout.NORTH);


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
        btnTotalPoints.addActionListener(e -> {
            try { JComboBox<String> courierComboBox = new JComboBox<>();
                // Creating ComboBox within action handler
                List<String> courierIds = controller.getAllCourierIds();
                for (String courierId : courierIds) {
                    courierComboBox.addItem(courierId); }
                Object[] dialogContent = { "Select Courier:", courierComboBox };
                int option = JOptionPane.showConfirmDialog(frame, dialogContent, "Total Points", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String selectedCourier = (String) courierComboBox.getSelectedItem();
                    int totalPoints = controller.getTotalPointsForCourier(selectedCourier);
                    JOptionPane.showMessageDialog(frame, "Total Points for courier " + selectedCourier + ": " + totalPoints); }
            } catch (SQLException ex) { JOptionPane.showMessageDialog(frame, "Failed to calculate total points: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });

        //Event Listener untuk Tombol print pdf
        btnPrintPreview.addActionListener(e -> showPrintPreview());
        btnPrint.addActionListener(e -> printReport());
        btnExportPDF.addActionListener(e -> exportToPDF());

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
                        request.getPoints(),
                        request.getWasteType()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to load data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createRequest() {
        JTextField requestIdField = new JTextField();
        JComboBox<String> userIdComboBox = new JComboBox<>();
        JComboBox<String> courierIdComboBox = new JComboBox<>();
        JTextField statusField = new JTextField();
        JTextField pointsField = new JTextField();
        JTextField wasteTypeField = new JTextField();

        try {
            // Ambil data User dan Courier dari database
            List<String> userIds = controller.getAllUserIds();
            List<String> courierIds = controller.getAllCourierIds();

            // Tambahkan ke JComboBox
            for (String userId : userIds) {
                userIdComboBox.addItem(userId);
            }
            for (String courierId : courierIds) {
                courierIdComboBox.addItem(courierId);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to load users or couriers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Object[] fields = {
                "ID Permintaan:", requestIdField,
                "ID Pengguna:", userIdComboBox,
                "ID Kurir:", courierIdComboBox,
                "Status:", statusField,
                "Point:", pointsField,
                "Jenis Sampah:", wasteTypeField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Create New Request", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String requestId = requestIdField.getText().trim();
                String userId = (String) userIdComboBox.getSelectedItem();
                String courierId = (String) courierIdComboBox.getSelectedItem();
                String status = statusField.getText().trim();
                String wasteType = wasteTypeField.getText().trim();

                if (status.isEmpty() || wasteType.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int points;
                try {
                    points = Integer.parseInt(pointsField.getText().trim());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(frame, "Points must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PickupRequest request = new PickupRequest(
                        requestId.isEmpty() ? null : requestId, // Gunakan ID jika diisi
                        userId,
                        courierId,
                        status,
                        points,
                        wasteType
                );

                controller.addRequest(request);
                JOptionPane.showMessageDialog(frame, "Request added successfully!");
                loadTableData();
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
        String requestId = (String) table.getValueAt(selectedRow, 0);
        String userId = (String) table.getValueAt(selectedRow, 1);
        String courierId = (String) table.getValueAt(selectedRow, 2);
        String status = (String) table.getValueAt(selectedRow, 3);
        String points = table.getValueAt(selectedRow, 4).toString();
        String wasteType = (String) table.getValueAt(selectedRow, 5);

        // Buat JComboBox untuk userId dan courierId
        JComboBox<String> userIdComboBox = new JComboBox<>();
        JComboBox<String> courierIdComboBox = new JComboBox<>();
        JTextField statusField = new JTextField(status);
        JTextField pointsField = new JTextField(points);
        JTextField wasteTypeField = new JTextField(wasteType);

        try {
            // Ambil data User dan Courier dari database
            List<String> userIds = controller.getAllUserIds();
            List<String> courierIds = controller.getAllCourierIds();

            // Tambahkan ke JComboBox
            for (String id : userIds) {
                userIdComboBox.addItem(id);
                if (id.equals(userId)) {
                    userIdComboBox.setSelectedItem(id);
                }
            }
            for (String id : courierIds) {
                courierIdComboBox.addItem(id);
                if (id.equals(courierId)) {
                    courierIdComboBox.setSelectedItem(id);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to load users or couriers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tampilkan dialog input untuk mengedit data
        Object[] fields = {
                "Request ID (Read-only):", new JLabel(requestId),
                "User ID:", userIdComboBox,
                "Courier ID:", courierIdComboBox,
                "Status:", statusField,
                "Points:", pointsField,
                "Waste Type:", wasteTypeField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Update Request", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                // Validasi input
                if (statusField.getText().trim().isEmpty() ||
                        pointsField.getText().trim().isEmpty() ||
                        wasteTypeField.getText().trim().isEmpty()) {
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
                        (String) userIdComboBox.getSelectedItem(),
                        (String) courierIdComboBox.getSelectedItem(),
                        statusField.getText().trim(),
                        pointsValue,
                        wasteTypeField.getText().trim()
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
            JOptionPane.showMessageDialog(frame, scrollPane, "Print Preview", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to generate report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(frame, "Failed to print report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    //export ke pdf langsung
    private void exportToPDF() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save as PDF");
            int userSelection = fileChooser.showSaveDialog(frame);
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
                JOptionPane.showMessageDialog(frame, "Report exported to PDF successfully!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to export report to PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
