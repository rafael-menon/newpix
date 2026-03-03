package newpixserver.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixserver.database.entities.User;
import newpixserver.messages.operation.AuthenticatedOperation;
import newpixserver.service.UserService;

public class UserDeposit implements AuthenticatedOperation {
    private final UserService userService;

    public UserDeposit(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object execute(JsonNode payload, User authenticatedUser) throws Exception {
        Double sentValue = payload.path("valor_enviado").asDouble();
        userService.deposit(authenticatedUser.getToken(), sentValue);

        return null;
    }
}
