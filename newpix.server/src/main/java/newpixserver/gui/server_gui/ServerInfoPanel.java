package newpixserver.gui.server_gui;

import newpixserver.service.GuiService;
import newpixserver.service.TcpService;
import newpixserver.utils.PortFilter;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ServerInfoPanel extends JPanel {
    private JButton startStopServerButton;
    private JComboBox<InetAddress> ipList;
    private JTextField portField;
    private JLabel localIpLabel;
    private JLabel serverStatusLabel;
    private JLabel clientsConnectedLabel;

    private ServerGui serverGui;
    private final GuiService guiService;

    public ServerInfoPanel(GuiService guiService) {
        this.guiService = guiService;
        initComponents();
    }

    public void initListeners(GuiService guiService) {
        startStopServerButton.addActionListener(e -> {
            try {
                guiService.getServerPortFromTextField((InetAddress) ipList.getSelectedItem(), portField.getText());
                guiService.startStopServerFromButton();
                updateUIState(guiService);
            } catch(NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "A porta deve conter apenas números.", "Porta inválida", JOptionPane.ERROR_MESSAGE);
                System.out.println("-> Erro: A porta deve conter apenas números.");
            } catch (IllegalArgumentException iae){
                JOptionPane.showMessageDialog(this, iae.getMessage(), "Porta inválida", JOptionPane.ERROR_MESSAGE);
                System.out.println("-> " + iae.getMessage());
            } catch (UnknownHostException uhe) {
                System.out.println("-> Erro ao escutar: " + uhe.getMessage());
            } catch(BindException bex) {
                JOptionPane.showMessageDialog(this, "Porta já utilizada, escolha outra.", "Porta ocupada", JOptionPane.ERROR_MESSAGE);
            } catch (IOException iox) {
                JOptionPane.showMessageDialog(this, "Erro: " + iox.getMessage(), "Falha", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void initComponents() {
        this.setLayout(null);

        JLabel ipLabel = new JLabel("IP:");
        ipLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ipLabel.setBounds(10, 22, 30, 22);

        JPanel connectionPanel = new JPanel();
        connectionPanel.setLayout(null);
        connectionPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Conex\u00E3o", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        connectionPanel.setBounds(10, 8, 385, 60);

        try {
            ipList = new JComboBox<>(guiService.getLocalConnections());
            ipList.setBounds(32, 22, 120, 22);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        JLabel portLabel = new JLabel("Porta:");
        portLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        portLabel.setBounds(160, 22, 46, 22);

        portField = new JTextField("20000");
        portField.setPreferredSize(new Dimension(20, 30));
        portField.setEditable(true);
        portField.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        portField.setBounds(200, 21, 60, 25);
        ((AbstractDocument) portField.getDocument()).setDocumentFilter(new PortFilter());
        connectionPanel.add(portField);

        startStopServerButton = new JButton("Iniciar Servidor");
        startStopServerButton.setFont(new Font("Segoe UI", Font.BOLD, 10));
        startStopServerButton.setBounds(270, 21, 105, 25);

        serverStatusLabel = new JLabel("Status: Offline");
        serverStatusLabel.setForeground(Color.RED);
        serverStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        serverStatusLabel.setBounds(10, 22, 95, 22);

        clientsConnectedLabel = new JLabel("Clientes conectados: 0");
        clientsConnectedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        clientsConnectedLabel.setBounds(96, 22, 154, 22);

        JPanel connectionStatusPanel = new JPanel();
        connectionStatusPanel.setLayout(null);
        connectionStatusPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Status", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        connectionStatusPanel.setBounds(395, 8, 260, 60);

        JPanel hostInfoPanel = new JPanel();
        hostInfoPanel.setLayout(null);
        hostInfoPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Informa\u00E7\u00E3o", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        hostInfoPanel.setBounds(655, 8, 270, 60);

        localIpLabel = new JLabel("Host: Offline");
        localIpLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        localIpLabel.setBounds(9, 22, 240, 22);

        connectionPanel.add(ipLabel);
        connectionPanel.add(ipList);
        connectionPanel.add(portLabel);
        connectionPanel.add(startStopServerButton);
        connectionStatusPanel.add(serverStatusLabel);
        connectionStatusPanel.add(clientsConnectedLabel);
        hostInfoPanel.add(localIpLabel);

        this.add(connectionPanel);
        this.add(connectionStatusPanel);
        this.add(hostInfoPanel);
    }

    public void updateUIState(GuiService guiService) throws UnknownHostException {
        boolean status = guiService.getStartStopServerButtonStatus();
        portField.setEditable(!status);
        startStopServerButton.setText(status ? "Parar Servidor" : "Iniciar Servidor");
        serverStatusLabel.setText(status ? "Status: Online" : "Status: Offline");
        serverStatusLabel.setForeground(status ? (new Color(0, 170, 0)) : Color.RED);
        localIpLabel.setText(status ? "Host: " + guiService.getLocalIp() : "Host: Offline");
    }

    public void updateClientCount(int count) {
        clientsConnectedLabel.setText("Clientes conectados: " + count);
    }
}
