package newpixclient.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixclient.json.JsonMessage;
import newpixclient.messages.operation.Operation;
import newpixclient.messages.operation.OperationCode;
import newpixclient.messages.operation.ReadAfter;
import newpixclient.service.ClientService;
import newpixclient.type.Token;

import java.util.Map;

public class TransactionCreate implements Operation, ReadAfter {
    private final ClientService clientService;

    public TransactionCreate(ClientService clientService) {
        this.clientService = clientService;
    }


    @Override
    public Object send(Map<String, Object> itemMap, Token token) throws Exception {
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setOperacao(OperationCode.TRANSACTION_CREATE);
        jsonMessage.setToken(token.getToken());
        jsonMessage.setValor((Double) itemMap.get("valor"));
        jsonMessage.setCpf_destino((String) itemMap.get("cpf_destino"));

        return jsonMessage;
    }

    @Override
    public Object receive(JsonNode payload) throws Exception {
        boolean status = payload.path("status").asBoolean();
        String info = payload.path("info").asText();

        clientService.transactionCreate(status, info);

        return status;
    }
}
