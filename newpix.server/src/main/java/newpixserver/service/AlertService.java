package newpixserver.service;

import newpixserver.service.listener.AlertListener;

import java.util.ArrayList;
import java.util.List;

public class AlertService {
    private final List<AlertListener> listeners = new ArrayList<>();

    public void addListener(AlertListener listener) {
        listeners.add(listener);
    }

    public void sendAlert(String title, String message) {
        for (AlertListener listener : listeners) {
            listener.onServerAlert(title, message);
        }
    }
}
