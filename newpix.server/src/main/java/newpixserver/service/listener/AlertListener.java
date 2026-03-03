package newpixserver.service.listener;

public interface AlertListener {
    void onServerAlert(String title, String message);
}
