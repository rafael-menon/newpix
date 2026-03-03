package newpixserver.service;

import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.swing.*;

import newpixserver.database.entities.Transaction;
import newpixserver.database.entities.User;
import newpixserver.gui.server_gui.ServerGui;
import newpixserver.service.listener.*;
import newpixserver.tcp.ConnectedClient;
import newpixserver.type.IsoDate;

public class GuiService implements LogEvent, UserServiceListener, ClientServiceListener, AlertListener {
	private final AdminService adminService;
    private final TcpService tcpService;
	private ServerGui serverGui;

    private List<User> userList;
    private List<Transaction> transactionList;

    public GuiService(TcpService tcpService, AdminService adminService) {
        this.tcpService = tcpService;
        this.adminService = adminService;
        this.userList = new ArrayList<>();
        this.transactionList = new ArrayList<>();
    }
	
	public void setGui(ServerGui serverGui) {
		this.serverGui = serverGui;
	}

    public InetAddress[] getLocalConnections() throws SocketException {
        return tcpService.getLocalConnections();
    }

	public String getLocalIp() {
        return tcpService.getLocalIp();
	}
	 
	public void getServerPortFromTextField(InetAddress ip, String serverPortStr) throws UnknownHostException, SocketException {
        tcpService.buildAddress(ip, serverPortStr);
	}
	
	public void startStopServerFromButton() throws IOException {
        tcpService.startStopServer();
	}	
		
	public boolean getStartStopServerButtonStatus() {
        return tcpService.isServerRunning();
	}

	public void updateConnectedClientList(Map<String, ConnectedClient> clientMap ) {
        if(clientMap == null) {
            SwingUtilities.invokeLater(() -> {
                serverGui.updateConnectedClientsTable(null);
                serverGui.updateClientCount(0);
            });
        } else {
            List<ConnectedClient> connectedClients = new ArrayList<>(clientMap.values());

            SwingUtilities.invokeLater(() -> {
                serverGui.updateConnectedClientsTable(connectedClients);
                serverGui.updateClientCount(clientMap.size());
            });
        }
	}

	public void updateRegisteredTableList() {
        try {
            userList = adminService.getAllUsers();

            SwingUtilities.invokeLater(() -> {
                serverGui.loadRegisteredUserList(userList);
            });
        } catch (SQLException e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(serverGui,
                        "Falha ao carregar a lista de usuários do banco de dados.\nErro: " + e.getMessage(),
                        "Erro de Banco de Dados",
                        JOptionPane.ERROR_MESSAGE);
            });
        }
	}

    public void updateTransactionTableList() {
        try {
            transactionList = adminService.getAllTransactions();

            for(Transaction transaction : transactionList) {
                transaction.setDate(IsoDate.formatDate(transaction.getDate()));
            }

            SwingUtilities.invokeLater(() -> {
                serverGui.loadTransactionList(transactionList);
            });
        } catch (SQLException e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(serverGui,
                        "Falha ao carregar a lista de transações do banco de dados.\nErro: " + e.getMessage(),
                        "Erro de Banco de Dados",
                        JOptionPane.ERROR_MESSAGE);
            });
        }
    }

    @Override
	public void logUpdate(String message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String timestamp = dtf.format(LocalDateTime.now());
	    String formattedMessage = String.format("[%s] %s", timestamp, message);
		
	    SwingUtilities.invokeLater(() -> {
	    	serverGui.updateLog(formattedMessage);
	    });
	}

    @Override
    public void onUserServiceEvent(UserServiceEvent event) {
        switch(event) {
            case USER_LIST_CHANGED -> updateRegisteredTableList();
            case TRANSACTION_LIST_CHANGED -> updateTransactionTableList();
        }
    }

    @Override
    public void onClientServiceEvent(ClientServiceEvent event, Map<String, ConnectedClient> clientMap) {
        switch(event) {
            case CONNECTED_LIST_CHANGED -> updateConnectedClientList(clientMap);
        }
    }

    @Override
    public void onServerAlert(String title, String message) {
        SwingUtilities.invokeLater(() -> {
            if (serverGui != null) {
                JOptionPane.showMessageDialog(serverGui, message, title, JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}
