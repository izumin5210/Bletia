package info.izumin.android.bletia.event;

import android.bluetooth.BluetoothGattCharacteristic;

import info.izumin.android.bletia.BleErrorType;
import info.izumin.android.bletia.BletiaException;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/15/15.
 */
public class WriteCharacteristicEvent extends CharacteristicEvent {

    public WriteCharacteristicEvent(BluetoothGattCharacteristic characteristic) {
        super(characteristic);
    }

    @Override
    public Type getType() {
        return Type.WRITE_CHARACTERISTIC;
    }

    @Override
    public void handle(BluetoothGattWrapper gattWrapper) {
        if (!gattWrapper.writeCharacteristic(getCharacteristic())) {
            getDeferred().reject(new BletiaException(BleErrorType.OPERATION_INITIATED_FAILURE, getCharacteristic()));
        }
    }
}
