package info.izumin.android.bletia;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by izumin on 9/10/15.
 */
public class EventEmitter {

    private List<BletiaListener> mListeners = new ArrayList<>();

    public void addListener(BletiaListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(BletiaListener listener) {
        mListeners.remove(listener);
    }

    public void emitConnectEvent() {
        for (BletiaListener listener : mListeners) {
            listener.onConnect();
        }
    }

    public void emitDisconnectEvent() {
        for (BletiaListener listener : mListeners) {
            listener.onDisconnect();
        }
    }

    public void emitError(BletiaException exception) {
        for (BletiaListener listener : mListeners) {
            listener.onError(exception);
        }
    }
}
