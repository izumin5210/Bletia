package info.izumin.android.bletia;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import info.izumin.android.bletia.wrapper.BluetoothGattCallbackWrapper;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/7/15.
 */
public class BluetoothGattCallbackHandler extends BluetoothGattCallbackWrapper {

    private EnumMap<BleEvent.Type, Map<UUID, BleEvent>> mEventMap;
    private Callback mCallback;

    public BluetoothGattCallbackHandler(Callback callback) {
        mEventMap = new EnumMap<>(BleEvent.Type.class);
        mCallback = callback;
    }

    public BleEvent append(BleEvent.Type key, BleEvent event) {
        if (!mEventMap.containsKey(key)) {
            mEventMap.put(key, new HashMap<UUID, BleEvent>());
        }
        return mEventMap.get(key).put(event.getUuid(), event);
    }

    @Override
    public void onConnectionStateChange(BluetoothGattWrapper gatt, int status, int newState) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                mCallback.onConnect(gatt);
            }
            if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                mCallback.onDisconnect(gatt);
            }
        } else {
            mCallback.onError(BleStatus.valueOf(status));
        }
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

    public UUID generateUuid(BleEvent.Type type) {
        UUID uuid;
        final Map<UUID, BleEvent> map = mEventMap.get(type);
        final Set<UUID> usedUuids = (map != null) ? map.keySet() : new HashSet<UUID>();
        do {
            uuid = UUID.randomUUID();
        } while (usedUuids.contains(uuid));
        return uuid;
    }

    private void handleBleEvent(BleEvent.Type type, BluetoothGattCharacteristic characteristic, int status) {
        UUID uuid = UUID.fromString(new String(characteristic.getDescriptor(Bletia.BRETIA_UUID).getValue()));
        BleEvent event = mEventMap.get(type).get(uuid);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            event.getDeferred().resolve(characteristic);
        } else {
            event.getDeferred().reject(BluetoothGattStatus.valueOf(status));
        }
    }

    interface Callback {
        void onConnect(BluetoothGattWrapper gatt);
        void onDisconnect(BluetoothGattWrapper gatt);
        void onError(BleStatus status);
    }
}
