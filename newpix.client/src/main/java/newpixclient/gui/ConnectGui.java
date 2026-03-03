package newpixclient.gui;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;

import newpixclient.service.GuiService;
import newpixclient.utils.IpFilter;
import newpixclient.utils.PortFilter;

import javax.swing.BorderFactory;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class ConnectGui extends JFrame {
	private static final long serialVersionUID = 1L;
	private JMenuBar menuBar;
	private JMenu optionsMenu;
	private JMenuItem menuItemQuit;
	private JPanel contentPane;
	private JLabel ipLabel;
	private JTextField ipTextField;
	private JLabel portLabel;
	private JTextField portTextField;
	private JButton connectButton;
	
	private GuiService guiService;

	public ConnectGui() {
		buildGui();
		initiateListeners();
	}
	
	public void injectController(GuiService guiService) {
		this.guiService = guiService;
	}
	
	@SuppressWarnings("unused")
	private void initiateListeners() {
		connectButton.addActionListener(e -> {
			String ip = ipTextField.getText();
			String port = portTextField.getText();

			try {
				guiService.connect(ip, port);
				connectButton.setEnabled(false);
				connectButton.setText("Conectando...");
			} catch(IllegalArgumentException iae) {
				connectButton.setEnabled(true);
				connectButton.setText("Conectar-se");
				JOptionPane.showMessageDialog(this, iae.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);		
			} catch(Exception ex) {
				connectButton.setEnabled(true);
				connectButton.setText("Conectar-se");
				JOptionPane.showMessageDialog(this, "Preencha os campos corretamente!", "Alerta", JOptionPane.WARNING_MESSAGE);		
			}
		});	
		
		menuItemQuit.addActionListener(e -> {
			System.exit(1);
		});
	}
	
	public void updateConnectionButton(boolean connected) {		
		connectButton.setEnabled(connected ? false : true);
		connectButton.setText(connected ? "Conectando..." : "Conectar-se");
	}
	
	private void buildGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 250);
		this.setTitle("NewPix - Conecte-se");
		this.setIconImage(loadWindowIcon());
		this.setResizable(false);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		Font textFieldFont = new Font("SegoeUI", Font.PLAIN, 20);
		Font labelFont = new Font("SegoeUI", Font.PLAIN, 15);
		Font buttonFont = new Font("SegoeUI", Font.BOLD, 12);
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
			
		menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 384, 22);	
		optionsMenu = new JMenu("Opções");	
		menuItemQuit = new JMenuItem("Sair");
		
		ipLabel = new JLabel();
		ipLabel.setText("IP");
		ipLabel.setFont(labelFont);
		ipLabel.setSize(30, 20);
		ipLabel.setLocation(37, 49);
		
		ipTextField = new JTextField();	
		((AbstractDocument) ipTextField.getDocument()).setDocumentFilter(new IpFilter());

        ipTextField.setText("192.168.1.11");

		ipTextField.setFont(textFieldFont);
		ipTextField.setBorder(BorderFactory.createEtchedBorder());
		ipTextField.setMargin(new Insets(0, 6, 0, 6));
		ipTextField.setLocation(36, 70);
		ipTextField.setSize(200, 40);
		
		portLabel = new JLabel();
		portLabel.setText("Port");
		portLabel.setFont(labelFont);
		portLabel.setSize(30, 20);
		portLabel.setLocation(246, 49);
		
		portTextField = new JTextField();
		((AbstractDocument) portTextField.getDocument()).setDocumentFilter(new PortFilter());

        portTextField.setText("20000");

		portTextField.setFont(textFieldFont);
		portTextField.setBorder(BorderFactory.createEtchedBorder());
		portTextField.setMargin(new Insets(0, 6, 0, 6));
		portTextField.setLocation(245, 70);
		portTextField.setSize(100, 40);
		
		connectButton = new JButton();
		connectButton.setText("Conectar-se");
		connectButton.setFont(buttonFont);
		connectButton.setFocusPainted(false);
		connectButton.setBorder(BorderFactory.createRaisedBevelBorder());
		connectButton.setSize(110, 35);
		connectButton.setLocation(130, 140);
		
		menuBar.add(optionsMenu);	
		optionsMenu.add(menuItemQuit);
		
		contentPane.add(menuBar);	
		contentPane.add(ipLabel);
		contentPane.add(ipTextField);
		contentPane.add(portLabel);
		contentPane.add(portTextField);
		contentPane.add(connectButton);	
	}
	
	private Image loadWindowIcon() {
		URL iconURL = getClass().getResource("/pix.png");
		
		if (iconURL != null) {
			Image icon = Toolkit.getDefaultToolkit().getImage(iconURL);
			return icon;
		}
		
		return null;
	}
}
