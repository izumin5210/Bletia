package info.izumin.android.bletia.action;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Bundle;

import java.util.UUID;

/**
 * Created by izumin on 9/15/15.
 */
public abstract class DescriptorAction extends Action<BluetoothGattDescriptor, UUID> {

    private final BluetoothGattDescriptor mDescriptor;

    public DescriptorAction(BluetoothGattCharacteristic characteristic, UUID uuid) {
        super(uuid);
        mDescriptor = characteristic.getDescriptor(uuid);
    }

    public DescriptorAction(BluetoothGattDescriptor descriptor) {
        super(descriptor.getUuid());
        mDescriptor = descriptor;
    }

    public BluetoothGattDescriptor getDescriptor() {
        return mDescriptor;
    }

    @Override
    public Bundle getBundle() {
        Bundle bundle = super.getBundle();
        bundle.putSerializable(KEY_UUID, mDescriptor.getUuid());
        return bundle;
    }
}
