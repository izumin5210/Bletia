package info.izumin.android.bletia.action;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import info.izumin.android.bletia.core.BleErrorType;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.util.NotificationUtils;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/15/15.
 */
public class EnableNotificationAction extends CharacteristicAction {

    private final boolean mEnabled;

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
