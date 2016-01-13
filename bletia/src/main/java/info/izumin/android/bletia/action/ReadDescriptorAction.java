package info.izumin.android.bletia.action;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import java.util.UUID;

import info.izumin.android.bletia.BleErrorType;
import info.izumin.android.bletia.BletiaException;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/15/15.
 */
public class ReadDescriptorAction extends DescriptorAction {

    public ReadDescriptorAction(BluetoothGattCharacteristic characteristic, UUID uuid) {
        super(characteristic, uuid);
    }

    public ReadDescriptorAction(BluetoothGattDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    public Type getType() {
        return Type.READ_DESCRIPTOR;
    }

    @Override
    public boolean execute(BluetoothGattWrapper gattWrapper) {
        if (!gattWrapper.readDescriptor(getDescriptor())) {
            getDeferred().reject(new BletiaException(this, BleErrorType.OPERATION_INITIATED_FAILURE));
            return false;
        }
        return true;
    }
}
