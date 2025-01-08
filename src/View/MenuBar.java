package View;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MenuBar {
    public JMenuBar createMenuBar(ActionListener dashboardListener, ActionListener requestListener,
                                  ActionListener manageUsersListener, ActionListener manageCouriersListener,
                                  ActionListener exitListener) {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem menuDashboard = new JMenuItem("Dashboard");
        JMenuItem menuRequest = new JMenuItem("Request");
        JMenuItem menuManageUsers = new JMenuItem("Pendaftaran Masyarakat");
        JMenuItem menuManageCouriers = new JMenuItem("Pendaftaran Kurir");
        JMenuItem menuExit = new JMenuItem("Exit");

        menuDashboard.addActionListener(dashboardListener);
        menuRequest.addActionListener(requestListener);
        menuManageUsers.addActionListener(manageUsersListener);
        menuManageCouriers.addActionListener(e -> new CourierFrame());
        menuExit.addActionListener(exitListener);

        menu.add(menuDashboard);
        menu.add(menuRequest);
        menu.add(menuManageUsers);
        menu.add(menuManageCouriers);
        menu.addSeparator();
        menu.add(menuExit);

        menuBar.add(menu);



        return menuBar;
    }
}
