package info.izumin.android.bletia.core.action;

import android.bluetooth.BluetoothGattDescriptor;

import java.util.UUID;

import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.ResolveStrategy;

/**
 * Created by izumin on 9/15/15.
 */
public abstract class AbstractDescriptorAction extends AbstractAction<BluetoothGattDescriptor, BletiaException, UUID> {

    private final BluetoothGattDescriptor mDescriptor;

    public AbstractDescriptorAction(BluetoothGattDescriptor descriptor, AbstractAction.Type type,
                                    ResolveStrategy<BluetoothGattDescriptor, BletiaException> resolveStrategy) {
        super(descriptor.getUuid(), type, resolveStrategy);
        mDescriptor = descriptor;
    }

    public BluetoothGattDescriptor getDescriptor() {
        return mDescriptor;
    }
}
