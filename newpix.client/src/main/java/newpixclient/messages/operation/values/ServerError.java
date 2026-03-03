package newpixclient.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixclient.json.JsonMessage;
import newpixclient.messages.operation.Operation;
import newpixclient.messages.operation.OperationCode;
import newpixclient.service.ClientService;
import newpixclient.type.Token;

import java.util.Map;

public class ServerError implements Operation {
    private final ClientService clientService;

    public ServerError(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public Object send(Map<String, Object> itemMap, Token token) throws Exception {
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setOperacao(OperationCode.SERVER_ERROR);
        jsonMessage.setOperacao_enviada((String) itemMap.get("operacao_enviada"));
        jsonMessage.setInfo((String) itemMap.get("info"));

        return jsonMessage;
    }

    @Override
    public Object receive(JsonNode payload) throws Exception {
        return null;
    }
}
