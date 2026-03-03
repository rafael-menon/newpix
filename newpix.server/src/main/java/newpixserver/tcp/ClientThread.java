package newpixserver.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import newpixserver.database.entities.User;
import newpixserver.messages.MessageHandler;
import newpixserver.messages.json.JsonValidator;
import newpixserver.messages.operation.OperationCode;
import newpixserver.service.ClientService;
import newpixserver.service.ServiceManager;
import newpixserver.service.UserService;
import newpixserver.gui.Log;

public class ClientThread extends Thread {
    private final Socket clientSocket;
    private final ClientService clientService;
    private final MessageHandler messageHandler;
    private User authenticatedUser = null;

    public ClientThread(Socket socket, ClientService clientService, ServiceManager serviceManager) {
        this.clientSocket = socket;
        this.clientService = clientService;
        this.messageHandler = new MessageHandler(serviceManager, this);
    }

    public void stopClient() {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            Log.print("Thread do cliente: " + e.getMessage());
        }
    }

    public void run() {
        try (
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            while (!clientSocket.isClosed()) {
                String clientRequest = input.readLine();
                Log.print(clientSocket.getInetAddress() + ":" + clientSocket.getPort() + ": " + clientRequest);

                if(clientRequest == null) {
                    Log.print("Cliente " + clientSocket.getInetAddress() + " encerrou a conexão.");
                    break;
                }

                String jsonResponse = messageHandler.handleRequest(clientRequest);

                if (jsonResponse != null && (!jsonResponse.equals(OperationCode.SERVER_ERROR))) {
                    Log.print("Servidor: " + jsonResponse);
                    output.println(jsonResponse);
                }
            }
        } catch (SocketException e) {
            Log.print("Conexão com " + clientSocket.getInetAddress() + " foi perdida ou fechada.");
        } catch (IOException e) {
            Log.print("Cliente " + clientSocket.getInetAddress() + " desconectado.");
        } finally {
            clientService.removeClient(clientSocket);
        }
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(User user) {
        this.authenticatedUser = user;
    }

    public void clearAuthentication() {
        this.authenticatedUser = null;
    }

    public void updateClientName(String name) {
        clientService.updateClientName(clientSocket.getPort(), clientSocket.getInetAddress().getHostAddress(), name);
    }
}
