package newpixserver.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixserver.database.entities.User;
import newpixserver.messages.operation.AuthenticatedOperation;
import newpixserver.service.UserService;
import newpixserver.tcp.ClientThread;

public class UserUpdate implements AuthenticatedOperation {
    private final UserService userService;
    private final ClientThread clientThread;

    public UserUpdate(UserService userService, ClientThread clientThread) {
        this.userService = userService;
        this.clientThread = clientThread;
    }

    @Override
    public String execute(JsonNode payload, User authenticatedUser) throws Exception {
        JsonNode userNode = payload.path("usuario");

        String name = userNode.path("nome").asText(null);
        String password = userNode.path("senha").asText(null);

        if ((name == null || name.isEmpty()) && (password == null || password.isEmpty())) {
            throw new IllegalArgumentException("Forneça um nome ou uma senha para atualizar.");
        }

        String successMessage = userService.updateUser(authenticatedUser, name, password);

        if (name != null && !name.isEmpty()) {
            clientThread.updateClientName(name);
        }

        return successMessage;
    }


}
