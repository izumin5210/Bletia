package info.izumin.android.bletia;

import android.bluetooth.BluetoothGatt;

/**
 * Created by izumin on 9/9/15.
 */
public enum BluetoothGattStatus {
    FAILURE(BluetoothGatt.GATT_FAILURE),
    INSUFFICIENT_AUTHENTICATION(BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION),
    INSUFFICIENT_ENCRYPTION(BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION),
    INVALID_ATTRIBUTE_LENGTH(BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH),
    INVALID_OFFSET(BluetoothGatt.GATT_INVALID_OFFSET),
    READ_NOT_PERMITTED(BluetoothGatt.GATT_READ_NOT_PERMITTED),
    SUCCESS(BluetoothGatt.GATT_SUCCESS),
    WRITE_NOT_PERMITTED(BluetoothGatt.GATT_WRITE_NOT_PERMITTED),
    UNKNOWN(-1)
    ;

    private final int mCode;

    BluetoothGattStatus(int code) {
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }

    public static BluetoothGattStatus valueOf(int code) {
        for (BluetoothGattStatus status : values()) {
            if (code == status.getCode()) {
                return status;
            }
        }
        return UNKNOWN;
    }
}
