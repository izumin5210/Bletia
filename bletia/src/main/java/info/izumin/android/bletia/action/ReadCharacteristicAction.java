package info.izumin.android.bletia.action;

import android.bluetooth.BluetoothGattCharacteristic;

import info.izumin.android.bletia.core.BleErrorType;
import info.izumin.android.bletia.BletiaException;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/15/15.
 */
public class ReadCharacteristicAction extends CharacteristicAction {

    public ReadCharacteristicAction(BluetoothGattCharacteristic characteristic) {
        super(characteristic);
    }

    @Override
    public Type getType() {
        return Type.READ_CHARACTERISTIC;
    }

    @Override
    public boolean execute(BluetoothGattWrapper gattWrapper) {
        if (!gattWrapper.readCharacteristic(getCharacteristic())) {
            getDeferred().reject(new BletiaException(this, BleErrorType.OPERATION_INITIATED_FAILURE));
            return false;
        }
        return true;
    }
}
