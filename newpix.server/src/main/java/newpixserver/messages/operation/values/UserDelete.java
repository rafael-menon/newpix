package newpixserver.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixserver.database.entities.User;
import newpixserver.messages.operation.AuthenticatedOperation;
import newpixserver.service.UserService;

public class UserDelete implements AuthenticatedOperation {
    private final UserService userService;

    public UserDelete(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object execute(JsonNode payload, User authenticatedUser) throws Exception {
        userService.deactivateUser(authenticatedUser.getToken());

        return null;
    }
}
