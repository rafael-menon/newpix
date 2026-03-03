package newpixclient.gui;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;

import newpixclient.service.GuiService;
import newpixclient.type.Name;
import newpixclient.type.Password;
import newpixclient.utils.CpfFilter;

public class UserGui extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu optionsMenu;
	private JMenuItem menuItemLogout;
	private JMenuItem menuItemDeleteAccount;
	private JLabel usernameLabel;
	private JLabel cpfLabel;
	private JLabel saldoLabel;
	private JTextField alterNameTextField;
	private JLabel alterNameLabel;
	private JPasswordField alterPasswordTextField;
	private JLabel alterPasswordLabel;
	private JButton alterButton;
	private JLabel depositLabel;
	private JButton depositButton;
	private JLabel transactionLabel;
	private JButton transactionButton;
	private JTextField depositTextField;
	private JTextField transactionValueTextField;
	private JTextField transactionCpfTextField;
	private JButton viewStatementButton;
    private JPanel mainContentWrapper;

    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 18);
    private final Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font dataFont = new Font("Consolas", Font.BOLD, 15);

	private GuiService guiService;

	public UserGui(GuiService guiService) {
		this.guiService = guiService;
		loadComponents();
		loadListeners();
        setupResponsiveLayout();
	}

	@SuppressWarnings("unused")
	private void loadListeners() {
		alterButton.addActionListener(e -> {
			try {
				Name name = new Name(alterNameTextField.getText(), "update");
				Password password = new Password(alterPasswordTextField.getPassword(), "update");

				guiService.userUpdate(name, password);
				alterNameTextField.setText("");
				alterPasswordTextField.setText("");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Alerta", JOptionPane.WARNING_MESSAGE);
			}
		});

		menuItemDeleteAccount.addActionListener(e -> {
			try {
				guiService.userDelete();
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage(), "Alerta", JOptionPane.WARNING_MESSAGE);
			}
		});

		menuItemLogout.addActionListener(e -> {
			try {
				guiService.userLogout();
			} catch(Exception e2) {

			}
		});

        depositButton.addActionListener(e -> {
            try {
                String value = depositTextField.getText();
                guiService.deposit(value);
                depositTextField.setText("");
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        });

        transactionButton.addActionListener(e -> {
            try {
                String value = transactionValueTextField.getText();
                String cpfReceiver = transactionCpfTextField.getText();
                guiService.createTransaction(value, cpfReceiver);
                transactionCpfTextField.setText("");
                transactionValueTextField.setText("");
            } catch(Exception e4) {
                e4.printStackTrace();
            }
        });

        viewStatementButton.addActionListener(e -> {
        	guiService.createExtractGui();
        });
	}

	public void updateUserData(String username, String cpf, Double saldo) {
        usernameLabel.setText("<html>Nome: <b>" + username + "</b></html>");
        cpfLabel.setText("<html>CPF: <b>" + cpf + "</b></html>");

        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String saldoFormatado = nf.format(saldo);
        saldoLabel.setText("<html>Saldo: <font color='green'><b>" + saldoFormatado + "</b></font></html>");
	}

    private void loadComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setTitle("NewPix");
        setIconImage(loadWindowIcon());
        setResizable(true);

        menuBar = new JMenuBar();
        optionsMenu = new JMenu("Opções");
        menuItemLogout = new JMenuItem("Logout");
        menuItemDeleteAccount = new JMenuItem("Deletar conta");
        optionsMenu.add(menuItemDeleteAccount);
        optionsMenu.add(menuItemLogout);
        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);

        mainContentWrapper = new JPanel(new BorderLayout());
        mainContentWrapper.setBackground(new Color(245, 245, 250));
        setContentPane(mainContentWrapper);

        contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(new Color(245, 245, 250));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainContentWrapper.add(contentPane, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        JPanel userPanel = createStyledPanel("Dados da Conta");
        userPanel.setLayout(new GridLayout(3, 1, 5, 10));

        usernameLabel = new JLabel("Nome: Carregando...");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        usernameLabel.setBorder(new EmptyBorder(0, 5, 0, 0));

        cpfLabel = new JLabel("CPF: ---");
        cpfLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        cpfLabel.setBorder(new EmptyBorder(0, 5, 0, 0));

        saldoLabel = new JLabel("Saldo: ---");
        saldoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        saldoLabel.setBorder(new EmptyBorder(0, 5, 0, 0));

        userPanel.add(usernameLabel);
        userPanel.add(cpfLabel);
        userPanel.add(saldoLabel);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.4; gbc.weighty = 0.3;
        contentPane.add(userPanel, gbc);

        JPanel updatePanel = createStyledPanel("Atualizar Cadastro");
        updatePanel.setLayout(new GridBagLayout());
        GridBagConstraints uGbc = new GridBagConstraints();
        uGbc.insets = new Insets(5, 5, 5, 5);
        uGbc.fill = GridBagConstraints.HORIZONTAL;
        uGbc.weightx = 1.0;

        alterNameLabel = new JLabel("Novo Nome");
        alterNameLabel.setFont(labelFont);
        uGbc.gridx = 0; uGbc.gridy = 0;
        updatePanel.add(alterNameLabel, uGbc);

        alterNameTextField = new JTextField();
        styleTextField(alterNameTextField);
        uGbc.gridy = 1;
        updatePanel.add(alterNameTextField, uGbc);

        alterPasswordLabel = new JLabel("Nova Senha");
        alterPasswordLabel.setFont(labelFont);
        uGbc.gridy = 2;
        updatePanel.add(alterPasswordLabel, uGbc);

        alterPasswordTextField = new JPasswordField();
        styleTextField(alterPasswordTextField);
        uGbc.gridy = 3;
        updatePanel.add(alterPasswordTextField, uGbc);

        alterButton = createStyledButton("Salvar Alterações", new Color(70, 130, 180));
        uGbc.gridy = 4;
        uGbc.insets = new Insets(15, 5, 5, 5);
        updatePanel.add(alterButton, uGbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weighty = 0.7;
        contentPane.add(updatePanel, gbc);

        JPanel operationsPanel = createStyledPanel("Operações Financeiras");
        operationsPanel.setLayout(new GridBagLayout());

        transactionLabel = new JLabel("Realizar PIX / Transferência");
        transactionLabel.setFont(titleFont);
        uGbc.gridy = 0; uGbc.insets = new Insets(5, 5, 10, 5);
        operationsPanel.add(transactionLabel, uGbc);

        JLabel lblValor = new JLabel("Valor (R$)");
        lblValor.setFont(labelFont);
        uGbc.gridy = 1; uGbc.insets = new Insets(5, 5, 2, 5);
        operationsPanel.add(lblValor, uGbc);

        transactionValueTextField = new JTextField();
        styleTextField(transactionValueTextField);
        uGbc.gridy = 2; uGbc.insets = new Insets(0, 5, 10, 5);
        operationsPanel.add(transactionValueTextField, uGbc);

        JLabel lblCpf = new JLabel("CPF do Destinatário");
        lblCpf.setFont(labelFont);
        uGbc.gridy = 3; uGbc.insets = new Insets(5, 5, 2, 5);
        operationsPanel.add(lblCpf, uGbc);

        transactionCpfTextField = new JTextField();
        ((AbstractDocument) transactionCpfTextField.getDocument()).setDocumentFilter(new CpfFilter());
        styleTextField(transactionCpfTextField);
        uGbc.gridy = 4; uGbc.insets = new Insets(0, 5, 15, 5);
        operationsPanel.add(transactionCpfTextField, uGbc);

        transactionButton = createStyledButton("Confirmar Transação", new Color(34, 139, 34));
        uGbc.gridy = 5;
        operationsPanel.add(transactionButton, uGbc);

        JSeparator sep = new JSeparator();
        uGbc.gridy = 6; uGbc.insets = new Insets(20, 5, 20, 5);
        operationsPanel.add(sep, uGbc);

        depositLabel = new JLabel("Realizar Depósito");
        depositLabel.setFont(titleFont);
        uGbc.gridy = 7; uGbc.insets = new Insets(5, 5, 10, 5);
        operationsPanel.add(depositLabel, uGbc);

        depositTextField = new JTextField();
        styleTextField(depositTextField);
        uGbc.gridy = 8;
        operationsPanel.add(depositTextField, uGbc);

        depositButton = createStyledButton("Depositar", new Color(70, 130, 180));
        uGbc.gridy = 9; uGbc.insets = new Insets(10, 5, 20, 5);
        operationsPanel.add(depositButton, uGbc);

        viewStatementButton = createStyledButton("Ver Extrato Completo", new Color(100, 100, 100));
        uGbc.gridy = 10; uGbc.weighty = 0.8;
        uGbc.anchor = GridBagConstraints.SOUTH;
        operationsPanel.add(viewStatementButton, uGbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.6; gbc.weighty = 1.0;
        contentPane.add(operationsPanel, gbc);
    }

    private JPanel createStyledPanel(String title) {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(100, 100, 100)
        ));
        return p;
    }

    private void styleTextField(JTextField tf) {
        tf.setFont(fieldFont);
        tf.setPreferredSize(new Dimension(0, 35));
        tf.setMargin(new Insets(5, 2, 5, 2));
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 40));
        return btn;
    }

    private void setupResponsiveLayout() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();

                int leftRightMargin;
                int topBottomMargin;

                if (width > 1600) {
                    leftRightMargin = (width - 1200) / 2;
                } else if(width > 1200) {
                    leftRightMargin = 80;
                } else {
                    leftRightMargin = 20;
                }

                if(height > 600) {
                   topBottomMargin = (height - 600) / 2;
                } else {
                    topBottomMargin = 0;
                }

                contentPane.setBorder(new EmptyBorder(topBottomMargin, leftRightMargin, topBottomMargin, leftRightMargin));
                contentPane.revalidate();
            }
        });
    }

    private Image loadWindowIcon() {
        URL iconURL = getClass().getResource("/pix.png");
        if (iconURL != null) {
            return Toolkit.getDefaultToolkit().getImage(iconURL);
        }
        return null;
    }
}
