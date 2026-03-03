package newpixclient.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixclient.json.JsonMessage;
import newpixclient.messages.operation.Operation;
import newpixclient.messages.operation.OperationCode;
import newpixclient.service.ClientService;
import newpixclient.type.Token;

import java.util.Map;

public class UserCreate implements Operation {
    private final ClientService clientService;

    public UserCreate(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public Object send(Map<String, Object> itemMap, Token token) throws Exception {
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setOperacao(OperationCode.USER_CREATE);
        jsonMessage.setNome((String) itemMap.get("nome"));
        jsonMessage.setCpf((String) itemMap.get("cpf"));
        jsonMessage.setSenha((String) itemMap.get("senha"));

        return jsonMessage;
    }

    @Override
    public Object receive(JsonNode payload) throws Exception {
        boolean status = payload.path("status").asBoolean();
        String info = payload.path("info").asText();

        clientService.userCreate(status, info);

        return status;
    }
}
