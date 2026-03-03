package newpixserver.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixserver.messages.operation.ConnectedOperation;
import newpixserver.service.UserService;
import newpixserver.type.Cpf;
import newpixserver.type.Name;
import newpixserver.type.Password;
import newpixserver.utils.AuthenticationException;
import newpixserver.utils.IllegalOperation;

public class UserCreate implements ConnectedOperation {
    private final UserService userService;

    public UserCreate(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object execute(JsonNode payload, boolean isConnected) throws Exception {
        if(!isConnected) {
            throw new IllegalOperation("Operação negada. A primeira requisição deve ser \"conectar\".");
        }

        if(payload.path("nome").asText().isEmpty() || payload.path("cpf").asText().isEmpty() || payload.path("senha").asText().isEmpty()) {
            throw new AuthenticationException("Preencha os campos.");
        }

        Name name = new Name(payload.path("nome").asText());
        Cpf cpf = new Cpf(payload.path("cpf").asText());
        Password password = new Password(payload.path("senha").asText());

        userService.userCreate(name, cpf, password);

        return null;
    }
}
