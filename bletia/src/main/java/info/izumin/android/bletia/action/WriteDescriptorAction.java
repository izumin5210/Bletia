package info.izumin.android.bletia.action;

import android.bluetooth.BluetoothGattDescriptor;

import org.jdeferred.Promise;

import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.action.AbstractWriteDescriptorAction;

/**
 * Created by izumin on 9/15/15.
 */
public class WriteDescriptorAction extends AbstractWriteDescriptorAction<Promise<BluetoothGattDescriptor, BletiaException, Void>> {

    public WriteDescriptorAction(BluetoothGattDescriptor descriptor) {
        super(descriptor, new DeferredStrategy<BluetoothGattDescriptor, BletiaException>());
    }
}
