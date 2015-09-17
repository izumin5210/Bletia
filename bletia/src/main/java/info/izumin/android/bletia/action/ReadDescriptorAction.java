package info.izumin.android.bletia.action;

import android.bluetooth.BluetoothGattDescriptor;

import info.izumin.android.bletia.BleErrorType;
import info.izumin.android.bletia.BletiaException;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/15/15.
 */
public class ReadDescriptorAction extends DescriptorAction {

    public ReadDescriptorAction(BluetoothGattDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    public Type getType() {
        return Type.READ_DESCRIPTOR;
    }

    @Override
    public void execute(BluetoothGattWrapper gattWrapper) {
        if (!gattWrapper.readDescriptor(getDescriptor())) {
            getDeferred().reject(new BletiaException(BleErrorType.OPERATION_INITIATED_FAILURE, getDescriptor()));
        }
    }
}
