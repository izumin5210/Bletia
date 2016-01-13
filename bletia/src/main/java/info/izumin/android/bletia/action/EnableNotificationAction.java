package info.izumin.android.bletia.action;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

import info.izumin.android.bletia.BleErrorType;
import info.izumin.android.bletia.BletiaException;
import info.izumin.android.bletia.util.NotificationUtils;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/15/15.
 */
public class EnableNotificationAction extends CharacteristicAction {

    private final boolean mEnabled;

    public EnableNotificationAction(BluetoothGattService service, UUID uuid, boolean enabled) {
        super(service, uuid);
        mEnabled = enabled;
    }

    public EnableNotificationAction(BluetoothGattCharacteristic characteristic, boolean enabled) {
        super(characteristic);
        mEnabled = enabled;
    }

    @Override
    public Type getType() {
        return Type.ENABLE_NOTIFICATION;
    }

    @Override
    public boolean execute(BluetoothGattWrapper gattWrapper) {
        if (gattWrapper.setCharacteristicNotification(getCharacteristic(), mEnabled)) {
            BluetoothGattDescriptor descriptor = NotificationUtils.getDescriptor(getCharacteristic(), mEnabled);
            if (!gattWrapper.writeDescriptor(descriptor)) {
                getDeferred().reject(new BletiaException(this, BleErrorType.OPERATION_INITIATED_FAILURE));
                return false;
            }
        } else {
            getDeferred().reject(new BletiaException(this, BleErrorType.REQUEST_FAILURE));
            return false;
        }
        return true;
    }

    public boolean getEnabled() {
        return mEnabled;
    }
}
