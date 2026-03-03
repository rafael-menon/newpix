package newpixserver.messages.client_state.type;

import com.fasterxml.jackson.databind.JsonNode;
import newpixserver.messages.MessageHandler;
import newpixserver.messages.client_state.StateResult;
import newpixserver.messages.operation.ConnectedOperation;
import newpixserver.messages.operation.Operation;
import newpixserver.messages.operation.OperationCode;

public class Connected extends AbstractClientState {
    @Override
    protected StateResult handleStateSpecificOperation(MessageHandler context, JsonNode payload, String operationName, Operation operation) throws Exception {
        if (operation instanceof ConnectedOperation connectedOperation) {
            Object result = connectedOperation.execute(payload, true);

            if (operationName.equals(OperationCode.USER_LOGIN)) {
                return new StateResult(result, new Authenticated());
            }

            if (operationName.equals(OperationCode.USER_CREATE)) {
                return StateResult.unchanged(result);
            }
        }

        throw new SecurityException("Operação '" + operationName + "' negada. Autenticação requerida.");
    }

    @Override
    protected StateResult connectOp(Object result) {
        return StateResult.unchanged(result);
    }

    @Override
    protected StateResult serverErrorOp(Object result) {
        return StateResult.unchanged(result);
    }

    @Override
    public ClientStateType getType() {
        return ClientStateType.CONNECTED;
    }
}
