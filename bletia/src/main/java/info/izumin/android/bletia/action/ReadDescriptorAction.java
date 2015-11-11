package info.izumin.android.bletia.action;

import android.bluetooth.BluetoothGattDescriptor;

import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.action.AbstractReadDescriptorAction;

/**
 * Created by izumin on 9/15/15.
 */
public class ReadDescriptorAction extends AbstractReadDescriptorAction {

    public ReadDescriptorAction(BluetoothGattDescriptor descriptor) {
        super(descriptor, new DeferredStrategy<BluetoothGattDescriptor, BletiaException>());
    }
}
