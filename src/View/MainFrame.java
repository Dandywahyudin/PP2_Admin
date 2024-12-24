package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame {
    private JFrame frame;

    public MainFrame() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("E-Waste Management");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Menu dropdown di pojok kiri atas
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem menuDashboard = new JMenuItem("Dashboard");
        JMenuItem menuRequest = new JMenuItem("Request");
        JMenuItem menuExit = new JMenuItem("Exit");

        menu.add(menuDashboard);
        menu.add(menuRequest);
        menu.addSeparator();
        menu.add(menuExit);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        // Panel utama untuk tampilan awal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        // Panel untuk menampung teks "E-Waste Dashboard" dan "Penjemputan Sampah"
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Label untuk "E-Waste Dashboard"
        JLabel lblTitle = new JLabel("E-Waste Dashboard", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 36));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(lblTitle);

        // Label untuk "Penjemputan Sampah"
        JLabel lblPickup = new JLabel("Penjemputan Sampah", SwingConstants.CENTER);
        lblPickup.setFont(new Font("Arial", Font.PLAIN, 20));
        lblPickup.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(lblPickup);

        // Add titlePanel to mainPanel
        mainPanel.add(titlePanel, gbc);

        frame.add(mainPanel, BorderLayout.CENTER);

        // Event handling
        menuDashboard.addActionListener(this::openDashboard);
        menuRequest.addActionListener(this::openRequest);
        menuExit.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private void openDashboard(ActionEvent e) {
        new DashboardFrame(); // Navigasi ke halaman Dashboard
    }

    private void openRequest(ActionEvent e) {
        new RequestFrame(frame); // Navigasi ke halaman Request
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
