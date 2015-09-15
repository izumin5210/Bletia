package info.izumin.android.bletia;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Bundle;
import android.os.Message;

import org.jdeferred.Deferred;

import java.util.UUID;

import info.izumin.android.bletia.util.NotificationUtils;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/7/15.
 */
public class BleEvent<T> {
    enum Type {
        WRITE_CHARACTERISTIC(1) {
            @Override
            public void handle(BluetoothGattWrapper gattWrapper, BleEvent event) {
                if (!gattWrapper.writeCharacteristic((BluetoothGattCharacteristic) event.getValue())) {
                    reject(event.getDeferred(), (BluetoothGattCharacteristic) event.getValue());
                }
            }
        },
        READ_CHARACTERISTIC(2) {
            @Override
            public void handle(BluetoothGattWrapper gattWrapper, BleEvent event) {
                if (!gattWrapper.readCharacteristic((BluetoothGattCharacteristic) event.getValue())) {
                    reject(event.getDeferred(), (BluetoothGattCharacteristic) event.getValue());
                }
            }
        },
        WRITE_DESCRIPTOR(3) {
            @Override
            public void handle(BluetoothGattWrapper gattWrapper, BleEvent event) {
                if (!gattWrapper.writeDescriptor((BluetoothGattDescriptor) event.getValue())) {
                    reject(event.getDeferred(), (BluetoothGattDescriptor) event.getValue());
                }
            }
        },
        READ_DESCRIPTOR(4) {
            @Override
            public void handle(BluetoothGattWrapper gattWrapper, BleEvent event) {
                if (!gattWrapper.readDescriptor((BluetoothGattDescriptor) event.getValue())) {
                    reject(event.getDeferred(), (BluetoothGattDescriptor) event.getValue());
                }
            }
        },
        ENABLE_NOTIFICATION(5) {
            @Override
            public void handle(BluetoothGattWrapper gattWrapper, BleEvent event) {
                BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) event.getValue();
                if (gattWrapper.setCharacteristicNotification(characteristic, true)) {
                    BluetoothGattDescriptor descriptor = NotificationUtils.getDescriptor(characteristic, true);
                    if (!gattWrapper.writeDescriptor(descriptor)) {
                        reject(event.getDeferred(), characteristic, descriptor);
                    }
                } else {
                    reject(BleErrorType.REQUEST_FAILURE, event.getDeferred(), characteristic, null);
                }
            }
        },
        DISABLE_NOTIFICATION(6) {
            @Override
            public void handle(BluetoothGattWrapper gattWrapper, BleEvent event) {
                BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) event.getValue();
                if (gattWrapper.setCharacteristicNotification(characteristic, true)) {
                    BluetoothGattDescriptor descriptor = NotificationUtils.getDescriptor(characteristic, false);
                    if (!gattWrapper.writeDescriptor(descriptor)) {
                        reject(event.getDeferred(), characteristic, descriptor);
                    }
                } else {
                    reject(BleErrorType.REQUEST_FAILURE, event.getDeferred(), characteristic, null);
                }
            }
        };

        private final int mWhat;

        Type(int what) {
            mWhat = what;
        }

        public int getWhat() {
            return mWhat;
        }

        public void reject(BleErrorType type, Deferred<? , BletiaException, ?> deferred,
                           BluetoothGattCharacteristic characteristic, BluetoothGattDescriptor descriptor) {
            deferred.reject(new BletiaException(type, characteristic, descriptor));
        }

        public void reject(Deferred<BluetoothGattCharacteristic, BletiaException, ?> deferred, BluetoothGattCharacteristic characteristic) {
            reject(BleErrorType.OPERATION_INITIATED_FAILURE, deferred, characteristic, null);
        }

        public void reject(Deferred<BluetoothGattDescriptor, BletiaException, ?> deferred, BluetoothGattDescriptor descriptor) {
            reject(BleErrorType.OPERATION_INITIATED_FAILURE, deferred, null, descriptor);
        }

        public void reject(Deferred<BluetoothGattDescriptor, BletiaException, ?> deferred, BluetoothGattCharacteristic characteristic, BluetoothGattDescriptor descriptor) {
            deferred.reject(new BletiaException(BleErrorType.OPERATION_INITIATED_FAILURE, characteristic, descriptor));
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
