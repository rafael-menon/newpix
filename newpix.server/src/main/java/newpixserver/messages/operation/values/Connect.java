package newpixserver.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixserver.messages.operation.Operation;
import newpixserver.tcp.ClientThread;

import java.net.SocketException;

public class Connect implements Operation {
    private final ClientThread clientThread;

    public Connect(ClientThread clientThread) {
        this.clientThread = clientThread;
    }

    @Override
    public Object execute(JsonNode payload) throws Exception {
        if(!clientThread.isAlive()) {
            throw new SocketException("Erro ao se conectar");
        }

        return null;
    }
}
