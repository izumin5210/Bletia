package info.izumin.android.bletia;

import android.bluetooth.BluetoothGattCharacteristic;

import org.jdeferred.Deferred;

import java.util.UUID;

/**
 * Created by izumin on 9/7/15.
 */
public class BleEvent {
    enum Type {
        WRITING_CHARACTERISTIC,
        READING_CHARACTERISTIC
    }

    private final UUID mUuid;
    private final Deferred mDeferred;
    private BluetoothGattCharacteristic mCharacteristic;

    public BleEvent(UUID uuid, Deferred deferred) {
        mUuid = uuid;
        mDeferred = deferred;
    }

    public UUID getUuid() {
        return mUuid;
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
