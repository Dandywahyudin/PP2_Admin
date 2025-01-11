package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public MainFrame() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("E-Waste Management");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create and set the menu bar using AppMenuBar
        MenuBar appMenuBar = new MenuBar();
        frame.setJMenuBar(appMenuBar.createMenuBar(
                this::openDashboard,
                this::openRequest,
                this::openManageUsers,
                this::openCourier,
                this::openHistory,
                e -> System.exit(0)
        ));

        // Initialize main panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create panels for different views (no need to recreate frames)
        JPanel dashboardPanel = new DashboardFrame();
        JPanel requestPanel = new RequestFrame();
        JPanel manageUsersPanel = new UserFrame(); // Make sure UserFrame is added directly
        JPanel courierPanel = new CourierView(); // Make sure CourierView is added directly
        JPanel historyPanel = new HistoryFrame();

        // Add all panels to the mainPanel
        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(requestPanel, "Request");
        mainPanel.add(manageUsersPanel, "ManageUsers");
        mainPanel.add(courierPanel, "Courier");
        mainPanel.add(historyPanel, "History");

        // Add the mainPanel to the frame
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // Methods to switch between views
    private void openDashboard(ActionEvent e) {
        cardLayout.show(mainPanel, "Dashboard");
    }

    private void openRequest(ActionEvent e) {
        cardLayout.show(mainPanel, "Request");
    }

    private void openManageUsers(ActionEvent e) {
        cardLayout.show(mainPanel, "ManageUsers");
    }

    private void openCourier(ActionEvent e) {
        cardLayout.show(mainPanel, "Courier");
    }

    private void openHistory(ActionEvent e) {
        cardLayout.show(mainPanel, "History");
    }

    // Create a dummy panel for placeholder (you can customize or remove this if not needed)
    private JPanel createDummyPanel(String text) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
