package newpixserver.messages.client_state.type;

import com.fasterxml.jackson.databind.JsonNode;
import newpixserver.database.entities.User;
import newpixserver.messages.MessageHandler;
import newpixserver.messages.client_state.StateResult;
import newpixserver.messages.operation.*;
import newpixserver.service.UserService;

public class Authenticated extends AbstractClientState {
    @Override
    protected StateResult handleStateSpecificOperation(MessageHandler context, JsonNode payload, String operationName, Operation operation) throws Exception {
        if (!(operation instanceof AuthenticatedOperation authenticatedOperation)) {
            throw new SecurityException("Operação '" + operationName + "' negada. Você já está logado.");
        }

        User cachedUser = context.getClientThread().getAuthenticatedUser();
        if (cachedUser == null) {
            return StateResult.transition(new Connected());
        }

        JsonNode tokenNode = payload.path("token");
        if (tokenNode.isMissingNode() || tokenNode.asText().isEmpty()) {
            throw new SecurityException("Token de autenticação não fornecido.");
        }
        String tokenFromRequest = tokenNode.asText();

        if (!tokenFromRequest.equals(cachedUser.getToken())) {
            throw new SecurityException("Violação de segurança: O token não corresponde a esta sessão.");
        }

        UserService userService = context.getServiceManager().getService(UserService.class);
        User userFromDb = userService.getUserByToken(tokenFromRequest);
        if (userFromDb == null) {
            context.getClientThread().clearAuthentication();
            throw new SecurityException("Token inválido ou expirado.");
        }

        context.getClientThread().setAuthenticatedUser(userFromDb);

        Object result = authenticatedOperation.execute(payload, context.getClientThread().getAuthenticatedUser());

        if (operationName.equals(OperationCode.USER_LOGOUT)) {
            return StateResult.transition(new Connected());
        }

        return StateResult.unchanged(result);
    }

    @Override
    protected StateResult connectOp(Object result) {
        return StateResult.unchanged(result);
    }

    @Override
    protected StateResult serverErrorOp(Object result) {
        return StateResult.unchanged(result);
    }

    @Override
    public ClientStateType getType() {
        return ClientStateType.AUTHENTICATED;
    }
}
