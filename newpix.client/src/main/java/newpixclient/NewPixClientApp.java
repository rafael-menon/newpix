package newpixclient;

import java.awt.EventQueue;
import java.io.IOException;

import newpixclient.messages.MessageHandler;
import newpixclient.service.GuiService;
import newpixclient.gui.ConnectGui;
import newpixclient.service.ServiceManager;
import newpixclient.service.ClientService;
import newpixclient.service.TcpService;
import newpixclient.tcp.TcpSocket;

public class NewPixClientApp {
	public static void main(String[] args) throws IOException {
        final ServiceManager serviceManager = new ServiceManager();
        final TcpSocket tcpSocket = new TcpSocket();
        final MessageHandler messageHandler = new MessageHandler(serviceManager, tcpSocket);
        tcpSocket.setMessageHandler(messageHandler);
        final TcpService tcpService = new TcpService(tcpSocket);

        ConnectGui connectGui = new ConnectGui();
        final GuiService guiService = new GuiService(tcpService, connectGui, messageHandler);

        final ClientService clientService = new ClientService(guiService);

        serviceManager.registerService(ClientService.class, clientService);

		guiService.startListener();
		
		connectGui.injectController(guiService);
		
		EventQueue.invokeLater(() -> {
            try {
                connectGui.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
	}
}
