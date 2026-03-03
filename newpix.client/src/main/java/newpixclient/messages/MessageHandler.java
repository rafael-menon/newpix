package newpixclient.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import newpixclient.json.JsonValidator;
import newpixclient.messages.operation.Operation;
import newpixclient.messages.operation.OperationCode;
import newpixclient.messages.operation.OperationRegistry;
import newpixclient.messages.operation.ReadAfter;
import newpixclient.messages.operation.values.*;
import newpixclient.service.ServiceManager;
import newpixclient.tcp.TcpSocket;
import newpixclient.type.Token;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class MessageHandler {
    private final ServiceManager serviceManager;
    private final TcpSocket tcpSocket;
    private Token token = null;

    private static final ObjectMapper mapper = new ObjectMapper();

    public MessageHandler(ServiceManager serviceManager, TcpSocket tcpSocket) {
        this.serviceManager = serviceManager;
        this.tcpSocket = tcpSocket;
    }

    public void send(String operationName, Map<String, Object> itemMap) throws Exception {
        send(operationName, itemMap, true);
    }

    public void send(String operationName, Map<String, Object> itemMap, boolean expectsResponse) throws Exception {
        Operation operation = OperationRegistry.getOperation(operationName, serviceManager);

        Object result = operation.send(itemMap, token);

        String request = mapper.writeValueAsString(result);

        tcpSocket.addToQueue(request, expectsResponse);
    }
    
    public void receive(String payload) {
        String operationName = "";

        try {
            JsonNode rootNode = mapper.readTree(payload);

            if (rootNode.has("operacao")) {
                operationName = rootNode.path("operacao").asText();
            }

            Operation operation = OperationRegistry.getOperation(operationName, serviceManager);

            JsonValidator.validateServer(payload);

            Object result = operation.receive(rootNode);

            if(result instanceof Token) {
              token = (Token) result;
            }

            if(operation instanceof ReadAfter && token != null) {
                send(OperationCode.USER_READ, null);
            }

        } catch(Exception e) {
            HashMap<String, Object> itemMap = new HashMap<String, Object>();
            itemMap.put("operacao_enviada", operationName);
            itemMap.put("info", e.getMessage());

            try {
                send(OperationCode.SERVER_ERROR, itemMap, false);
            } catch(Exception e1) {
                System.err.println("Erro catastrófico: " + e1.getMessage());
            }
        }
    }
}
