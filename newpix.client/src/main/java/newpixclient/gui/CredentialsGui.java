package newpixclient.gui;

import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;

import newpixclient.service.GuiService;
import newpixclient.type.Cpf;
import newpixclient.type.Name;
import newpixclient.type.Password;
import newpixclient.utils.CpfFilter;

import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;

public class CredentialsGui extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel loginPane;
	private JPanel registerPane;
	private JMenuBar menuBar;
	private JMenu optionsMenu;
	private JMenuItem menuItemQuit;
	private JButton returnButton;
	private JLabel titleLabel;
	private JFormattedTextField cpfTextFieldLogin;
	private JFormattedTextField cpfTextFieldRegister;
	private JLabel cpfLabel;
	private JTextField nameTextField;
	private JLabel nameLabel;
	private JPasswordField passwordTextFieldLogin;
	private JPasswordField passwordTextFieldRegister;
	private JLabel passwordLabel;
	private JButton loginButton;
	private JButton registerButton;
	private JButton registerButtonSend;
	
	private Font textFieldFont = new Font("SegoeUI", Font.PLAIN, 20);
	private Font passwordFieldFont = new Font("SegoeUI", Font.PLAIN, 13);
	private Font labelFont = new Font("SegoeUI", Font.PLAIN, 15);
	private Font buttonFont = new Font("SegoeUI", Font.BOLD, 12);
	
	private GuiService guiService;
	
	public CredentialsGui(GuiService guiService) {
		this.guiService = guiService;
		buildGui();
	}
	
	private void buildGui() {
		buildLoginPane();
		buildRegisterPane();
		initiateListeners();
	}

	private void initiateListeners() {
		registerButtonSend.addActionListener(e -> {				
			try {
				Cpf cpf = new Cpf(cpfTextFieldRegister.getText());
				Name name = new Name(nameTextField.getText());
				Password password = new Password(passwordTextFieldRegister.getPassword());
			
				guiService.userCreate(cpf.getString(), name.getString(), password.getString());
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Alerta", JOptionPane.WARNING_MESSAGE);				
			}
		});	
		
		loginButton.addActionListener(e -> {			
			try {				
				Cpf cpf = new Cpf(cpfTextFieldLogin.getText());
				Password password = new Password(passwordTextFieldLogin.getPassword());
				
				guiService.userLogin(cpf.getString(), password.getString());
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage(), "Alerta", JOptionPane.WARNING_MESSAGE);	
			}
		});	
				
		registerButton.addActionListener(e -> {
			loginPane.setVisible(false);
			registerPane.setVisible(true);
		});	
		
		returnButton.addActionListener(e -> {
			loginPane.setVisible(true);
			registerPane.setVisible(false);
		});	
		
		menuItemQuit.addActionListener(e -> {
			System.exit(1);
		});
	}
	
	private void buildLoginPane() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 423, 388);
		this.setTitle("NewPix - Login ou cadastro");
		this.setIconImage(loadWindowIcon());
		this.setResizable(false);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		loginPane = new JPanel();
		loginPane.setBounds(0, 22, 407, 327);
		loginPane.setLayout(null);
		loginPane.setVisible(true);
		
		menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 407, 22);
		optionsMenu = new JMenu("Opções");
		menuItemQuit = new JMenuItem("Sair");
			
		titleLabel = new JLabel();
		titleLabel.setText("LOGIN");
		titleLabel.setFont(new Font("Dialog", Font.BOLD, 25));
		titleLabel.setBounds(160, 30, 78, 33);
		
		cpfTextFieldLogin = new JFormattedTextField();
		((AbstractDocument) cpfTextFieldLogin.getDocument()).setDocumentFilter(new CpfFilter());
		cpfTextFieldLogin.setTransferHandler(null);
				
		cpfTextFieldLogin.setFont(textFieldFont);
		cpfTextFieldLogin.setBorder(BorderFactory.createEtchedBorder());
		cpfTextFieldLogin.setMargin(new Insets(0, 6, 0, 6));
		cpfTextFieldLogin.setLocation(100, 110);
		cpfTextFieldLogin.setSize(200, 32);
		
		cpfLabel = new JLabel();
		cpfLabel.setText("CPF");
		cpfLabel.setFont(labelFont);
		cpfLabel.setSize(30, 20);
		cpfLabel.setLocation(101, 88);
		
		passwordTextFieldLogin = new JPasswordField();	
		passwordTextFieldLogin.setFont(passwordFieldFont);
		passwordTextFieldLogin.setBorder(BorderFactory.createEtchedBorder());
		passwordTextFieldLogin.setMargin(new Insets(0, 6, 0, 6));
		passwordTextFieldLogin.setLocation(100, 180);
		passwordTextFieldLogin.setSize(200, 32);
		
		passwordLabel = new JLabel();
		passwordLabel.setText("Senha");
		passwordLabel.setFont(labelFont);
		passwordLabel.setSize(42, 20);
		passwordLabel.setLocation(101, 158);
		
		loginButton = new JButton();
		loginButton.setText("Login");
		loginButton.setFont(buttonFont);
		loginButton.setFocusPainted(false);
		loginButton.setBorder(BorderFactory.createRaisedBevelBorder());
		loginButton.setSize(105, 32);
		loginButton.setLocation(80, 255);
		
		registerButton = new JButton();
		registerButton.setText("Cadastrar-se");
		registerButton.setFont(buttonFont);
		registerButton.setFocusPainted(false);
		registerButton.setBorder(BorderFactory.createRaisedBevelBorder());
		registerButton.setSize(105, 32);
		registerButton.setLocation(211, 255);

		menuBar.add(optionsMenu);
		optionsMenu.add(menuItemQuit);
		
		contentPane.add(menuBar);
		
		loginPane.add(titleLabel);
		loginPane.add(cpfTextFieldLogin);
		loginPane.add(cpfLabel);		
		loginPane.add(passwordTextFieldLogin);
		loginPane.add(passwordLabel);
		loginPane.add(loginButton);
		loginPane.add(registerButton);
		
		contentPane.add(loginPane);
	}
	
	private void buildRegisterPane() {
		registerPane = new JPanel();
		registerPane.setLayout(null);
		registerPane.setVisible(false);
		registerPane.setBounds(0, 22, 407, 327);
		
		returnButton = new JButton("←");
		returnButton.setFont(new Font("Dialog", Font.BOLD, 30));
		returnButton.setBounds(-1, -1, 70, 35);
		
		titleLabel = new JLabel();
		titleLabel.setText("CADASTRO");
		titleLabel.setFont(new Font("Dialog", Font.BOLD, 25));
		titleLabel.setBounds(125, 10, 142, 33);
	
		cpfTextFieldRegister = new JFormattedTextField();	
		((AbstractDocument) cpfTextFieldRegister.getDocument()).setDocumentFilter(new CpfFilter());
		cpfTextFieldRegister.setTransferHandler(null);
	
		cpfTextFieldRegister.setFont(textFieldFont);
		cpfTextFieldRegister.setBorder(BorderFactory.createEtchedBorder());
		cpfTextFieldRegister.setMargin(new Insets(0, 6, 0, 6));
		cpfTextFieldRegister.setLocation(100, 82);
		cpfTextFieldRegister.setSize(200, 32);
		
		cpfLabel = new JLabel();
		cpfLabel.setText("CPF");
		cpfLabel.setFont(labelFont);
		cpfLabel.setSize(30, 20);
		cpfLabel.setLocation(101, 60);
		
		nameTextField = new JTextField();	
		nameTextField.setFont(passwordFieldFont);
		nameTextField.setBorder(BorderFactory.createEtchedBorder());
		nameTextField.setMargin(new Insets(0, 6, 0, 6));
		nameTextField.setLocation(100, 148);
		nameTextField.setSize(200, 32);
		
		nameLabel = new JLabel();
		nameLabel.setText("Nome");
		nameLabel.setFont(labelFont);
		nameLabel.setSize(42, 20);
		nameLabel.setLocation(101, 126);
			
		passwordTextFieldRegister = new JPasswordField();	
		passwordTextFieldRegister.setFont(passwordFieldFont);
		passwordTextFieldRegister.setBorder(BorderFactory.createEtchedBorder());
		passwordTextFieldRegister.setMargin(new Insets(0, 6, 0, 6));
		passwordTextFieldRegister.setLocation(100, 217);
		passwordTextFieldRegister.setSize(200, 32);
		
		passwordLabel = new JLabel();
		passwordLabel.setText("Senha");
		passwordLabel.setFont(labelFont);
		passwordLabel.setSize(42, 20);
		passwordLabel.setLocation(101, 195);
		
		registerButtonSend = new JButton();
		registerButtonSend.setText("Cadastrar-se");
		registerButtonSend.setFont(buttonFont);
		registerButtonSend.setFocusPainted(false);
		registerButtonSend.setBorder(BorderFactory.createRaisedBevelBorder());
		registerButtonSend.setSize(115, 35);
		registerButtonSend.setLocation(140, 269);
		
		registerPane.add(returnButton);
		registerPane.add(titleLabel);
		registerPane.add(cpfTextFieldRegister);
		registerPane.add(cpfLabel);		
		registerPane.add(nameTextField);
		registerPane.add(nameLabel);		
		registerPane.add(passwordTextFieldRegister);
		registerPane.add(passwordLabel);
		registerPane.add(registerButtonSend);
			
		contentPane.add(registerPane);
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
