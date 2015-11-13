package info.izumin.android.bletia.action;

import android.bluetooth.BluetoothGattCharacteristic;

import org.jdeferred.Promise;

import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.action.AbstractEnableNotificationAction;

/**
 * Created by izumin on 9/15/15.
 */
public class EnableNotificationAction extends AbstractEnableNotificationAction<Promise<BluetoothGattCharacteristic, BletiaException, Void>> {

    public EnableNotificationAction(BluetoothGattCharacteristic characteristic, boolean enabled) {
        super(characteristic, enabled, new DeferredStrategy<BluetoothGattCharacteristic, BletiaException>());
    }
}
