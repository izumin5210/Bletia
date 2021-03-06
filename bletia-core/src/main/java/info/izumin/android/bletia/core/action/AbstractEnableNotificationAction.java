package info.izumin.android.bletia.core.action;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import info.izumin.android.bletia.core.BleErrorType;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.ResolveStrategy;
import info.izumin.android.bletia.core.util.NotificationUtils;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/15/15.
 */
public abstract class AbstractEnableNotificationAction<R> extends AbstractCharacteristicAction<R> {

    private final boolean mEnabled;

    public AbstractEnableNotificationAction(BluetoothGattCharacteristic characteristic, boolean enabled,
                                            ResolveStrategy<BluetoothGattCharacteristic, BletiaException, R> resolveStrategy) {
        super(characteristic, Type.ENABLE_NOTIFICATION, resolveStrategy);
        mEnabled = enabled;
    }

    @Override
    public boolean execute(BluetoothGattWrapper gattWrapper) {
        if (gattWrapper.setCharacteristicNotification(getCharacteristic(), mEnabled)) {
            BluetoothGattDescriptor descriptor = NotificationUtils.getDescriptor(getCharacteristic(), mEnabled);
            if (!gattWrapper.writeDescriptor(descriptor)) {
                reject(new BletiaException(this, BleErrorType.OPERATION_INITIATED_FAILURE));
                return false;
            }
        } else {
            reject(new BletiaException(this, BleErrorType.REQUEST_FAILURE));
            return false;
        }
        return true;
    }

    public boolean getEnabled() {
        return mEnabled;
    }
}
