package newpixclient.service;

import newpixclient.json.TransactionViewModel;
import newpixclient.json.UserJson;

import java.util.List;

public class ClientService {
    private final GuiService guiService;

    public ClientService(GuiService guiService) {
        this.guiService = guiService;
    }

    public void connect(boolean status, String info) {
        guiService.connectionOperationResult(status, info);
    }

    public void userCreate(boolean status, String info) {
        guiService.createUserOperationResult(status, info);
    }

    public void userDelete(boolean status, String info) {
       guiService.userDeleteOperationResult(status, info);
    }

    public void userLogin(boolean status, String info) {
        if(!status) {
            guiService.userLoginOperationError(info);
        }
    }

    public void userLogout(boolean status, String info) {
        guiService.userLogoutOperationResult(status, info);
    }

    public void userRead(boolean status, String info, UserJson userJson) {
        guiService.userReadOperationResult(status, info, userJson);
    }

    public void userUpdate(boolean status, String info) {
        guiService.userUpdateOperationResult(status, info);
    }

    public void transactionCreate(boolean status, String info) {
        guiService.transactionsCreateResult(status, info);
    }

    public void transactionRead(boolean status, String info,  List<TransactionViewModel> transactions) {
        guiService.transactionsReadResult(status, info, transactions);
    }

    public void deposit(boolean status,String info) {
        guiService.depositOperationResult(status, info);
    }
}
