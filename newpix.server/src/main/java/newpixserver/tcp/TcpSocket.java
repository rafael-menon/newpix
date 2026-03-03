package newpixserver.tcp;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import newpixserver.service.ClientService;
import newpixserver.gui.Log;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class TcpSocket {
	private ServerSocket serverSocket = null;
	private volatile boolean serverSocketStatus = false;	
	private Thread listenerThread;

    private final ClientService clientService;

    public TcpSocket(ClientService clientService) {
        this.clientService = clientService;
    }

    public void startServerSocket(InetSocketAddress serverAddress) throws IOException {
    	if(serverSocketStatus) return;

    	serverSocket = new ServerSocket();
    	serverSocket.bind(serverAddress);
    	serverSocketStatus = true;
    	Log.print("Servidor iniciado. IP: " + serverAddress);
    	startListening();
    }
    
    private void startListening() {
    	listenerThread = new Thread(() -> {
    		try {
    			Log.print("Servidor escutando...");    			
    			while(serverSocketStatus) {    				
    				Socket clientSocket = serverSocket.accept();
    				Log.print("Cliente conectado: " + clientSocket.getInetAddress());
                    clientService.handleNewClient(clientSocket);
    			}	
    		} catch(SocketException e) {
    			Log.print("Loop de escuta encerrado (o socket foi fechado).");
    		} catch(IOException e) {
    			Log.error("Erro ao escutar: " + e.getMessage());
    		} finally {
                clientService.removeAllClients();
            }
    	});
    	
    	listenerThread.start();
    }

    public void stopServerSocket() {
        if (!serverSocketStatus) return;
        serverSocketStatus = false;

        clientService.shutdown();

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            Log.error("Erro ao encerrar o servidor: " + e.getMessage());
        }

        try {
            if (listenerThread != null && listenerThread.isAlive()) {
                listenerThread.join(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
