package newpixclient.tcp;

import java.io.*;
import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.fasterxml.jackson.databind.ObjectMapper;

import newpixclient.messages.MessageHandler;
import newpixclient.messages.TcpRequest;
import newpixclient.service.listener.GuiEvent;

public class TcpSocket {
	private Thread clientThread = null;
	private Socket clientSocket = null;
	private String ip;
	private Integer port;
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private GuiEvent guiListener;
    private MessageHandler messageHandler;

	private final BlockingQueue<TcpRequest> requests = new LinkedBlockingDeque<>();

	public void setGuiListener(GuiEvent guiListener) {
        this.guiListener = guiListener;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public void setPort(Integer port) {
		this.port = port;
	}
	
	public void start() throws RuntimeException {
		System.out.println("Tentando se conectar com o host " + ip + " na porta " + port);

		clientThread = new Thread(() -> {
			initiateSocket();
            runSocket();
		});
	
		clientThread.start();
	}

    private void initiateSocket() {
        try {
            clientSocket = new Socket(ip, port);

            if (guiListener != null) {
                guiListener.socketConnectionSuccessful();
            }

        } catch (UnknownHostException e) {
            if (guiListener != null) {
                requests.clear();
                guiListener.socketConnectionFailed("Não foi possivel se conectar com " + ip);
            }
            requests.clear();
            System.err.println("Não foi possivel se conectar com " + ip);
        } catch (IOException e) {
            if (guiListener != null) {
                requests.clear();
                guiListener.socketConnectionFailed("Não foi possivel se conectar com " + ip);
            }
            requests.clear();
            System.err.println("Não foi possivel se conectar com " + ip);
        }
    }

    private void runSocket() {
        try (PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            while (!clientSocket.isClosed()) {
                try {
                    TcpRequest request = requests.take();

                    System.out.println("Cliente enviou: " + request.getPayload());

                    output.println(request.getPayload());

                    if (request.expectsResponse()) {
                        String serverResponse = input.readLine();

                        if (serverResponse == null) {
                            break;
                        }

                        System.out.println("Servidor retornou: " + serverResponse);
                        messageHandler.receive(serverResponse);
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            output.close();
            input.close();
            clientSocket.close();
        } catch(NullPointerException ex) {
            requests.clear();
            System.out.println("Socket finalizado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addToQueue(String message, boolean expectsResponse) throws InterruptedException {
        requests.put(new TcpRequest(message, expectsResponse));
    }
}
	