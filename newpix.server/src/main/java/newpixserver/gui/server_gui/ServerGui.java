package newpixserver.gui.server_gui;

import java.io.Serial;
import java.net.*;
import java.util.List;

import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;

import newpixserver.database.entities.Transaction;
import newpixserver.service.GuiService;
import newpixserver.database.entities.User;
import newpixserver.tcp.ConnectedClient;

public class ServerGui extends JFrame {
	@Serial
    private static final long serialVersionUID = 1L;

	final private GuiService guiService;

    private ServerInfoPanel serverInfoPanel;
    private ServerTabbedPane serverTabbedPane;

	public ServerGui(GuiService guiService) {
		this.guiService = guiService;
		initComponents();
        initListeners();
	}

    private void initListeners() {
        serverInfoPanel.initListeners(guiService);
        serverTabbedPane.initListeners();
    }

	private void initComponents() { 
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(100, 100, 950, 780);
	    
	    URL iconURL = getClass().getResource("/pix.png");
	    if (iconURL != null) {
            Image icon = Toolkit.getDefaultToolkit().getImage(iconURL);
            setIconImage(icon);
        } else {
            System.err.println("-> Erro: ícone não encontrado");
        }
	    
	    this.setTitle("NewPix - Server");

        JPanel contentPane = new JPanel();
	    setContentPane(contentPane);

	    GridBagLayout gbl_contentPane = new GridBagLayout();
	    gbl_contentPane.columnWidths = new int[]{0, 0};
	    gbl_contentPane.rowHeights = new int[]{80, 0, 0};
	    gbl_contentPane.columnWeights = new double[]{1, Double.MIN_VALUE};
	    gbl_contentPane.rowWeights = new double[]{0.0, 1, Double.MIN_VALUE};
	    contentPane.setLayout(gbl_contentPane);

        serverInfoPanel = new ServerInfoPanel(guiService);
        GridBagConstraints gbc_serverInfoPanel = new GridBagConstraints();
        gbc_serverInfoPanel.anchor = GridBagConstraints.NORTH;
        gbc_serverInfoPanel.fill = GridBagConstraints.BOTH;
        gbc_serverInfoPanel.insets = new Insets(0, 0, 5, 0);
        gbc_serverInfoPanel.gridx = 0;
        gbc_serverInfoPanel.gridy = 0;

        serverTabbedPane = new ServerTabbedPane(JTabbedPane.TOP);
	    GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
	    gbc_tabbedPane.fill = GridBagConstraints.BOTH;  
	    gbc_tabbedPane.gridx = 0;
	    gbc_tabbedPane.gridy = 1;

        contentPane.add(serverInfoPanel, gbc_serverInfoPanel);
	    contentPane.add(serverTabbedPane, gbc_tabbedPane);
    }

	public void updateClientCount(int count) {
        serverInfoPanel.updateClientCount(count);
	}

	public void updateConnectedClientsTable(List<ConnectedClient> clients) {
        serverTabbedPane.updateConnectedClientsTable(clients);
	}
	
	public void updateHostnameInTable(int rowIndex, String hostname) {
        serverTabbedPane.updateHostnameInTable(rowIndex, hostname);
	}
	
	public void updateNameInTable(int rowIndex, String name) {
        serverTabbedPane.updateNameInTable(rowIndex, name);
	}
	
	public void loadRegisteredUserList(List<User> users) {
        serverTabbedPane.loadRegisteredUserList(users);
	}

    public void loadTransactionList(List<Transaction> transactions) {
        serverTabbedPane.loadTransactionList(transactions);
    }
	
	public void updateLog(String message) {
        serverTabbedPane.updateLog(message);
	}
}
