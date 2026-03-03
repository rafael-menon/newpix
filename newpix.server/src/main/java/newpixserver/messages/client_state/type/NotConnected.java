package newpixserver.messages.client_state.type;

import com.fasterxml.jackson.databind.JsonNode;
import newpixserver.messages.MessageHandler;
import newpixserver.messages.client_state.StateResult;
import newpixserver.messages.operation.Operation;

public class NotConnected extends AbstractClientState {

    @Override
    protected StateResult handleStateSpecificOperation(MessageHandler context, JsonNode payload, String operationName, Operation operation) throws Exception {
        throw new SecurityException("Operação '" + operationName + "' negada. Conecte-se primeiro.");
    }

    @Override
    protected StateResult connectOp(Object result) {
        return new StateResult(result, new Connected());
    }

    @Override
    protected StateResult serverErrorOp(Object result) {
        return StateResult.unchanged(result);
    }

    @Override
    public ClientStateType getType() {
        return ClientStateType.NOT_CONNECTED;
    }
}
