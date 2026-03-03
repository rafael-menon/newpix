package newpixserver.messages.operation;

import com.fasterxml.jackson.databind.JsonNode;
import newpixserver.database.entities.User;

public interface AuthenticatedOperation extends Operation {
    Object execute(JsonNode payload, User authenticatedUser) throws Exception;

    @Override
    default Object execute(JsonNode payload) throws Exception {
        throw new UnsupportedOperationException("Esta operação requer que o usuário esteja autenticado.");
    }
}
