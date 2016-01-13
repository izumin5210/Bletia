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
public class ReadCharacteristicAction extends CharacteristicAction {

    public ReadCharacteristicAction(BluetoothGattService service, UUID uuid) {
        super(service, uuid);
    }

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
