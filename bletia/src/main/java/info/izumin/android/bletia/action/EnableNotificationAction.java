package info.izumin.android.bletia.action;

import android.bluetooth.BluetoothGattCharacteristic;

import info.izumin.android.bletia.DeferredResolver;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.action.AbstractEnableNotificationAction;

/**
 * Created by izumin on 9/15/15.
 */
public class EnableNotificationAction extends AbstractEnableNotificationAction {

    public EnableNotificationAction(BluetoothGattCharacteristic characteristic, boolean enabled) {
        super(characteristic, enabled, new DeferredResolver<BluetoothGattCharacteristic, BletiaException>());
    }
}
