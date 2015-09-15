package info.izumin.android.bletia;

import android.bluetooth.BluetoothGatt;

/**
 * Created by izumin on 9/10/15.
 */
public enum BleErrorType implements BletiaErrorType {
    // BluetoothGatt.GATT_CONNECTION_CONGESTED is added in API level 21(Lolipop)
    // CONNECTION_CONGESTED(BluetoothGatt.GATT_CONNECTION_CONGESTED),
    CONNECTION_CONGESTED(143),
    FAILURE(BluetoothGatt.GATT_FAILURE),
    INSUFFICIENT_AUTHENTICATION(BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION),
    INSUFFICIENT_ENCRYPTION(BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION),
    INVALID_ATTRIBUTE_LENGTH(BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH),
    INVALID_OFFSET(BluetoothGatt.GATT_INVALID_OFFSET),
    READ_NOT_PERMITTED(BluetoothGatt.GATT_READ_NOT_PERMITTED),
    REQUEST_NOT_SUPPORTED(BluetoothGatt.GATT_REQUEST_NOT_SUPPORTED),
    SUCCESS(BluetoothGatt.GATT_SUCCESS),
    WRITE_NOT_PERMITTED(BluetoothGatt.GATT_WRITE_NOT_PERMITTED),
    INVALID_PARAMETER(-1),
    OPERATION_INITIATED_FAILURE(-1),
    REQUEST_FAILURE(-1),
    UNKNOWN_ERROR(-1);

    private final int mCode;

    BleErrorType(int code) {
        mCode = code;
    }

    @Override
    public int getCode() {
        return mCode;
    }

    @Override
    public String getName() {
        return name();
    }

    public static BleErrorType valueOf(int code) {
        for (BleErrorType status : values()) {
            if (code == status.getCode()) {
                return status;
            }
        }
        return UNKNOWN_ERROR;
    }
}
