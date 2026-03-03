package newpixserver.gui.server_gui;

import newpixserver.database.entities.Transaction;
import newpixserver.database.entities.User;
import newpixserver.tcp.ConnectedClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ServerTabbedPane extends JTabbedPane {
    private JTable connectedClientsTable;
    private JTable registeredClientsTable;
    private JTable transactionsTable;
    private JTextArea logArea;
    private JButton clearLogButton;

    public ServerTabbedPane(int tabPlacement) {
        super(tabPlacement, WRAP_TAB_LAYOUT);
        initComponents();
    }

    public void initListeners() {
        clearLogButton.addActionListener(e -> {
            clearLog();
        });
    }

    private void initComponents() {
        this.setBorder(null);
        connectedClientsTable = new JTable();
        connectedClientsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        connectedClientsTable.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        connectedClientsTable.setModel(new DefaultTableModel(
                new Object[][] {}, new String[] {"IP", "Port", "Hostname", "Nome"}) {
                    @SuppressWarnings("rawtypes")
                    final Class[] columnTypes = new Class[] {String.class, Integer.class, String.class, String.class};
                    @SuppressWarnings({ "rawtypes"})
                    public Class getColumnClass(int columnIndex) { return columnTypes[columnIndex]; }
                }
        );

        registeredClientsTable = new JTable();
        registeredClientsTable.setModel(new DefaultTableModel(
                new Object[][] {}, new String[] {"ID", "CPF", "Nome", "Saldo"}) {
                    @SuppressWarnings("rawtypes")
                    final Class[] columnTypes = new Class[] { int.class, String.class, String.class, double.class};
                    @SuppressWarnings({ "rawtypes"})
                    public Class getColumnClass(int columnIndex) { return columnTypes[columnIndex]; }
                }
        );
        registeredClientsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        transactionsTable = new JTable();
        transactionsTable.setModel(new DefaultTableModel(
                new Object[][] {}, new String[] {"Nome do remetente", "CPF do remetente", "Nome do destinatário", "CPF do destinatário", "Valor (R$)", "Data"}) {
                    @SuppressWarnings("rawtypes")
                    final Class[] columnTypes = new Class[] { String.class, String.class, String.class, String.class, double.class, String.class };
                    @SuppressWarnings({ "rawtypes"})
                    public Class getColumnClass(int columnIndex) { return columnTypes[columnIndex]; }
                }
        );
        transactionsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel logPanel = new JPanel(new BorderLayout());
        logArea = new JTextArea();
        logArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        logArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        logArea.setEditable(false);

        JScrollPane connectedClientsScrollPane = new JScrollPane(connectedClientsTable);
        JScrollPane registeredClientsScrollPane = new JScrollPane(registeredClientsTable);
        JScrollPane transactionsScrollPane = new JScrollPane(transactionsTable);
        JScrollPane logScrollPane = new JScrollPane(logArea);
        clearLogButton = new JButton("Limpar Log");

        logPanel.add(logScrollPane, BorderLayout.CENTER);
        logPanel.add(clearLogButton, BorderLayout.SOUTH);

        this.addTab("Clientes conectados", null, connectedClientsScrollPane, null);
        this.addTab("Contas registradas", null, registeredClientsScrollPane, null);
        this.addTab("Transações", null, transactionsScrollPane, null);
        this.addTab("Log", null, logPanel, "Log de eventos");
    }

    public void updateConnectedClientsTable(java.util.List<ConnectedClient> clients) {
        DefaultTableModel model = (DefaultTableModel) connectedClientsTable.getModel();
        model.setRowCount(0);

        if (clients != null) {
            for (int i = clients.size() - 1; i >= 0; i--) {
                ConnectedClient client = clients.get(i);
                model.addRow(new Object[]{
                        client.getIp(),
                        client.getPort(),
                        client.getHostname(),
                        client.getName()
                });
            }
        } else {
            model.setRowCount(0);
        }
    }

    public void updateHostnameInTable(int rowIndex, String hostname) {
        DefaultTableModel model = (DefaultTableModel) connectedClientsTable.getModel();
        if (rowIndex >= 0 && rowIndex < model.getRowCount()) {
            model.setValueAt(hostname, rowIndex, 2);
        }
    }

    public void updateNameInTable(int rowIndex, String name) {
        DefaultTableModel model = (DefaultTableModel) connectedClientsTable.getModel();
        if (rowIndex >= 0 && rowIndex < model.getRowCount()) {
            model.setValueAt(name, rowIndex, 3);
        }
    }

    public void loadRegisteredUserList(List<User> users) {
        DefaultTableModel model = (DefaultTableModel) registeredClientsTable.getModel();
        model.setRowCount(0);

        if (users != null) {
            for (User user : users) {
                model.addRow(new Object[]{
                        user.getId(),
                        user.getCpf(),
                        user.getName(),
                        user.getBalance()
                });
            }
        }
    }

    public void loadTransactionList(List<Transaction> transactions) {
        DefaultTableModel model = (DefaultTableModel) transactionsTable.getModel();
        model.setRowCount(0);

        if (transactions != null) {
            for (Transaction transaction : transactions) {
                model.addRow(new Object[]{
                        transaction.getSenderName(),
                        transaction.getSenderCpf(),
                        transaction.getReceiverName(),
                        transaction.getReceiverCpf(),
                        transaction.getAmount(),
                        transaction.getDate()
                });
            }
        }
    }

    public void updateLog(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void clearLog() {
        logArea.setText("");
    }
}
