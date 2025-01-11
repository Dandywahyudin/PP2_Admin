package View;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MenuBar {
    public JMenuBar createMenuBar(ActionListener dashboardListener, ActionListener requestListener,
                                  ActionListener manageUsersListener, ActionListener manageCouriersListener,
                                  ActionListener historyListener, ActionListener requestUserListener, ActionListener requestCourierListener, ActionListener exitListener) {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem menuDashboard = new JMenuItem("Dashboard");
        JMenuItem menuRequest = new JMenuItem("Permintaan");
        JMenuItem menuManageUsers = new JMenuItem("Pendaftaran Masyarakat");
        JMenuItem menuManageCouriers = new JMenuItem("Pendaftaran Kurir");
        JMenuItem requestUser = new JMenuItem("Permintaan Masyarakat");
        JMenuItem requestCourier = new JMenuItem("Permintaan Kurir");
        JMenuItem menuHistory = new JMenuItem("Histori");
        JMenuItem menuExit = new JMenuItem("Exit");

        menuDashboard.addActionListener(dashboardListener);
        menuRequest.addActionListener(requestListener);
        menuManageUsers.addActionListener(manageUsersListener);
        menuManageCouriers.addActionListener(manageCouriersListener);
        requestUser.addActionListener(requestUserListener);
        requestCourier.addActionListener(requestCourierListener);
        menuHistory.addActionListener(historyListener);
        menuExit.addActionListener(exitListener);

        menu.add(menuDashboard);
        menu.add(menuRequest);
        menu.add(menuManageUsers);
        menu.add(menuManageCouriers);
        menu.add(requestCourier);
        menu.add(requestUser);
        menu.add(menuHistory);
        menu.addSeparator();
        menu.add(menuExit);

        menuBar.add(menu);

        return menuBar;
    }
}
