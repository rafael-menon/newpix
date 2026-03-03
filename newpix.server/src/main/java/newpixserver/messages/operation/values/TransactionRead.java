package newpixserver.messages.operation.values;

import com.fasterxml.jackson.databind.JsonNode;
import newpixserver.database.entities.User;
import newpixserver.messages.json.TransactionJson;
import newpixserver.messages.operation.AuthenticatedOperation;
import newpixserver.service.UserService;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

public class TransactionRead implements AuthenticatedOperation {
    private final UserService userService;

    public TransactionRead(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<TransactionJson> execute(JsonNode payload, User authenticatedUser) throws Exception {
        String startDate = payload.path("data_inicial").asText();
        String endDate = payload.path("data_final").asText();

        LocalDate start = ZonedDateTime.parse(startDate).toLocalDate();
        LocalDate end = ZonedDateTime.parse(endDate).toLocalDate();

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("A data inicial não pode ser posterior à data final.");
        }

        LocalDate maxAllowedDate = start.plusMonths(1);

        if (end.isAfter(maxAllowedDate)) {
            throw new IllegalArgumentException("O período de busca não pode exceder 1 mês.");
        }

        return userService.transactionRead(authenticatedUser.getToken(), startDate, endDate);
    }
}
