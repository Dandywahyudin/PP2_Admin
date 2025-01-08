package View;

import Controller.CourierController;
import Helpers.CourierUtil;
import Model.Courier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class CourierFrame {
    private JFrame frame;
    private CourierController controller;
    private DefaultTableModel tableModel;
    private JTable table;

    public CourierFrame() {
        controller = new CourierController();
        initializeUI();
        loadCourierData(); // Load data awal
        MenuBar appMenuBar = new MenuBar();

        frame.setJMenuBar(appMenuBar.createMenuBar(
                this::openDashboard,
                this::openRequest,
                this::openManageUsers,
                this::openCourier,
                e -> System.exit(0)
        ));
    }

    private void initializeUI() {
        frame = new JFrame("Pendaftaran Kurir");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Pendaftaran Kurir", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(lblTitle, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Courier ID", "Nama", "Email", "Nomor Handphone", "Nomor Polisi", "Nomor SIM"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel crudPanel = new JPanel(new FlowLayout());
        JButton btnCreate = new JButton("Daftar Kurir");
        JButton btnUpdate = new JButton("Ubah Data Kurir");
        JButton btnDelete = new JButton("Hapus Data Kurir");
        JButton btnBack = new JButton("Kembali");

        crudPanel.add(btnCreate);
        crudPanel.add(btnUpdate);
        crudPanel.add(btnDelete);
        crudPanel.add(btnBack);

        frame.add(crudPanel, BorderLayout.SOUTH);

        // Event Handling
        btnCreate.addActionListener(e -> createCourier());
        btnUpdate.addActionListener(e -> updateCourier());
        btnDelete.addActionListener(e -> deleteCourier());
        btnBack.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }

    private void loadCourierData() {
        try {
            tableModel.setRowCount(0);
            List<Courier> couriers = controller.getAllCouriers();
            for (Courier courier : couriers) {
                tableModel.addRow(new Object[]{
                        courier.getCourierId(),
                        courier.getName(),
                        courier.getEmail(),
                        courier.getPhoneNumber(),
                        courier.getVehicleNumber(),
                        courier.getSimNumber()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to load courier data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createCourier() {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneNumberField = new JTextField();
        JTextField vehicleNumberField = new JTextField();
        JTextField simNumberField = new JTextField();

        // Generate Courier ID otomatis
        String courierId = CourierUtil.generateCourierId();

        Object[] fields = {
                "Kurir ID:", courierId,
                "Nama:", nameField,
                "Email:", emailField,
                "Nomor Handphone:", phoneNumberField,
                "Nomor Polisi:", vehicleNumberField,
                "Nomor SIM:", simNumberField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Create Courier", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                // Membuat objek Courier dengan ID yang sudah otomatis di-generate
                Courier courier = new Courier(
                        courierId,
                        nameField.getText().trim(),
                        emailField.getText().trim(),
                        phoneNumberField.getText().trim(),
                        vehicleNumberField.getText().trim(),
                        simNumberField.getText().trim()
                );
                controller.addCourier(courier);
                JOptionPane.showMessageDialog(frame, "Courier created successfully!");
                loadCourierData(); // Muat ulang data setelah pembuatan kurir baru
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Failed to create courier: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateCourier() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a courier to update", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courierId = (String) table.getValueAt(selectedRow, 0);
        String name = (String) table.getValueAt(selectedRow, 1);
        String email = (String) table.getValueAt(selectedRow, 2);
        String phoneNumber = (String) table.getValueAt(selectedRow, 3);
        String vehicleNumber = (String) table.getValueAt(selectedRow, 4);
        String simNumber = (String) table.getValueAt(selectedRow, 5);

        JTextField nameField = new JTextField(name);
        JTextField emailField = new JTextField(email);
        JTextField phoneNumberField = new JTextField(phoneNumber);
        JTextField vehicleNumberField = new JTextField(vehicleNumber);
        JTextField simNumberField = new JTextField(simNumber);

        Object[] fields = {
                "Nama:", nameField,
                "Email:", emailField,
                "Nomor Handphone:", phoneNumberField,
                "Nomor Polisi:", vehicleNumberField,
                "Nomor SIM:", simNumberField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Ubah Data Kurir", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Courier courier = new Courier(
                        courierId,
                        nameField.getText().trim(),
                        emailField.getText().trim(),
                        phoneNumberField.getText().trim(),
                        vehicleNumberField.getText().trim(),
                        simNumberField.getText().trim()
                );
                controller.updateCourier(courier);
                JOptionPane.showMessageDialog(frame, "Berhasil!");
                loadCourierData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Failed to update courier: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCourier() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a courier to delete", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courierId = (String) table.getValueAt(selectedRow, 0);
        int option = JOptionPane.showConfirmDialog(frame, "Apakah Yakin Ingin Menghapus Data Kurir?", "Hapus Data Kurir", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                controller.deleteCourier(courierId);
                JOptionPane.showMessageDialog(frame, "Data Kurir Behasil di Hapus!");
                loadCourierData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Failed to delete courier: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openDashboard(ActionEvent e) {
        new DashboardFrame(); // Navigasi ke halaman Dashboard
    }

    private void openRequest(ActionEvent e) {
        new RequestFrame(frame); // Navigasi ke halaman Request
    }

    private void openManageUsers(ActionEvent e) {
        new UserFrame(); // Navigasi ke halaman User Management
    }

    private void openCourier(ActionEvent e) {
        new CourierFrame(); // Navigasi ke halaman Pendaftaran Kurir
    }
}
