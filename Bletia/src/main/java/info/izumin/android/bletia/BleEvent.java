package info.izumin.android.bletia;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.os.Message;

import org.jdeferred.Deferred;

import java.util.UUID;

/**
 * Created by izumin on 9/7/15.
 */
public class BleEvent {
    enum Type {
        WRITE_CHARACTERISTIC(1),
        READ_CHARACTERISTIC(2),
        WRITE_DESCRIPTOR(3),
        READ_DESCRIPTOR(4);

        private final int mWhat;

        Type(int what) {
            mWhat = what;
        }

        public int getWhat() {
            return mWhat;
        }
    }

    public static final String KEY_UUID = "event uuid";

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

    public Message obtainMessage() {
        Message message = Message.obtain();
        message.what = mType.getWhat();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_UUID, mUuid);
        message.setData(bundle);
        return message;
    }
}
