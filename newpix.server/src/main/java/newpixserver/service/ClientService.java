package newpixserver.service;

import newpixserver.service.listener.*;
import newpixserver.tcp.ClientThread;
import newpixserver.tcp.ConnectedClient;

import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ClientService {
    //private final UserService userService;
    private final ServiceManager serviceManager;
    private ClientServiceListener listener;

    private final Map<String, ConnectedClient> clientMap = new ConcurrentHashMap<>();
    private final List<ClientThread> clientThreadList = new CopyOnWriteArrayList<>();
    private final ExecutorService hostnameResolver = Executors.newCachedThreadPool();

    public ClientService(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public void addListener(ClientServiceListener listener) {
        this.listener = listener;
    }

    private void clientServiceEvent(ClientServiceEvent event, Map<String, ConnectedClient> clientMap) {
        listener.onClientServiceEvent(event, clientMap);
    }

    public void handleNewClient(Socket clientSocket) {
        ClientThread clientThread = new ClientThread(clientSocket, this, serviceManager);
        clientThread.start();
        clientThreadList.add(clientThread);

        addNewClient(clientSocket);
    }

    private void addNewClient(Socket clientSocket) {
        String clientIp = clientSocket.getInetAddress().getHostAddress();
        int clientPort = clientSocket.getPort();

        ConnectedClient newClient = new ConnectedClient(clientIp, clientPort, "Carregando...", "Esperando login...");

        String clientKey = clientIp + ":" + clientPort;

        clientMap.put(clientKey, newClient);

        clientServiceEvent(ClientServiceEvent.CONNECTED_LIST_CHANGED, clientMap);
        resolveClientHostname(clientSocket);
    }

    private void resolveClientHostname(Socket clientSocket) {
        hostnameResolver.submit(() -> {
            String clientIp = clientSocket.getInetAddress().getHostAddress();
            int clientPort = clientSocket.getPort();
            String hostname = clientSocket.getInetAddress().getHostName();
            String clientKey = clientIp + ":" + clientPort;

            ConnectedClient client = clientMap.get(clientKey);

            if (client != null) {
                client.setHostname(hostname);
                clientServiceEvent(ClientServiceEvent.CONNECTED_LIST_CHANGED, clientMap);
            }
        });
    }

    public void updateClientName(int port, String ip, String name) {
        String clientKey = ip + ":" + port;
        ConnectedClient client = clientMap.get(clientKey);

        if (client != null) {
            client.setName(name);
            clientServiceEvent(ClientServiceEvent.CONNECTED_LIST_CHANGED, clientMap);
        }
    }

    public void removeClient(Socket clientSocket) {
        String clientIp = clientSocket.getInetAddress().getHostAddress();
        int clientPort = clientSocket.getPort();
        String clientKey = clientIp + ":" + clientPort;
        clientMap.remove(clientKey);

        clientServiceEvent(ClientServiceEvent.CONNECTED_LIST_CHANGED, clientMap);
    }

    public void removeAllClients() {
        clientMap.clear();
        for (ClientThread client : clientThreadList) {
            client.stopClient();
        }
        clientThreadList.clear();

        clientServiceEvent(ClientServiceEvent.CONNECTED_LIST_CHANGED, null);
    }

    public void shutdown() {
        removeAllClients();
        hostnameResolver.shutdown();
        try {
            if (!hostnameResolver.awaitTermination(5, TimeUnit.SECONDS)) {
                hostnameResolver.shutdownNow();
            }
        } catch (InterruptedException e) {
            hostnameResolver.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
