package info.izumin.android.bletia.action;

import android.bluetooth.BluetoothGattDescriptor;

import org.jdeferred.Promise;

import info.izumin.android.bletia.DeferredStrategy;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.action.AbstractReadDescriptorAction;

/**
 * Created by izumin on 9/15/15.
 */
public class ReadDescriptorAction extends AbstractReadDescriptorAction<Promise<BluetoothGattDescriptor, BletiaException, Void>> {

    public ReadDescriptorAction(BluetoothGattDescriptor descriptor) {
        super(descriptor, new DeferredStrategy<BluetoothGattDescriptor, BletiaException>());
    }
}
