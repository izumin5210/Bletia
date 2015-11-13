package info.izumin.android.bletia.core.action;

import android.bluetooth.BluetoothGattDescriptor;

import info.izumin.android.bletia.core.BleErrorType;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.ResolveStrategy;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/15/15.
 */
public abstract class AbstractReadDescriptorAction<R> extends AbstractDescriptorAction<R> {

    public AbstractReadDescriptorAction(BluetoothGattDescriptor descriptor,
                                        ResolveStrategy<BluetoothGattDescriptor, BletiaException, R> resolveStrategy) {
        super(descriptor, Type.READ_DESCRIPTOR, resolveStrategy);
    }

    @Override
    public boolean execute(BluetoothGattWrapper gattWrapper) {
        if (!gattWrapper.readDescriptor(getDescriptor())) {
            reject(new BletiaException(this, BleErrorType.OPERATION_INITIATED_FAILURE));
            return false;
        }
        return true;
    }
}
