package info.izumin.android.bletia.action;

import android.os.Bundle;
import android.os.Message;

import org.jdeferred.Deferred;
import org.jdeferred.impl.DeferredObject;

import info.izumin.android.bletia.BletiaException;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/15/15.
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
    private static final int DEFAULT_TIMEOUT_MILLIS = 10 * 1000;

    private final I mIdentity;
    private final Deferred<T, BletiaException, Void> mDeferred;

    private long mTimeoutMillis = DEFAULT_TIMEOUT_MILLIS;

    private boolean mDequeued = false;

    public Action(I identity) {
        mIdentity = identity;
        mDeferred = new DeferredObject<>();
    }

    public Deferred<T, BletiaException, Void> getDeferred() {
        return mDeferred;
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

    public long getTimeoutMillis() {
        return mTimeoutMillis;
    }

    protected void setTimeoutMillis(long timeoutMillis) {
        mTimeoutMillis = timeoutMillis;
    }

    public void setDequeued(boolean dequeued) {
        mDequeued = dequeued;
    }

    public boolean isDequeued() {
        return mDequeued;
    }

    public abstract Type getType();
    public abstract boolean execute(BluetoothGattWrapper gattWrapper);
}
