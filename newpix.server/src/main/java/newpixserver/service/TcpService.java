package newpixserver.service;

import newpixserver.tcp.TcpSocket;
import newpixserver.type.ServerPort;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class TcpService {
    private final TcpSocket tcpSocket;

    private InetSocketAddress serverAddress;
    private boolean isServerRunning = false;

    public TcpService(TcpSocket tcpSocket) {
        this.tcpSocket = tcpSocket;
    }

    public InetAddress[] getLocalConnections() throws SocketException {
        List<Inet4Address> addresses = new ArrayList<Inet4Address>();

        Enumeration<NetworkInterface> interfaceList = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface networkInterface : Collections.list(interfaceList)) {
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                    addresses.add((Inet4Address) inetAddress);
                }
            }
        }

        return addresses.toArray(new InetAddress[0]);
    }

    public void buildAddress(InetAddress ip, String serverPortStr) {
        ServerPort serverPort = new ServerPort(Integer.parseInt(serverPortStr));

        serverAddress = new InetSocketAddress(ip, serverPort.getServerPort());
    }

    public void startStopServer() throws IOException {
        if (!isServerRunning) {
            if (serverAddress == null) {
                throw new IllegalStateException("O endereço do servidor não foi configurado antes de iniciar.");
            }
            tcpSocket.startServerSocket(serverAddress);
            isServerRunning = true;
        } else {
            tcpSocket.stopServerSocket();
            isServerRunning = false;
        }
    }

    public boolean isServerRunning() {
        return isServerRunning;
    }

    public String getLocalIp() {
        return isServerRunning && serverAddress != null ? serverAddress.toString() : "Offline";
    }
}
