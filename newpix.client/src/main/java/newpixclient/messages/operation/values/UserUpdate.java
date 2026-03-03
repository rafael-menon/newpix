package newpixclient.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixclient.json.JsonMessage;
import newpixclient.json.UserJson;
import newpixclient.messages.operation.Operation;
import newpixclient.messages.operation.OperationCode;
import newpixclient.messages.operation.ReadAfter;
import newpixclient.service.ClientService;
import newpixclient.type.Token;

import java.util.Map;

public class UserUpdate implements Operation, ReadAfter {
    private final ClientService clientService;

    public UserUpdate(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public Object send(Map<String, Object> itemMap, Token token) throws Exception {
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setOperacao(OperationCode.USER_UPDATE);
        jsonMessage.setToken(token.getToken());

        UserJson userJson = new UserJson();
        userJson.setNome((String) itemMap.get("nome"));
        userJson.setSenha((String) itemMap.get("senha"));

        jsonMessage.setUsuario(userJson);

        return jsonMessage;
    }

    @Override
    public Object receive(JsonNode payload) throws Exception {
        boolean status = payload.path("status").asBoolean();
        String info = payload.path("info").asText();

        clientService.userUpdate(status, info);

        return status;
    }
}
