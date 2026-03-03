package newpixserver.messages.operation;

import com.fasterxml.jackson.databind.JsonNode;
import newpixserver.database.entities.User;

public interface ConnectedOperation extends Operation {
    Object execute(JsonNode payload, boolean isConnected) throws Exception;

    @Override
    default Object execute(JsonNode payload) throws Exception {
        throw new UnsupportedOperationException("Esta é uma operação requer que o cliente esteja conectado.");
    }
}
