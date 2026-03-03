package newpixclient.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import newpixclient.service.GuiService;
import newpixclient.json.TransactionViewModel;

import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class ExtractGui extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private GuiService guiService;
	private JTable table;
	private JButton searchButton;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;

	public ExtractGui(GuiService guiService) {
		this.guiService = guiService;
		buildGui();
		startListeners();
	}

    public void startListeners() {
        searchButton.addActionListener(e -> {
            Date start = (Date) startDateSpinner.getValue();
            Date end = (Date) endDateSpinner.getValue();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            String startDateStr = sdf.format(start);
            String endDateStr = sdf.format(end);

            try {
                guiService.searchExtract(startDateStr, endDateStr);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao buscar: " + ex.getMessage());
            }
        });
    }

    public void buildGui() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 700); // Tamanho inicial sugerido
        setTitle("NewPix - Extrato");
        setIconImage(loadWindowIcon());
        setLocationRelativeTo(null);

        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filtrar por Período"));

        SpinnerDateModel startModel = new SpinnerDateModel();
        SpinnerDateModel endModel = new SpinnerDateModel();

        startDateSpinner = new JSpinner(startModel);
        configureDateSpinner(startDateSpinner);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 0);
        startDateSpinner.setValue(cal.getTime());

        endDateSpinner = new JSpinner(endModel);
        configureDateSpinner(endDateSpinner);
        endDateSpinner.setValue(new Date());

        searchButton = new JButton("Pesquisar");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchButton.setBackground(new Color(70, 130, 180));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setPreferredSize(new Dimension(120, 30));

        filterPanel.add(new JLabel("Data Inicial:"));
        filterPanel.add(startDateSpinner);
        filterPanel.add(new JLabel("Data Final:"));
        filterPanel.add(endDateSpinner);
        filterPanel.add(searchButton);

        contentPane.add(filterPanel, BorderLayout.NORTH);

        table = new JTable();
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);

        table.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] { "Data", "Tipo", "Valor" }
        ) {
            Class[] columnTypes = new Class[] { String.class, Object.class, Object.class };
            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        contentPane.add(scrollPane, BorderLayout.CENTER);
    }

    private void configureDateSpinner(JSpinner spinner) {
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
        spinner.setEditor(editor);
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinner.setPreferredSize(new Dimension(120, 30));
    }

    public void createTable(List<TransactionViewModel> transactions) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        if (transactions != null && !transactions.isEmpty()) {
            for (TransactionViewModel t : transactions) {
                model.addRow(new Object[]{
                        t.getDate(),
                        t.getType(),
                        String.format("R$ %.2f", t.getValue())
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nenhuma transação encontrada no período.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private Image loadWindowIcon() {
        URL iconURL = getClass().getResource("/pix.png");
        if (iconURL != null) {
            return Toolkit.getDefaultToolkit().getImage(iconURL);
        }
        return null;
    }
}