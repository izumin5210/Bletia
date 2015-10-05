package info.izumin.android.bletia;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by izumin on 9/10/15.
 */
class EventEmitter {

    private final Bletia mBletia;
    private final List<BletiaListener> mListeners;

    public EventEmitter(Bletia bletia) {
        mBletia = bletia;
        mListeners = new ArrayList<>();
    }

    public void addListener(BletiaListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(BletiaListener listener) {
        mListeners.remove(listener);
    }

    public void emitConnectEvent() {
        for (BletiaListener listener : mListeners) {
            listener.onConnect(mBletia);
        }
    }

    public void emitDisconnectEvent() {
        for (BletiaListener listener : mListeners) {
            listener.onDisconnect(mBletia);
        }
    }

    public void emitServiceDiscovered(int status) {
        for (BletiaListener listener : mListeners) {
            listener.onServicesDiscovered(mBletia, status);
        }
    }

    public void emitCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
        for (BletiaListener listener : mListeners) {
            listener.onCharacteristicChanged(mBletia, characteristic);
        }
    }

    public void emitError(BletiaException exception) {
        for (BletiaListener listener : mListeners) {
            listener.onError(exception);
        }
    }
}
