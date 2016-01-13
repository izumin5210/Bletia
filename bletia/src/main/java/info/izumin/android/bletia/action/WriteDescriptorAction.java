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
public class WriteDescriptorAction extends DescriptorAction {

    public WriteDescriptorAction(BluetoothGattCharacteristic characteristic, UUID uuid) {
        super(characteristic, uuid);
    }

    public WriteDescriptorAction(BluetoothGattDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    public Type getType() {
        return Type.WRITE_DESCRIPTOR;
    }

    @Override
    public boolean execute(BluetoothGattWrapper gattWrapper) {
        if (!gattWrapper.writeDescriptor(getDescriptor())) {
            getDeferred().reject(new BletiaException(this, BleErrorType.OPERATION_INITIATED_FAILURE));
            return false;
        }
        return true;
    }
}
