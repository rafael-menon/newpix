package newpixserver.messages.client_state.type;

import com.fasterxml.jackson.databind.JsonNode;
import newpixserver.messages.MessageHandler;
import newpixserver.messages.client_state.ClientState;
import newpixserver.messages.client_state.StateResult;
import newpixserver.messages.operation.Operation;
import newpixserver.messages.operation.OperationCode;
import newpixserver.messages.operation.OperationRegistry;
import newpixserver.messages.operation.values.Connect;
import newpixserver.messages.operation.values.ServerError;

public abstract class AbstractClientState implements ClientState {

    @Override
    public final StateResult handle(MessageHandler context, JsonNode payload, String operationName) throws Exception {
        Operation operation = OperationRegistry.getOperation(operationName, context.getServiceManager(), context.getClientThread());

        if(operation instanceof Connect connectOperation) {
            Object result = connectOperation.execute(payload);

            return connectOp(result);
        }

        if(operation instanceof ServerError serverErrorOperation) {
            Object result = serverErrorOperation.execute(payload);

            if (result.equals(OperationCode.CONNECT)) {
                return new StateResult(result, new NotConnected());
            }

            if (result.equals(OperationCode.USER_LOGIN)) {
                return new StateResult(result, new Connected());
            }

            if (result.equals(OperationCode.USER_LOGOUT)) {
                return new StateResult(result, new Authenticated());
            }

            return serverErrorOp(null);
        }

        return handleStateSpecificOperation(context, payload, operationName, operation);
    }

    protected abstract StateResult handleStateSpecificOperation(MessageHandler context, JsonNode payload, String operationName, Operation operation) throws Exception;

    protected abstract StateResult connectOp(Object result);

    protected abstract StateResult serverErrorOp(Object result);
}
