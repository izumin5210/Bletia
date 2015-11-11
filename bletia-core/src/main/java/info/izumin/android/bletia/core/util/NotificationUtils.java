package info.izumin.android.bletia.core.util;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by izumin on 9/15/15.
 */
public final class NotificationUtils {
    private NotificationUtils() {
        throw new AssertionError("constructor of the utility class should not be called");
    }

    public final static UUID DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public final static int DESCRIPTOR_PERMISSION = BluetoothGattDescriptor.PERMISSION_WRITE;

    public static BluetoothGattDescriptor getDescriptor(BluetoothGattCharacteristic characteristic, UUID descriptorUuid, boolean enabled) {
        byte[] value = enabled ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(descriptorUuid);
        if (descriptor == null) {
            descriptor = new BluetoothGattDescriptor(DESCRIPTOR_UUID, DESCRIPTOR_PERMISSION);
            characteristic.addDescriptor(descriptor);
        }
        descriptor.setValue(value);
        return descriptor;
    }

    public static BluetoothGattDescriptor getDescriptor(BluetoothGattCharacteristic characteristic, boolean enabled) {
        return getDescriptor(characteristic, DESCRIPTOR_UUID, enabled);
    }

    public static boolean isEnabledNotificationDescriptor(BluetoothGattDescriptor descriptor, UUID descriptorUuid) {
        return descriptor.getUuid().equals(descriptorUuid)
                && (Arrays.equals(descriptor.getValue(), BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                || Arrays.equals(descriptor.getValue(), BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE));
    }

    public static boolean isEnabledNotificationDescriptor(BluetoothGattDescriptor descriptor) {
        return isEnabledNotificationDescriptor(descriptor, DESCRIPTOR_UUID);
    }
}