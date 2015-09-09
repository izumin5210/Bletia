package info.izumin.android.bletia;

import android.bluetooth.BluetoothGattCharacteristic;

import org.jdeferred.Deferred;

/**
 * Created by izumin on 9/7/15.
 */
public class BleEvent {
    enum Type {
        WRITING_CHARACTERISTIC,
        READING_CHARACTERISTIC
    }

    private final Deferred mDeferred;
    private BluetoothGattCharacteristic mCharacteristic;

    public BleEvent(Deferred deferred) {
        mDeferred = deferred;
    }

    public <D, F, P> Deferred<D, F, P> getDeferred() {
        return mDeferred;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return mCharacteristic;
    }

    public void setCharacteristic(BluetoothGattCharacteristic characteristic) {
        mCharacteristic = characteristic;
    }
}
