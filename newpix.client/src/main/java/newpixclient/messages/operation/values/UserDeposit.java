package newpixclient.messages.operation.values;
import newpixclient.json.JsonMessage;
import newpixclient.messages.operation.Operation;
import com.fasterxml.jackson.databind.JsonNode;
import newpixclient.messages.operation.OperationCode;
import newpixclient.messages.operation.ReadAfter;
import newpixclient.service.ClientService;
import newpixclient.type.Token;

import java.util.Map;

public class UserDeposit implements Operation, ReadAfter {
    private final ClientService clientService;

    public UserDeposit(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public Object send(Map<String, Object> itemMap, Token token) throws Exception {
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setOperacao(OperationCode.USER_DEPOSIT);
        jsonMessage.setToken(token.getToken());
        jsonMessage.setValor_enviado((Double) itemMap.get("valor_enviado"));

        return jsonMessage;
    }

    @Override
    public Object receive(JsonNode payload) throws Exception {
        boolean status = payload.path("status").asBoolean();
        String info = payload.path("info").asText();

        clientService.deposit(status, info);

        return null;
    }
}
