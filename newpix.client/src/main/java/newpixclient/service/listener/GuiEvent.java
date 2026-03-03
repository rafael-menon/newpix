package newpixclient.service.listener;

import newpixclient.json.TransactionViewModel;
import newpixclient.json.UserJson;

import java.util.List;

public interface GuiEvent {

	void socketConnectionSuccessful();

	void socketConnectionFailed(String errorMessage);
}
