package View;

import Controller.UserController;
import Model.User;
import Helpers.UserUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UserView extends JPanel{
    private UserController controller;
    private DefaultTableModel tableModel;
    private JTable table;
    private JPanel mainPanel;

    public UserView() {
        controller = new UserController();
        initializeUI();
        loadUserData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Pendaftaran Masyarakat", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID Masyarakat", "Nama", "Email", "Nomor Handphone", "Alamat"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel crudPanel = new JPanel(new FlowLayout());
        JButton btnCreate = new JButton("Daftar Masyarakat");
        JButton btnUpdate = new JButton("Ubah Data Masyarakat");
        JButton btnDelete = new JButton("Hapus Data Masyarakat");

        crudPanel.add(btnCreate);
        crudPanel.add(btnUpdate);
        crudPanel.add(btnDelete);

        add(crudPanel, BorderLayout.SOUTH);

        // Event Handling
        btnCreate.addActionListener(e -> createUser());
        btnUpdate.addActionListener(e -> updateUser());
        btnDelete.addActionListener(e -> deleteUser());
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
            JOptionPane.showMessageDialog(this, "Failed to load user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        int option = JOptionPane.showConfirmDialog(this, fields, "Pendaftaran Masyarakat", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                // Validasi input
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String phoneNumber = phoneNumberField.getText().trim();
                String address = addressField.getText().trim();

                if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                    JOptionPane.showMessageDialog(this, "Email tidak valid!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!phoneNumber.matches("^\\d{10,15}$")) {
                    JOptionPane.showMessageDialog(this, "Nomor handphone harus terdiri dari 10-15 digit angka!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                User user = new User(userIdField, name, email, phoneNumber, address);
                controller.addUser(user);
                JOptionPane.showMessageDialog(this, "Berhasil!");
                loadUserData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih Masyarakat Yang Ingin di Ubah", "Warning", JOptionPane.WARNING_MESSAGE);
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

        int option = JOptionPane.showConfirmDialog(this, fields, "Ubah Data Masyarakat", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                // Validasi input
                String newName = nameField.getText().trim();
                String newEmail = emailField.getText().trim();
                String newPhoneNumber = phoneNumberField.getText().trim();
                String newAddress = addressField.getText().trim();

                if (newName.isEmpty() || newEmail.isEmpty() || newPhoneNumber.isEmpty() || newAddress.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                    JOptionPane.showMessageDialog(this, "Email tidak valid!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!phoneNumber.matches("^\\d{10,15}$")) {
                    JOptionPane.showMessageDialog(this, "Nomor handphone harus terdiri dari 10-15 digit angka!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                User user = new User(userId, newName, newEmail, newPhoneNumber, newAddress);
                controller.updateUser(user);
                JOptionPane.showMessageDialog(this, "Berhasil!");
                loadUserData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal! " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void deleteUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih Data Yang Ingin di Hapus", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userId = (String) table.getValueAt(selectedRow, 0);
        int option = JOptionPane.showConfirmDialog(this, "Apakah Kamu Yakin Ingin Menghapus", "Hapus Data", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                controller.deleteUser(userId);
                JOptionPane.showMessageDialog(this, "Berhasil!");
                loadUserData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
