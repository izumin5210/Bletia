package info.izumin.android.bletia;

import android.bluetooth.BluetoothGattCharacteristic;

import info.izumin.android.bletia.core.BleState;
import info.izumin.android.bletia.core.BletiaException;

/**
 * Created by izumin on 9/10/15.
 */
public interface BletiaListener {
    void onError(BletiaException exception, BleState state);
    void onCharacteristicChanged(Bletia bletia, BluetoothGattCharacteristic characteristic);
}
