package newpixserver.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixserver.database.entities.User;
import newpixserver.messages.operation.AuthenticatedOperation;
import newpixserver.service.UserService;
import newpixserver.type.Cpf;

public class TransactionCreate implements AuthenticatedOperation {
    private final UserService userService;

    public TransactionCreate(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object execute(JsonNode payload, User authenticatedUser) throws Exception {
        Double amount = payload.path("valor").asDouble();
        Cpf destinationCpf = new Cpf(payload.path("cpf_destino").asText());

        userService.createTransaction(authenticatedUser.getToken(), amount, destinationCpf.getString());

        return null;
    }
}
