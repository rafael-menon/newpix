package newpixserver.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixserver.database.entities.User;
import newpixserver.messages.operation.AuthenticatedOperation;
import newpixserver.service.UserService;
import newpixserver.tcp.ClientThread;

public class UserLogout implements AuthenticatedOperation {
    private final UserService userService;
    private final ClientThread clientThread;

    public UserLogout(UserService userService, ClientThread clientThread) {
        this.userService = userService;
        this.clientThread = clientThread;
    }

    @Override
    public Object execute(JsonNode payload, User authenticatedUser) throws Exception {
        userService.userLogout(authenticatedUser);

        if (clientThread != null) {
            clientThread.updateClientName("Aguardando login...");
            clientThread.clearAuthentication();
        }

        return null;
    }
}
