package info.izumin.android.bletia.action;

import android.bluetooth.BluetoothGattDescriptor;
import android.os.Bundle;

import java.util.UUID;

/**
 * Created by izumin on 9/15/15.
 */
public abstract class DescriptorAction extends Action<BluetoothGattDescriptor> {

    private final BluetoothGattDescriptor mDescriptor;

    public DescriptorAction(BluetoothGattDescriptor descriptor) {
        super();
        mDescriptor = descriptor;
    }

    public BluetoothGattDescriptor getDescriptor() {
        return mDescriptor;
    }

    @Override
    public UUID getUuid() {
        return mDescriptor.getUuid();
    }

    @Override
    public Bundle getBundle() {
        Bundle bundle = super.getBundle();
        bundle.putSerializable(KEY_UUID, mDescriptor.getUuid());
        return bundle;
    }
}
