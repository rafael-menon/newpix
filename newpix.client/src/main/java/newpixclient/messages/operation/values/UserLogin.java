package newpixclient.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixclient.json.JsonMessage;
import newpixclient.messages.operation.Operation;
import newpixclient.messages.operation.OperationCode;
import newpixclient.messages.operation.ReadAfter;
import newpixclient.service.ClientService;
import newpixclient.type.Token;

import java.util.Map;

public class UserLogin implements Operation, ReadAfter {
    private final ClientService clientService;

    public UserLogin(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public Object send(Map<String, Object> itemMap, Token token) throws Exception {
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setOperacao(OperationCode.USER_LOGIN);
        jsonMessage.setCpf((String) itemMap.get("cpf"));
        jsonMessage.setSenha((String) itemMap.get("senha"));

        return jsonMessage;
    }

    @Override
    public Object receive(JsonNode payload) throws Exception {
        String tokenStr = payload.path("token").asText();
        boolean status = payload.path("status").asBoolean();
        String info = payload.path("info").asText();

        clientService.userLogin(status, info);

        if(status) {
            return new Token(tokenStr);
        }

        return null;
    }
}
