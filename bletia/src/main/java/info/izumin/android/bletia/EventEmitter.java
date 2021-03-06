package info.izumin.android.bletia;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.ArrayList;
import java.util.List;

import info.izumin.android.bletia.core.BleState;
import info.izumin.android.bletia.core.BletiaException;

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

    public void emitCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
        for (BletiaListener listener : mListeners) {
            listener.onCharacteristicChanged(mBletia, characteristic);
        }
    }

    public void emitError(BletiaException exception, BleState state) {
        for (BletiaListener listener : mListeners) {
            listener.onError(exception, state);
        }
    }
}
