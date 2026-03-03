package newpixserver.messages.client_state;

import com.fasterxml.jackson.databind.JsonNode;
import newpixserver.messages.MessageHandler;
import newpixserver.messages.client_state.type.ClientStateType;

public interface ClientState {
    StateResult handle(MessageHandler context, JsonNode payload, String operationName) throws Exception;

    ClientStateType getType();
}
