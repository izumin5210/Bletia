package info.izumin.android.bletia;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

/**
 * Created by izumin on 9/14/15.
 */
public class BletiaException extends Exception {

    private final BletiaErrorType mType;
    private BluetoothGattCharacteristic mCharacteristic;
    private BluetoothGattDescriptor mDescriptor;

    public BletiaException(BletiaErrorType type) {
        this(type.getName(), type);
    }

    public BletiaException(BletiaErrorType type, BluetoothGattCharacteristic characteristic) {
        this(type);
        mCharacteristic = characteristic;
    }

    public BletiaException(BletiaErrorType type, BluetoothGattDescriptor descriptor) {
        this(type);
        mDescriptor = descriptor;
    }

    public BletiaException(String detailMessage, BletiaErrorType type) {
        super(detailMessage);
        this.mType = type;
    }

    public BletiaErrorType getType() {
        return mType;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return mCharacteristic;
    }

    public BluetoothGattDescriptor getDescriptor() {
        return mDescriptor;
    }
}
