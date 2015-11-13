package info.izumin.android.bletia.core.action;

import android.os.Bundle;
import android.os.Message;

import java.io.Serializable;

import info.izumin.android.bletia.core.ActionResolver;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 10/9/15.
 */
public abstract class AbstractAction<T, E extends Throwable, I> {
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

    public static final String KEY_IDENTITY = "key_identity";

    private final I mIdentity;
    private final Type mType;
    private final ActionResolver<T, E> mActionResolver;

    public AbstractAction(I identity, Type type, ActionResolver<T, E> actionResolver) {
        mIdentity = identity;
        mType = type;
        mActionResolver = actionResolver;
    }

    public final I getIdentity() {
        return mIdentity;
    }

    public final Type getType() {
        return mType;
    }

    public final ActionResolver<T, E> getResolver() {
        return mActionResolver;
    }

    public void resolve(T value) {
        mActionResolver.resolve(value);
    }

    public void reject(E throwable) {
        mActionResolver.reject(throwable);
    }

    public final Message obtainMessage() {
        Message message = Message.obtain();
        message.what = mType.getCode();
        message.setData(getBundle());
        return message;
    }

    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        if (mIdentity instanceof Serializable) {
            bundle.putSerializable(KEY_IDENTITY, (Serializable) mIdentity);
        }
        return bundle;
    }

    public abstract boolean execute(BluetoothGattWrapper gattWrapper);
}
