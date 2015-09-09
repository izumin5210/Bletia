package info.izumin.android.bletia;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import org.jdeferred.Deferred;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import info.izumin.android.bletia.wrapper.BluetoothGattCallbackWrapper;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/7/15.
 */
public class BluetoothGattCallbackHandler extends BluetoothGattCallbackWrapper {

    private EnumMap<BleEvent.Type, List<BleEvent>> mEventMap;

    public BluetoothGattCallbackHandler() {
        mEventMap = new EnumMap<>(BleEvent.Type.class);
    }

    public boolean append(BleEvent.Type key, BleEvent event) {
        if (!mEventMap.containsKey(key)) {
            mEventMap.put(key, new ArrayList<BleEvent>());
        }
        return mEventMap.get(key).add(event);
    }

    @Override
    public void onConnectionStateChange(BluetoothGattWrapper gatt, int status, int newState) {
        // TODO: Not yet implemented.
    }

    @Override
    public void onServicesDiscovered(BluetoothGattWrapper gatt, int status) {
        // TODO: Not yet implemented.
    }

    @Override
    public void onCharacteristicRead(BluetoothGattWrapper gatt, BluetoothGattCharacteristic characteristic, int status) {
        handleBleEvent(BleEvent.Type.READING_CHARACTERISTIC, characteristic, status);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGattWrapper gatt, BluetoothGattCharacteristic characteristic, int status) {
        handleBleEvent(BleEvent.Type.WRITING_CHARACTERISTIC, characteristic, status);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGattWrapper gatt, BluetoothGattCharacteristic characteristic) {
        // TODO: Not yet implemented.
    }

    @Override
    public void onDescriptorRead(BluetoothGattWrapper gatt, BluetoothGattDescriptor descriptor, int status) {
        // TODO: Not yet implemented.
    }

    @Override
    public void onDescriptorWrite(BluetoothGattWrapper gatt, BluetoothGattDescriptor descriptor, int status) {
        // TODO: Not yet implemented.
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGattWrapper gatt, int status) {
        // TODO: Not yet implemented.
    }

    @Override
    public void onReadRemoteRssi(BluetoothGattWrapper gatt, int rssi, int status) {
        // TODO: Not yet implemented.
    }

    @Override
    public void onMtuChanged(BluetoothGattWrapper gatt, int mtu, int status) {
        // TODO: Not yet implemented.
    }

    private void handleBleEvent(BleEvent.Type type, BluetoothGattCharacteristic characteristic, int status) {
        List<BleEvent> events = mEventMap.get(type);
        for (int i = 0; i < events.size(); i++) {
            BleEvent event = events.get(i);
            if (event.getCharacteristic().getUuid().equals(characteristic.getUuid())) {
                Deferred<BluetoothGattCharacteristic, BluetoothGattStatus, Object> deferred = events.remove(i).getDeferred();
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    deferred.resolve(characteristic);
                } else {
                    deferred.reject(BluetoothGattStatus.valueOf(status));
                }
                break;
            }
        }
    }
}
