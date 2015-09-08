package info.izumin.android.bletia.wrapper;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

/**
 * Created by izumin on 9/8/15.
 */
public abstract class BluetoothGattCallbackWrapper extends BluetoothGattCallback {

    @Override
    public final void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        onConnectionStateChange(new BluetoothGattWrapper(gatt), status, newState);
    }

    @Override
    public final void onServicesDiscovered(BluetoothGatt gatt, int status) {
        onServicesDiscovered(new BluetoothGattWrapper(gatt), status);
    }

    @Override
    public final void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        onCharacteristicRead(new BluetoothGattWrapper(gatt), characteristic, status);
    }

    @Override
    public final void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        onCharacteristicWrite(new BluetoothGattWrapper(gatt), characteristic, status);
    }

    @Override
    public final void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        onCharacteristicChanged(new BluetoothGattWrapper(gatt), characteristic);
    }

    @Override
    public final void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        onDescriptorRead(new BluetoothGattWrapper(gatt), descriptor, status);
    }

    @Override
    public final void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        onDescriptorWrite(new BluetoothGattWrapper(gatt), descriptor, status);
    }

    @Override
    public final void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        onReliableWriteCompleted(new BluetoothGattWrapper(gatt), status);
    }

    @Override
    public final void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        onReadRemoteRssi(new BluetoothGattWrapper(gatt), rssi, status);
    }

    @Override
    public final void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        onMtuChanged(new BluetoothGattWrapper(gatt), mtu, status);
    }

    public void onConnectionStateChange(BluetoothGattWrapper gatt, int status, int newState) {

    }

    public void onServicesDiscovered(BluetoothGattWrapper gatt, int status) {

    }

    public void onCharacteristicRead(BluetoothGattWrapper gatt, BluetoothGattCharacteristic characteristic, int status) {

    }

    public void onCharacteristicWrite(BluetoothGattWrapper gatt, BluetoothGattCharacteristic characteristic, int status) {

    }

    public void onCharacteristicChanged(BluetoothGattWrapper gatt, BluetoothGattCharacteristic characteristic) {

    }

    public void onDescriptorRead(BluetoothGattWrapper gatt, BluetoothGattDescriptor descriptor, int status) {

    }

    public void onDescriptorWrite(BluetoothGattWrapper gatt, BluetoothGattDescriptor descriptor, int status) {

    }

    public void onReliableWriteCompleted(BluetoothGattWrapper gatt, int status) {

    }

    public void onReadRemoteRssi(BluetoothGattWrapper gatt, int rssi, int status) {

    }

    public void onMtuChanged(BluetoothGattWrapper gatt, int mtu, int status) {

    }
}
