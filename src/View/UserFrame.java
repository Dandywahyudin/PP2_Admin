package View;

import Controller.UserController;
import Model.User;
import Helpers.UserUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class UserFrame {
    private JFrame frame;
    private UserController controller;
    private DefaultTableModel tableModel;
    private JTable table;

    public UserFrame() {
        controller = new UserController();
        initializeUI();
        loadUserData(); // Load data awal
        String userIdField = UserUtil.generateUserId();
        System.out.println("Generated User ID: " + userIdField);
    }

    private void initializeUI() {
        frame = new JFrame("Pendaftaran Masyarakat");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        MenuBar appMenuBar = new MenuBar();
        frame.setJMenuBar(appMenuBar.createMenuBar(
                this::openDashboard,
                this::openRequest,
                this::openManageUsers,
                this::openCourier,
                e -> System.exit(0)
        ));

        JLabel lblTitle = new JLabel("Pendaftaran Masyarakat", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(lblTitle, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID Masyarakat", "Nama", "Email", "Nomor Handphone", "Alamat"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel crudPanel = new JPanel(new FlowLayout());
        JButton btnCreate = new JButton("Daftar Masyarakat");
        JButton btnUpdate = new JButton("Ubah Data Masyarakat");
        JButton btnDelete = new JButton("Hapus Data Masyarakat");
        JButton btnBack = new JButton("Kembali");

        crudPanel.add(btnCreate);
        crudPanel.add(btnUpdate);
        crudPanel.add(btnDelete);
        crudPanel.add(btnBack);

        frame.add(crudPanel, BorderLayout.SOUTH);

        // Event Handling
        btnCreate.addActionListener(e -> createUser());
        btnUpdate.addActionListener(e -> updateUser());
        btnDelete.addActionListener(e -> deleteUser());
        btnBack.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }

    private void loadUserData() {
        try {
            tableModel.setRowCount(0);
            List<User> users = controller.getAllUsers();
            for (User user : users) {
                tableModel.addRow(new Object[]{
                        user.getUserId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getAddress()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to load user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createUser() {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneNumberField = new JTextField();
        JTextField addressField = new JTextField();

        String userIdField = UserUtil.generateUserId();

        Object[] fields = {
                "ID Masyarakat:", userIdField,
                "Nama:", nameField,
                "Email:", emailField,
                "Nomor Handphone:", phoneNumberField,
                "Alamat:", addressField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Pendaftaran Masyarakat", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                User user = new User(
                        userIdField,
                        nameField.getText().trim(),
                        emailField.getText().trim(),
                        phoneNumberField.getText().trim(),
                        addressField.getText().trim()
                );
                controller.addUser(user);
                JOptionPane.showMessageDialog(frame, "Berhasil!");
                loadUserData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Gagal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Pilih Masyarakat Yang Ingin di Ubah", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userId = (String) table.getValueAt(selectedRow, 0);
        String name = (String) table.getValueAt(selectedRow, 1);
        String email = (String) table.getValueAt(selectedRow, 2);
        String phoneNumber = (String) table.getValueAt(selectedRow, 3);
        String address = (String) table.getValueAt(selectedRow, 4);

        JTextField nameField = new JTextField(name);
        JTextField emailField = new JTextField(email);
        JTextField phoneNumberField = new JTextField(phoneNumber);
        JTextField addressField = new JTextField(address);

        Object[] fields = {
                "Nama:", nameField,
                "Email:", emailField,
                "Nomor Handphone:", phoneNumberField,
                "Alamat:", addressField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "Ubah Data Masyarakat", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                User user = new User(
                        userId,
                        nameField.getText().trim(),
                        emailField.getText().trim(),
                        phoneNumberField.getText().trim(),
                        addressField.getText().trim()
                );
                controller.updateUser(user);
                JOptionPane.showMessageDialog(frame, "Berhasil!");
                loadUserData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Gagal! " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Pilih Data Yang Ingin di Hapus", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userId = (String) table.getValueAt(selectedRow, 0);
        int option = JOptionPane.showConfirmDialog(frame, "Apakah Kamu Yakin Ingin Menghapus", "Hapus Data", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                controller.deleteUser(userId);
                JOptionPane.showMessageDialog(frame, "Berhasil!");
                loadUserData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Gagal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
