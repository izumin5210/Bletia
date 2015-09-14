package info.izumin.android.bletia;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Bundle;
import android.os.Message;

import org.jdeferred.Deferred;

import java.util.UUID;

import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/7/15.
 */
public class BleEvent<T> {
    enum Type {
        WRITE_CHARACTERISTIC(1) {
            @Override
            public void handle(BluetoothGattWrapper gattWrapper, BleEvent event) {
                gattWrapper.writeCharacteristic((BluetoothGattCharacteristic) event.getValue());
            }
        },
        READ_CHARACTERISTIC(2) {
            @Override
            public void handle(BluetoothGattWrapper gattWrapper, BleEvent event) {
                gattWrapper.readCharacteristic((BluetoothGattCharacteristic) event.getValue());
            }
        },
        WRITE_DESCRIPTOR(3) {
            @Override
            public void handle(BluetoothGattWrapper gattWrapper, BleEvent event) {
                gattWrapper.writeDescriptor((BluetoothGattDescriptor) event.getValue());
            }
        },
        READ_DESCRIPTOR(4) {
            @Override
            public void handle(BluetoothGattWrapper gattWrapper, BleEvent event) {
                gattWrapper.readDescriptor((BluetoothGattDescriptor) event.getValue());
            }
        };

        private final int mWhat;

        Type(int what) {
            mWhat = what;
        }

        public int getWhat() {
            return mWhat;
        }

        public abstract void handle(BluetoothGattWrapper gattWrapper, BleEvent event);

        public static Type valueOf(int what) {
            for (Type type : values()) {
                if (type.getWhat() == what) { return type; }
            }
            throw new IllegalArgumentException();
        }
    }

    public static final String KEY_UUID = "event uuid";

    private final Type mType;
    private final UUID mUuid;
    private final T mValue;

    private Deferred<T, BletiaException, ?> mDeferred;

    public BleEvent(Type type, UUID uuid, T value) {
        mType = type;
        mUuid = uuid;
        mValue = value;
    }

    public Type getType() {
        return mType;
    }

    public UUID getUuid() {
        return mUuid;
    }

    public T getValue() {
        return mValue;
    }

    public Deferred<T, BletiaException, ?> getDeferred() {
        return mDeferred;
    }

    public void setDeferred(Deferred<T, BletiaException, ?> deferred) {
        mDeferred = deferred;
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
