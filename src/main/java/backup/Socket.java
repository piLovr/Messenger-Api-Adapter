package backup;

import backup.adapters.ExtendedMessage;
import backup.adapters.Message;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

public abstract class Socket implements UnifiedSocket {
    protected String alias;

    public String getAlias() {
        return alias;
    }
    @Getter protected boolean connected = false;
    protected List<Listener> listeners;

    public Socket(String alias) {
        this.alias = alias;
        listeners = new LinkedList<>();

    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void fireOnMessage(ExtendedMessage extendedMessage) {
        for (Listener listener : listeners) {
            listener.onMessage(extendedMessage);
        }
    }

    public void fireOnSlashCommand(Message message) {
        for (Listener listener : listeners) {
            listener.onSlashCommand(message);
        }
    }

    public void fireOnReaction(Message message) {
        for (Listener listener : listeners) {
            listener.onReaction(message);
        }
    }
}
