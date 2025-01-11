package View;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MenuBar {
    public JMenuBar createMenuBar(ActionListener dashboardListener, ActionListener requestListener,
                                  ActionListener manageUsersListener, ActionListener manageCouriersListener,
                                  ActionListener historyListener, ActionListener exitListener) {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem menuDashboard = new JMenuItem("Dashboard");
        JMenuItem menuRequest = new JMenuItem("Request");
        JMenuItem menuManageUsers = new JMenuItem("Pendaftaran Masyarakat");
        JMenuItem menuManageCouriers = new JMenuItem("Pendaftaran Kurir");
        JMenuItem menuHistory = new JMenuItem("History");
        JMenuItem menuExit = new JMenuItem("Exit");

        menuDashboard.addActionListener(dashboardListener);
        menuRequest.addActionListener(requestListener);
        menuManageUsers.addActionListener(manageUsersListener);
        menuManageCouriers.addActionListener(manageCouriersListener);
        menuHistory.addActionListener(historyListener);
        menuExit.addActionListener(exitListener);

        menu.add(menuDashboard);
        menu.add(menuRequest);
        menu.add(menuManageUsers);
        menu.add(menuManageCouriers);
        menu.add(menuHistory);
        menu.addSeparator();
        menu.add(menuExit);

        menuBar.add(menu);

        return menuBar;
    }
}
