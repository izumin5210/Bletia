package info.izumin.android.bletia;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by izumin on 9/10/15.
 */
public interface BletiaListener {
    void onConnect(Bletia bletia);
    void onDisconnect(Bletia bletia);
    void onError(BletiaException exception);
    void onServicesDiscovered(Bletia bletia, int status);
    void onCharacteristicChanged(Bletia bletia, BluetoothGattCharacteristic characteristic);
}
