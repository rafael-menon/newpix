package newpixserver;

import java.awt.EventQueue;
import java.io.IOException;

import newpixserver.database.dao.TransactionDao;
import newpixserver.service.*;
import newpixserver.database.dao.UserDao;
import newpixserver.gui.Log;
import newpixserver.gui.server_gui.ServerGui;
import newpixserver.tcp.TcpSocket;

public class NewPixServerApp {
	public static void main(String[] args) throws IOException {
        final UserDao userDao = new UserDao();
        final TransactionDao transactionDao = new TransactionDao();

		final UserService userService = new UserService(userDao, transactionDao);
        final AdminService adminService = new AdminService(userDao, transactionDao);
        final AlertService alertService = new AlertService();

        final ServiceManager serviceManager = new ServiceManager();
        serviceManager.registerService(UserService.class, userService);
        serviceManager.registerService(AlertService.class, alertService);

        final ClientService clientService = new ClientService(serviceManager);

        final TcpSocket tcpSocket = new TcpSocket(clientService);

        final TcpService tcpService = new TcpService(tcpSocket);

		final GuiService guiService = new GuiService(tcpService, adminService);

		final ServerGui serverGui = new ServerGui(guiService);

        userService.addListener(guiService);
        clientService.addListener(guiService);
        Log.addListener(guiService);
        alertService.addListener(guiService);

		guiService.setGui(serverGui);
        guiService.updateRegisteredTableList();
        guiService.updateTransactionTableList();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
                    serverGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
