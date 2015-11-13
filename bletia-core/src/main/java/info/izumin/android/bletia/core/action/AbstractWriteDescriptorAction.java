package info.izumin.android.bletia.core.action;

import android.bluetooth.BluetoothGattDescriptor;

import info.izumin.android.bletia.core.BleErrorType;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.ActionResolver;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/15/15.
 */
public abstract class AbstractWriteDescriptorAction extends AbstractDescriptorAction {

    public AbstractWriteDescriptorAction(BluetoothGattDescriptor descriptor,
                                         ActionResolver<BluetoothGattDescriptor, BletiaException> actionResolver) {
        super(descriptor, Type.WRITE_DESCRIPTOR, actionResolver);
    }

    @Override
    public boolean execute(BluetoothGattWrapper gattWrapper) {
        if (!gattWrapper.writeDescriptor(getDescriptor())) {
            reject(new BletiaException(this, BleErrorType.OPERATION_INITIATED_FAILURE));
            return false;
        }
        return true;
    }
}
