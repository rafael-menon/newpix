package newpixclient.service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import newpixclient.gui.ConnectGui;
import newpixclient.gui.CredentialsGui;
import newpixclient.gui.ExtractGui;
import newpixclient.gui.UserGui;
import newpixclient.messages.MessageHandler;
import newpixclient.messages.operation.OperationCode;
import newpixclient.service.listener.GuiEvent;
import newpixclient.json.TransactionViewModel;
import newpixclient.json.UserJson;
import newpixclient.type.Name;
import newpixclient.type.Password;

public class GuiService implements GuiEvent {
	private final TcpService tcpService;
	private final ConnectGui connectGui;
	private CredentialsGui credentialsGui;
	private ExtractGui extractGui;
	private UserGui userGui;

    private final MessageHandler messageHandler;

	public GuiService(TcpService tcpService, ConnectGui connectGui, MessageHandler messageHandler) {
        this.tcpService = tcpService;
		this.connectGui = connectGui;
        this.messageHandler = messageHandler;
	}

	public void startListener() {
		tcpService.setGuiListener(this);
	}

	public void connect(String ip, String port) throws Exception {
		tcpService.connect(ip, port);

        messageHandler.send(OperationCode.CONNECT, null);
	}

	public void userCreate(String cpf, String name, String password) throws Exception {
        HashMap<String, Object> itemMap = new HashMap<>();
        itemMap.put("nome", name);
        itemMap.put("cpf", cpf);
        itemMap.put("senha", password);

        messageHandler.send(OperationCode.USER_CREATE, itemMap);
	}
	
	public void userLogout() throws Exception {
		Object[] options = {"Yes", "No"};
		
		int logoutOption = JOptionPane.showOptionDialog(
                null,                                  
                "Deseja fazer logout?",           
                "Logout",                       
                JOptionPane.YES_NO_OPTION,            
                JOptionPane.QUESTION_MESSAGE,          
                null,                                
                options,                               
                options[0]                             
        );
		
		if(logoutOption == JOptionPane.YES_OPTION) {
            messageHandler.send(OperationCode.USER_LOGOUT, null);
		}
		
		if(logoutOption == JOptionPane.NO_OPTION) {
			return;
		}
	} 

	public void userLogin(String cpf, String password) throws Exception {
        HashMap<String, Object> itemMap = new HashMap<>();
        itemMap.put("cpf", cpf);
        itemMap.put("senha", password);

        messageHandler.send(OperationCode.USER_LOGIN, itemMap);
	}

	public void userUpdate(Name name, Password password) throws Exception {
        if(name.getString() == null && password.getString() == null) {
            throw new Exception("Preencha os campos corretamente.");
        }

        HashMap<String, Object> itemMap = new HashMap<>();
        itemMap.put("nome", name.getString());
        itemMap.put("senha", password.getString());

        messageHandler.send(OperationCode.USER_UPDATE, itemMap);
	}
	
	public void userDelete() throws Exception {
		String[] options = {"Yes", "No"};
		
		int deleteOption = JOptionPane.showOptionDialog(
                null,                                 
                "Deseja deletar a sua conta?",            
                "Deletar",                        
                JOptionPane.YES_NO_OPTION,             
                JOptionPane.QUESTION_MESSAGE,         
                null,                                 
                options,                              
                options[0]                             
        );
		
		if(deleteOption == JOptionPane.YES_OPTION) {
            messageHandler.send(OperationCode.USER_DELETE, null);
		}
		
		if(deleteOption == JOptionPane.NO_OPTION) {
			return;
		}
	}

    public void createTransaction(String value, String cpfReceiver) throws Exception {
        Double valueDouble = Double.parseDouble(value);

        HashMap<String, Object> itemMap = new HashMap<>();
        itemMap.put("valor", valueDouble);
        itemMap.put("cpf_destino", cpfReceiver);

        messageHandler.send("transacao_criar", itemMap);
    }

    public void deposit(String value) throws Exception {
        double valueDouble = Double.parseDouble(value);

        HashMap<String, Object> itemMap = new HashMap<>();
        itemMap.put("valor_enviado", valueDouble);

        messageHandler.send("depositar", itemMap);
    }
    
    public void searchExtract(String startDateStr, String endDateStr) throws Exception {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate startDate = LocalDate.parse(startDateStr, inputFormatter);
        LocalDate endDate = LocalDate.parse(endDateStr, inputFormatter);

        String formattedStartDate = startDate.atStartOfDay()
                .atOffset(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_INSTANT);

        String formattedEndDate = endDate.atTime(23, 59, 59)
                .atOffset(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_INSTANT);

        HashMap<String, Object> itemMap = new HashMap<>();
        itemMap.put("data_inicial", formattedStartDate);
        itemMap.put("data_final", formattedEndDate);

        messageHandler.send("transacao_ler", itemMap);
    }
    
    public void createExtractGui() {
    	SwingUtilities.invokeLater(() -> {
			extractGui = new ExtractGui(this);
			extractGui.setVisible(true);
		});	
    }

	@Override
	public void socketConnectionSuccessful() {
		SwingUtilities.invokeLater(() -> {           
			connectGui.updateConnectionButton(true);
		});
	}

	@Override
	public void socketConnectionFailed(String errorMessage) {
		SwingUtilities.invokeLater(() -> {           
			JOptionPane.showMessageDialog(connectGui, errorMessage, "Erro", JOptionPane.ERROR_MESSAGE);
			connectGui.updateConnectionButton(false);
		});
	}

    public void connectionOperationResult(boolean status, String message) {
        if(!status) {
            JOptionPane.showMessageDialog(connectGui, message, "Erro", JOptionPane.ERROR_MESSAGE);
            connectGui.updateConnectionButton(false);
        } else {
            SwingUtilities.invokeLater(() -> {
                connectGui.dispose();
                credentialsGui = new CredentialsGui(this);
                credentialsGui.setVisible(true);
            });
        }
    }

    public void createUserOperationResult
            (boolean status, String message) {
        if(!status) {
            JOptionPane.showMessageDialog(credentialsGui, message, "Falha", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(credentialsGui, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void userLogoutOperationResult(boolean status, String message) {
        if(!status) {
            JOptionPane.showMessageDialog(credentialsGui, message, "Falha", JOptionPane.ERROR_MESSAGE);
        } else {
            SwingUtilities.invokeLater(() -> {
                userGui.dispose();
                credentialsGui = new CredentialsGui(this);
                credentialsGui.setVisible(true);
            });
        }
    }

    public void userLoginOperationError(String errorMessage) {
        JOptionPane.showMessageDialog(connectGui, errorMessage, "Falha", JOptionPane.ERROR_MESSAGE);
    }

    public void userReadOperationResult(boolean status, String message, UserJson userJson) {
        if(!status) {
            JOptionPane.showMessageDialog(credentialsGui, message, "Falha", JOptionPane.ERROR_MESSAGE);
        } else {
            if(credentialsGui.isActive()) {
                SwingUtilities.invokeLater(() -> {
                    credentialsGui.dispose();
                    userGui = new UserGui(this);
                    userGui.setVisible(true);

                    userGui.updateUserData(userJson.getNome(), userJson.getCpf(), userJson.getSaldo());
                });
            } else {
                userGui.updateUserData(userJson.getNome(), userJson.getCpf(), userJson.getSaldo());
            }
        }
    }

    public void userUpdateOperationResult(boolean status, String message) {
        if(!status) {
            JOptionPane.showMessageDialog(credentialsGui, message, "Falha", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(credentialsGui, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public void userDeleteOperationResult(boolean status, String message) {
        if(!status) {
            JOptionPane.showMessageDialog(credentialsGui, message, "Falha", JOptionPane.ERROR_MESSAGE);
        } else {
            SwingUtilities.invokeLater(() -> {
                userGui.dispose();
                credentialsGui = new CredentialsGui(this);
                credentialsGui.setVisible(true);
            });
        }
    }

    public void depositOperationResult(boolean status, String message) {
        if(!status) {
            JOptionPane.showMessageDialog(userGui, message, "Falha", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(userGui, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void transactionsCreateResult(boolean status, String message) {
        if(!status) {
            JOptionPane.showMessageDialog(userGui, message, "Falha", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(userGui, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void transactionsReadResult(boolean status, String message, List<TransactionViewModel> transactions) {
        SwingUtilities.invokeLater(() -> {
            if (!status) {
                JOptionPane.showMessageDialog(extractGui, "Erro: " + message, "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            extractGui.createTable(transactions);
        });
    }
}
