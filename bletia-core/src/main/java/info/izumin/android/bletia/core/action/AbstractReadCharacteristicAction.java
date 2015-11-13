package info.izumin.android.bletia.core.action;

import android.bluetooth.BluetoothGattCharacteristic;

import info.izumin.android.bletia.core.BleErrorType;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.ActionResolver;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/15/15.
 */
public abstract class AbstractReadCharacteristicAction extends AbstractCharacteristicAction {

    public AbstractReadCharacteristicAction(BluetoothGattCharacteristic characteristic,
                                            ActionResolver<BluetoothGattCharacteristic, BletiaException> actionResolver) {
        super(characteristic, Type.READ_CHARACTERISTIC, actionResolver);
    }

    @Override
    public boolean execute(BluetoothGattWrapper gattWrapper) {
        if (!gattWrapper.readCharacteristic(getCharacteristic())) {
            reject(new BletiaException(this, BleErrorType.OPERATION_INITIATED_FAILURE));
            return false;
        }
        return true;
    }
}
