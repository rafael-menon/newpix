package newpixclient.messages.operation;

import newpixclient.messages.operation.values.*;
import newpixclient.service.ServiceManager;
import newpixclient.service.ClientService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OperationRegistry {
    @FunctionalInterface
    private interface OperationCreator {
        Operation create(ServiceManager service);
    }

    private static final Map<String, OperationCreator> registry = new ConcurrentHashMap<>();

    static {
        registry.put(OperationCode.CONNECT, (service) -> new Connect(service.getService(ClientService.class)));
        registry.put(OperationCode.USER_LOGIN, (service) -> new UserLogin(service.getService(ClientService.class)));
        registry.put(OperationCode.USER_LOGOUT, (service) -> new UserLogout(service.getService(ClientService.class)));
        registry.put(OperationCode.USER_CREATE, (service) -> new UserCreate(service.getService(ClientService.class)));
        registry.put(OperationCode.USER_READ, (service) -> new UserRead(service.getService(ClientService.class)));
        registry.put(OperationCode.USER_UPDATE, (service) -> new UserUpdate(service.getService(ClientService.class)));
        registry.put(OperationCode.USER_DELETE, (service) -> new UserDelete(service.getService(ClientService.class)));
        registry.put(OperationCode.TRANSACTION_CREATE, (service) -> new TransactionCreate(service.getService(ClientService.class)));
        registry.put(OperationCode.TRANSACTION_READ, (service) -> new TransactionRead(service.getService(ClientService.class)));
        registry.put(OperationCode.USER_DEPOSIT, (service) -> new UserDeposit(service.getService(ClientService.class)));
        registry.put(OperationCode.SERVER_ERROR, (service) -> new ServerError(service.getService(ClientService.class)));
    }

    public static Operation getOperation(String operationName, ServiceManager service) {
        OperationCreator creator = registry.get(operationName);
        if (creator == null) {
            throw new IllegalArgumentException("Operação desconhecida: " + operationName);
        }
        return creator.create(service);
    }
}
