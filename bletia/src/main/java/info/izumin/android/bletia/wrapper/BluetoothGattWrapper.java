package info.izumin.android.bletia.wrapper;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import java.util.List;
import java.util.UUID;

/**
 * Created by izumin on 9/8/15.
 */
public class BluetoothGattWrapper {
    private final BluetoothGatt mBluetoothGatt;

    public BluetoothGattWrapper(BluetoothGatt bluetoothGatt) {
        mBluetoothGatt = bluetoothGatt;
    }

    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        return mBluetoothGatt.getDevicesMatchingConnectionStates(states);
    }

    public List<BluetoothDevice> getConnectedDevices() {
        return mBluetoothGatt.getConnectedDevices();
    }

    public int getConnectionState(BluetoothDevice device) {
        return mBluetoothGatt.getConnectionState(device);
    }

    public boolean requestConnectionPriority(int connectionPriority) {
        return mBluetoothGatt.requestConnectionPriority(connectionPriority);
    }

    public boolean requestMtu(int mtu) {
        return mBluetoothGatt.requestMtu(mtu);
    }

    public boolean readRemoteRssi() {
        return mBluetoothGatt.readRemoteRssi();
    }

    public boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enable) {
        return mBluetoothGatt.setCharacteristicNotification(characteristic, enable);
    }

    @Deprecated
    public void abortReliableWrite(BluetoothDevice mDevice) {
        mBluetoothGatt.abortReliableWrite(mDevice);
    }

    public void abortReliableWrite() {
        mBluetoothGatt.abortReliableWrite();
    }

    public boolean executeReliableWrite() {
        return mBluetoothGatt.executeReliableWrite();
    }

    public boolean beginReliableWrite() {
        return mBluetoothGatt.beginReliableWrite();
    }

    public boolean writeDescriptor(BluetoothGattDescriptor descriptor) {
        return mBluetoothGatt.writeDescriptor(descriptor);
    }

    public boolean readDescriptor(BluetoothGattDescriptor descriptor) {
        return mBluetoothGatt.readDescriptor(descriptor);
    }

    public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        return mBluetoothGatt.writeCharacteristic(characteristic);
    }

    public boolean readCharacteristic(BluetoothGattCharacteristic characteristic) {
        return mBluetoothGatt.readCharacteristic(characteristic);
    }

    public BluetoothGattService getService(UUID uuid) {
        return mBluetoothGatt.getService(uuid);
    }

    public List<BluetoothGattService> getServices() {
        return mBluetoothGatt.getServices();
    }

    public boolean discoverServices() {
        return mBluetoothGatt.discoverServices();
    }

    public BluetoothDevice getDevice() {
        return mBluetoothGatt.getDevice();
    }

    public boolean connect() {
        return mBluetoothGatt.connect();
    }

    public void disconnect() {
        mBluetoothGatt.disconnect();
    }

    public void close() {
        mBluetoothGatt.close();
    }
}
