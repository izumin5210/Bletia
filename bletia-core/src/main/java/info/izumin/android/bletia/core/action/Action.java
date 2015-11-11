package info.izumin.android.bletia.core.action;

import android.os.Bundle;
import android.os.Message;

import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 10/9/15.
 */
public abstract class Action<T, I> {
    public enum Type {
        WRITE_CHARACTERISTIC(1),
        READ_CHARACTERISTIC(2),
        WRITE_DESCRIPTOR(3),
        READ_DESCRIPTOR(4),
        ENABLE_NOTIFICATION(5),
        READ_REMOTE_RSSI(6);

        private final int mCode;

        Type(int code) {
            mCode = code;
        }

        public int getCode() {
            return mCode;
        }

        public static Type valueOf(int code) {
            for (Type type : values()) {
                if (type.getCode() == code) { return type; }
            }
            throw new IllegalArgumentException();
        }
    }

    public static final String KEY_UUID = "key_uuid";

    private final I mIdentity;

    public Action(I identity) {
        mIdentity = identity;
    }

    public I getIdentity() {
        return mIdentity;
    }

    public Message obtainMessage() {
        Message message = Message.obtain();
        message.what = getType().getCode();
        message.setData(getBundle());
        return message;
    }

    public Bundle getBundle() {
        return new Bundle();
    }

    public abstract Type getType();
    public abstract boolean execute(BluetoothGattWrapper gattWrapper);
    public abstract void resolve(T value);
    public abstract void reject(BletiaException throwable);
}
