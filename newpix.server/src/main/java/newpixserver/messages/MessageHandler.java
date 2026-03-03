package newpixserver.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import newpixserver.gui.Log;
import newpixserver.messages.json.JsonMessage;
import newpixserver.messages.json.JsonValidator;
import newpixserver.messages.json.TransactionJson;
import newpixserver.messages.json.UserJson;
import newpixserver.messages.operation.*;
import newpixserver.messages.client_state.ClientState;
import newpixserver.messages.client_state.type.NotConnected;
import newpixserver.messages.client_state.StateResult;
import newpixserver.service.ServiceManager;
import newpixserver.service.UserService;
import newpixserver.tcp.ClientThread;

import java.util.List;

public class MessageHandler {
    private final ServiceManager serviceManager;
    private final ClientThread clientThread;
    private static final ObjectMapper mapper = new ObjectMapper();

    private ClientState currentState;

    public MessageHandler(ServiceManager serviceManager, ClientThread clientThread) {
        this.serviceManager = serviceManager;
        this.clientThread = clientThread;
        this.currentState = new NotConnected();
    }

    public String handleRequest(String requestJson) {
        String operationName = "";
        try {
            JsonNode rootNode = mapper.readTree(requestJson);

            if (rootNode.has("operacao")) {
                operationName = rootNode.path("operacao").asText();
            }

            JsonValidator.validateClient(requestJson);

            StateResult stateResult = currentState.handle(this, rootNode, operationName);

            if (stateResult.nextState() != null) {
                this.currentState = stateResult.nextState();
            }

            if (OperationCode.SERVER_ERROR.equals(operationName)) {
                return OperationCode.SERVER_ERROR;
            }

            JsonMessage.Builder responseBuilder = new JsonMessage.Builder(operationName, true)
                    .info(getSuccessMessage(operationName));

            Object result = stateResult.result();

            if (result instanceof String token) {
                responseBuilder.withToken(token);
            } else if (result instanceof UserJson userJson) {
                responseBuilder.withUser(userJson);
            } else if (result instanceof List<?> transactionList) {
                responseBuilder.withTransactions((List<TransactionJson>) transactionList);
            }

            JsonMessage responseMessage = responseBuilder.build();
            return mapper.writeValueAsString(responseMessage);
        } catch (Exception e) {
            try {
                JsonMessage.Builder responseBuilder = new JsonMessage.Builder(operationName, false).info(e.getMessage());
                JsonMessage errorResponse = responseBuilder.build();
                return mapper.writeValueAsString(errorResponse);
            } catch (Exception e1) {
                Log.error("Erro catastrófico: " + e1.getMessage());
                return null;
            }
        }
    }

    private String getSuccessMessage(String operationName) {
        return switch (operationName) {
            case OperationCode.CONNECT -> OperationCode.CONNECT_SUCCESS;
            case OperationCode.USER_LOGIN -> OperationCode.USER_LOGIN_SUCCESS;
            case OperationCode.USER_LOGOUT -> OperationCode.USER_LOGOUT_SUCCESS;
            case OperationCode.USER_CREATE -> OperationCode.USER_CREATE_SUCCESS;
            case OperationCode.USER_READ -> OperationCode.USER_READ_SUCCESS;
            case OperationCode.USER_UPDATE -> OperationCode.USER_UPDATE_SUCCESS;
            case OperationCode.USER_DELETE -> OperationCode.USER_DELETE_SUCCESS;
            case OperationCode.TRANSACTION_CREATE -> OperationCode.TRANSACTION_CREATE_SUCCESS;
            case OperationCode.TRANSACTION_READ -> OperationCode.TRANSACTION_READ_SUCCESS;
            case OperationCode.USER_DEPOSIT -> OperationCode.USER_DEPOSIT_SUCCESS;
            default -> "Operação realizada com sucesso.";
        };
    }

    public ServiceManager getServiceManager() { return serviceManager; }

    public ClientThread getClientThread() {
        return clientThread;
    }
}

