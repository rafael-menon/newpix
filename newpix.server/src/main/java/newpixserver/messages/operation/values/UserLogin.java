package newpixserver.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixserver.database.entities.User;
import newpixserver.messages.operation.ConnectedOperation;
import newpixserver.service.UserService;
import newpixserver.tcp.ClientThread;
import newpixserver.type.Cpf;
import newpixserver.type.Password;
import newpixserver.utils.AuthenticationException;
import newpixserver.utils.IllegalOperation;

public class UserLogin implements ConnectedOperation {
    private final UserService userService;
    private final ClientThread clientThread;

    public UserLogin(UserService userService, ClientThread clientThread) {
        this.userService = userService;
        this.clientThread = clientThread;
    }

    @Override
    public String execute(JsonNode payload, boolean isConnected) throws Exception {
        if(!isConnected) {
            throw new IllegalOperation("Operação negada. A primeira requisição deve ser \"conectar\".");
        }

        if(payload.path("cpf").asText().isEmpty() || payload.path("senha").asText().isEmpty()) {
            throw new AuthenticationException("Preencha os campos.");
        }

        Cpf cpf = new Cpf(payload.path("cpf").asText());
        Password requestedPassword = new Password(payload.path("senha").asText());

        User loggedInUser = userService.userLogin(cpf, requestedPassword);

        clientThread.updateClientName(loggedInUser.getName());
        clientThread.setAuthenticatedUser(loggedInUser);

        return loggedInUser.getToken();
    }
}
