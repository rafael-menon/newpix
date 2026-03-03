package newpixserver.service.listener;

import newpixserver.tcp.ConnectedClient;

import java.util.Map;

public interface ClientServiceListener {
    void onClientServiceEvent(ClientServiceEvent event, Map<String, ConnectedClient> clientMap);
}
