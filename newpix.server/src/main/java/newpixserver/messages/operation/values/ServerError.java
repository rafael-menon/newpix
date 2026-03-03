package newpixserver.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixserver.messages.operation.Operation;
import newpixserver.service.AlertService;

public class ServerError implements Operation {
    private final AlertService alertService;

    public ServerError(AlertService alertService) {
        this.alertService = alertService;
    }

    @Override
    public Object execute(JsonNode payload) throws Exception {
        String operacao = payload.path("operacao").asText();
        String operacaoEnviada = payload.path("operacao_enviada").asText();
        String info = payload.path("info").asText();

        alertService.sendAlert("Erro no servidor (\"operacao\":  \"" + operacao + "\")", "Operação enviada pelo servidor: \"" + operacaoEnviada + "\" Info: \"" + info + " \"");

        return operacaoEnviada;
    }
}
