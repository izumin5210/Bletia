package info.izumin.android.bletia.action;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

import info.izumin.android.bletia.BleErrorType;
import info.izumin.android.bletia.BletiaException;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/15/15.
 */
public class WriteCharacteristicAction extends CharacteristicAction {

    public WriteCharacteristicAction(BluetoothGattService service, UUID uuid) {
        super(service, uuid);
    }

    public WriteCharacteristicAction(BluetoothGattCharacteristic characteristic) {
        super(characteristic);
    }

    @Override
    public Type getType() {
        return Type.WRITE_CHARACTERISTIC;
    }

    @Override
    public boolean execute(BluetoothGattWrapper gattWrapper) {
        if (!gattWrapper.writeCharacteristic(getCharacteristic())) {
            getDeferred().reject(new BletiaException(this, BleErrorType.OPERATION_INITIATED_FAILURE));
            return false;
        }
        return true;
    }
}
