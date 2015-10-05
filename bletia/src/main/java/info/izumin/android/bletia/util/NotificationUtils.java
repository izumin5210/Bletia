package info.izumin.android.bletia.util;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import java.util.Arrays;
import java.util.UUID;

import info.izumin.android.bletia.Bletia;

/**
 * Created by izumin on 9/15/15.
 */
public final class NotificationUtils {
    private NotificationUtils() {
        throw new AssertionError("constructor of the utility class should not be called");
    }

    public final static UUID DESCRIPTOR_UUID = Bletia.CLIENT_CHARCTERISTIC_CONFIG;
    public final static int DESCRIPTOR_PERMISSION = BluetoothGattDescriptor.PERMISSION_WRITE;

    public static BluetoothGattDescriptor getDescriptor(BluetoothGattCharacteristic characteristic, boolean enabled) {
        byte[] value = enabled ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(Bletia.CLIENT_CHARCTERISTIC_CONFIG);
        if (descriptor == null) {
            descriptor = new BluetoothGattDescriptor(DESCRIPTOR_UUID, DESCRIPTOR_PERMISSION);
            characteristic.addDescriptor(descriptor);
        }
        descriptor.setValue(value);
        return descriptor;
    }

    public static boolean isEnableNotificationDescriptor(BluetoothGattDescriptor descriptor) {
        return descriptor.getUuid().equals(Bletia.CLIENT_CHARCTERISTIC_CONFIG)
                && (Arrays.equals(descriptor.getValue(), BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                || Arrays.equals(descriptor.getValue(), BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE));
    }
}