package info.izumin.android.bletia.core.action;

import android.bluetooth.BluetoothGattCharacteristic;

import info.izumin.android.bletia.core.BleErrorType;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.ResolveStrategy;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/15/15.
 */
public abstract class AbstractWriteCharacteristicAction extends AbstractCharacteristicAction {

    public AbstractWriteCharacteristicAction(BluetoothGattCharacteristic characteristic,
                                             ResolveStrategy<BluetoothGattCharacteristic, BletiaException> resolveStrategy) {
        super(characteristic, Type.WRITE_CHARACTERISTIC, resolveStrategy);
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
