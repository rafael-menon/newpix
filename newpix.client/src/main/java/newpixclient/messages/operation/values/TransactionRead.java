package newpixclient.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixclient.json.JsonMessage;
import newpixclient.json.TransactionViewModel;
import newpixclient.messages.operation.Operation;
import newpixclient.messages.operation.OperationCode;
import newpixclient.service.ClientService;
import newpixclient.type.Token;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransactionRead implements Operation {
    private final ClientService clientService;

    public TransactionRead(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public Object send(Map<String, Object> itemMap, Token token) throws Exception {
        JsonMessage jsonMessage = new JsonMessage();
        jsonMessage.setOperacao(OperationCode.TRANSACTION_READ);
        jsonMessage.setToken(token.getToken());
        jsonMessage.setData_inicial((String) itemMap.get("data_inicial"));
        jsonMessage.setData_final((String) itemMap.get("data_final"));

        return jsonMessage;
    }

    @Override
    public Object receive(JsonNode payload) throws Exception {
        List<TransactionViewModel> transactionList = new ArrayList<>();
        boolean status = payload.get("status").asBoolean();
        String info = payload.get("info").asText();

        if (status && payload.has("transacoes")) {
            JsonNode transactionsArray = payload.get("transacoes");

            if (transactionsArray.isArray()) {
                for (JsonNode node : transactionsArray) {
                    double sentValue = node.get("valor_enviado").asDouble();
                    String originCpf = node.get("usuario_enviador").asText();
                    String receiverCpf = node.get("usuario_recebedor").asText();
                    String rawDate = node.get("criado_em").asText();

                    String formattedDate = rawDate;
                    try {
                        LocalDateTime date = LocalDateTime.parse(rawDate, DateTimeFormatter.ISO_DATE_TIME);
                        formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                    } catch (Exception ignored) {
                    }

                    String type;
                    if (originCpf.equals(receiverCpf)) {
                        type = "Depósito";
                    } else {
                        type = "Transação";
                    }

                    transactionList.add(new TransactionViewModel(formattedDate, type, sentValue));
                }
            }
        }

        clientService.transactionRead(status, info, transactionList);

        return null;
    }
}
