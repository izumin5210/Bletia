package info.izumin.android.bletia.core.action;

import android.bluetooth.BluetoothGattDescriptor;

import info.izumin.android.bletia.core.BleErrorType;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.ResolveStrategy;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/15/15.
 */
public abstract class AbstractWriteDescriptorAction<R> extends AbstractDescriptorAction<R> {

    public AbstractWriteDescriptorAction(BluetoothGattDescriptor descriptor,
                                         ResolveStrategy<BluetoothGattDescriptor, BletiaException, R> resolveStrategy) {
        super(descriptor, Type.WRITE_DESCRIPTOR, resolveStrategy);
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
