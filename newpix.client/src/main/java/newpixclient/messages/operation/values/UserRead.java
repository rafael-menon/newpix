package newpixclient.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixclient.json.JsonMessage;
import newpixclient.json.UserJson;
import newpixclient.messages.operation.Operation;
import newpixclient.messages.operation.OperationCode;
import newpixclient.service.ClientService;
import newpixclient.type.Cpf;
import newpixclient.type.Token;

import java.util.Map;

public class UserRead implements Operation {
    private final ClientService clientService;

    public UserRead(ClientService clientService) {
        this.clientService = clientService;
    }


    @Override
    public Object send(Map<String, Object> itemMap, Token token) throws Exception {
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setOperacao(OperationCode.USER_READ);
        jsonMessage.setToken(token.getToken());

        return jsonMessage;
    }

    @Override
    public Object receive(JsonNode payload) throws Exception {
        boolean status = payload.path("status").asBoolean();
        String info = payload.path("info").asText();
        JsonNode userNode = payload.get("usuario");

        Cpf cpf = new Cpf(userNode.path("cpf").asText());
        double saldo = userNode.path("saldo").asDouble();
        String nome = userNode.path("nome").asText();

        UserJson userJson = new UserJson();
        userJson.setCpf(cpf.getString());
        userJson.setSaldo(saldo);
        userJson.setNome(nome);

        clientService.userRead(status, info, userJson);

        return status;
    }
}
