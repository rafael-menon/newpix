package newpixserver.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixserver.database.entities.User;
import newpixserver.messages.json.UserJson;
import newpixserver.messages.operation.AuthenticatedOperation;
import newpixserver.service.UserService;

public class UserRead implements AuthenticatedOperation {
    private final UserService userService;

    public UserRead(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserJson execute(JsonNode payload, User authenticatedUser) throws Exception {
        return new UserJson(authenticatedUser.getCpf(), authenticatedUser.getBalance(), authenticatedUser.getName());
    }
}
