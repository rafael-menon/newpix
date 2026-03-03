package newpixclient.service;

import newpixclient.messages.MessageHandler;
import newpixclient.service.listener.GuiEvent;
import newpixclient.json.JsonMessage;
import newpixclient.json.UserJson;
import newpixclient.tcp.TcpSocket;
import newpixclient.type.Name;
import newpixclient.type.Password;

public class TcpService {
	private final TcpSocket tcpSocket;

    public TcpService(TcpSocket tcpSocket) {
        this.tcpSocket = tcpSocket;
    }
	
	public void setGuiListener(GuiEvent guiEvent) {
		tcpSocket.setGuiListener(guiEvent);
	}
	
	public void connect(String ip, String port) throws Exception {
		Integer portInt = Integer.parseInt(port);

		tcpSocket.setIp(ip);
		tcpSocket.setPort(portInt);

        tcpSocket.start();
	}
}
