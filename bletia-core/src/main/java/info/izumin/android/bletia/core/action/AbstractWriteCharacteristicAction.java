package info.izumin.android.bletia.core.action;

import android.bluetooth.BluetoothGattCharacteristic;

import info.izumin.android.bletia.core.BleErrorType;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.ActionResolver;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/15/15.
 */
public abstract class AbstractWriteCharacteristicAction extends AbstractCharacteristicAction {

    public AbstractWriteCharacteristicAction(BluetoothGattCharacteristic characteristic,
                                             ActionResolver<BluetoothGattCharacteristic, BletiaException> actionResolver) {
        super(characteristic, Type.WRITE_CHARACTERISTIC, actionResolver);
    }

    @Override
    public boolean execute(BluetoothGattWrapper gattWrapper) {
        if (!gattWrapper.writeCharacteristic(getCharacteristic())) {
            reject(new BletiaException(this, BleErrorType.OPERATION_INITIATED_FAILURE));
            return false;
        }
        return true;
    }
}
