package newpixclient.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixclient.json.JsonMessage;
import newpixclient.messages.operation.Operation;
import newpixclient.messages.operation.OperationCode;
import newpixclient.service.ClientService;
import newpixclient.type.Token;

import java.util.Map;

public class UserLogout implements Operation {
    private final ClientService clientService;

    public UserLogout(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public Object send(Map<String, Object> itemMap, Token token) throws Exception {
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setOperacao(OperationCode.USER_LOGOUT);
        jsonMessage.setToken(token.getToken());

        return jsonMessage;
    }

    @Override
    public Object receive(JsonNode payload) throws Exception {
        boolean status = payload.path("status").asBoolean();
        String info = payload.path("info").asText();

        clientService.userLogout(status, info);

        if(status) {
            return new Token(null);
        } else {
            return null;
        }
    }
}
