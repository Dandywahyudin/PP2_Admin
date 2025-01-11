package View;

import Controller.CourierController;
import Helpers.CourierUtil;
import Model.Courier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CourierView extends JPanel {
    private CourierController controller;
    private DefaultTableModel tableModel;
    private JTable table;
    private JPanel mainPanel;

    public CourierView() {
        this.mainPanel = mainPanel;
        controller = new CourierController();
        initializeUI();
        loadCourierData(); // Load data awal
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Pendaftaran Kurir", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Courier ID", "Nama", "Email", "Nomor Handphone", "Nomor Polisi", "Nomor SIM"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel crudPanel = new JPanel(new FlowLayout());
        JButton btnCreate = new JButton("Daftar Kurir");
        JButton btnUpdate = new JButton("Ubah Data Kurir");
        JButton btnDelete = new JButton("Hapus Data Kurir");

        crudPanel.add(btnCreate);
        crudPanel.add(btnUpdate);
        crudPanel.add(btnDelete);

        add(crudPanel, BorderLayout.SOUTH);

        // Event Handling
        btnCreate.addActionListener(e -> createCourier());
        btnUpdate.addActionListener(e -> updateCourier());
        btnDelete.addActionListener(e -> deleteCourier());
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
            JOptionPane.showMessageDialog(this, "Failed to load courier data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createCourier() {
        JTextField txtName = new JTextField(15);
        JTextField txtEmail = new JTextField(15);
        JTextField txtPhoneNumber = new JTextField(15);
        JTextField txtVehicleNumber = new JTextField(15);
        JTextField txtSimNumber = new JTextField(15);

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Nama:"));
        panel.add(txtName);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Nomor Handphone:"));
        panel.add(txtPhoneNumber);
        panel.add(new JLabel("Nomor Polisi:"));
        panel.add(txtVehicleNumber);
        panel.add(new JLabel("Nomor SIM:"));
        panel.add(txtSimNumber);

        int result = JOptionPane.showConfirmDialog(this, panel, "Daftar Kurir Baru", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String courierId = CourierUtil.generateCourierId();
            String name = txtName.getText();
            String email = txtEmail.getText();
            String phoneNumber = txtPhoneNumber.getText();
            String vehicleNumber = txtVehicleNumber.getText();
            String simNumber = txtSimNumber.getText();

            Courier courier = new Courier(courierId, name, email, phoneNumber, vehicleNumber, simNumber);
            try {
                controller.addCourier(courier);
                JOptionPane.showMessageDialog(this, "Kurir berhasil didaftarkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadCourierData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal mendaftarkan kurir: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateCourier() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih kurir yang ingin diubah!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courierId = (String) tableModel.getValueAt(selectedRow, 0);

        JTextField txtName = new JTextField((String) tableModel.getValueAt(selectedRow, 1), 15);
        JTextField txtEmail = new JTextField((String) tableModel.getValueAt(selectedRow, 2), 15);
        JTextField txtPhoneNumber = new JTextField((String) tableModel.getValueAt(selectedRow, 3), 15);
        JTextField txtVehicleNumber = new JTextField((String) tableModel.getValueAt(selectedRow, 4), 15);
        JTextField txtSimNumber = new JTextField((String) tableModel.getValueAt(selectedRow, 5), 15);

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Nama:"));
        panel.add(txtName);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Nomor Handphone:"));
        panel.add(txtPhoneNumber);
        panel.add(new JLabel("Nomor Polisi:"));
        panel.add(txtVehicleNumber);
        panel.add(new JLabel("Nomor SIM:"));
        panel.add(txtSimNumber);

        int result = JOptionPane.showConfirmDialog(this, panel, "Ubah Data Kurir", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Courier courier = new Courier(courierId, txtName.getText(), txtEmail.getText(),
                        txtPhoneNumber.getText(), txtVehicleNumber.getText(), txtSimNumber.getText());
                controller.updateCourier(courier);
                JOptionPane.showMessageDialog(this, "Data kurir berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadCourierData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal mengubah data kurir: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCourier() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih kurir yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String courierId = (String) tableModel.getValueAt(selectedRow, 0);
        int result = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus kurir ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                controller.deleteCourier(courierId);
                JOptionPane.showMessageDialog(this, "Kurir berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadCourierData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus kurir: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
