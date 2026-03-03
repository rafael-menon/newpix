package newpixserver.messages.operation;

import newpixserver.messages.operation.values.*;
import newpixserver.service.AlertService;
import newpixserver.service.ServiceManager;
import newpixserver.service.UserService;
import newpixserver.tcp.ClientThread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OperationRegistry {
    @FunctionalInterface
    private interface OperationCreator {
        Operation create(ServiceManager service, ClientThread clientThread);
    }

    private static final Map<String, OperationCreator> registry = new ConcurrentHashMap<>();

    static {
        registry.put(OperationCode.CONNECT, (service, clientThread) -> new Connect(clientThread));
        registry.put(OperationCode.USER_LOGIN, (service, clientThread) -> new UserLogin(service.getService(UserService.class), clientThread));
        registry.put(OperationCode.USER_LOGOUT, (service, clientThread) -> new UserLogout(service.getService(UserService.class), clientThread));
        registry.put(OperationCode.USER_CREATE, (service, clientThread) -> new UserCreate(service.getService(UserService.class)));
        registry.put(OperationCode.USER_READ, (service, clientThread) -> new UserRead(service.getService(UserService.class)));
        registry.put(OperationCode.USER_UPDATE, (service, clientThread) -> new UserUpdate(service.getService(UserService.class), clientThread));
        registry.put(OperationCode.USER_DELETE, (service, clientThread) -> new UserDelete(service.getService(UserService.class)));
        registry.put(OperationCode.TRANSACTION_CREATE, (service, clientThread) -> new TransactionCreate(service.getService(UserService.class)));
        registry.put(OperationCode.TRANSACTION_READ, (service, clientThread) -> new TransactionRead(service.getService(UserService.class)));
        registry.put(OperationCode.USER_DEPOSIT, (service, clientThread) -> new UserDeposit(service.getService(UserService.class)));
        registry.put(OperationCode.SERVER_ERROR, (service, clientThread) -> new ServerError(service.getService(AlertService.class)));
    }

    public static Operation getOperation(String operationName, ServiceManager service, ClientThread clientThread) {
        OperationCreator creator = registry.get(operationName);
        if (creator == null) {
            throw new IllegalArgumentException("Operação desconhecida: " + operationName);
        }
        return creator.create(service, clientThread);
    }
}
