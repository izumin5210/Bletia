package info.izumin.android.bletia;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import info.izumin.android.bletia.util.NotificationUtils;
import info.izumin.android.bletia.wrapper.BluetoothGattCallbackWrapper;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/7/15.
 */
public class BluetoothGattCallbackHandler extends BluetoothGattCallbackWrapper {

    private Callback mCallback;
    private BleEventStore mEventStore;

    public BluetoothGattCallbackHandler(Callback callback, BleEventStore eventStore) {
        mCallback = callback;
        mEventStore = eventStore;
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
            mCallback.onError(new BletiaException(BleErrorType.valueOf(status)));
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGattWrapper gatt, int status) {
        mCallback.onServiceDiscovered(status);
    }

    @Override
    public void onCharacteristicRead(BluetoothGattWrapper gatt, BluetoothGattCharacteristic characteristic, int status) {
        handleBleEvent(BleEvent.Type.READ_CHARACTERISTIC, characteristic, status);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGattWrapper gatt, BluetoothGattCharacteristic characteristic, int status) {
        handleBleEvent(BleEvent.Type.WRITE_CHARACTERISTIC, characteristic, status);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGattWrapper gatt, BluetoothGattCharacteristic characteristic) {
        mCallback.onCharacteristicChanged(characteristic);
    }

    @Override
    public void onDescriptorRead(BluetoothGattWrapper gatt, BluetoothGattDescriptor descriptor, int status) {
        handleBleEvent(BleEvent.Type.READ_DESCRIPTOR, descriptor, status);
    }

    @Override
    public void onDescriptorWrite(BluetoothGattWrapper gatt, BluetoothGattDescriptor descriptor, int status) {
        if (NotificationUtils.isEnableNotificationDescriptor(descriptor)) {
            handleBleEvent(BleEvent.Type.ENABLE_NOTIFICATION, descriptor.getCharacteristic(), descriptor, status);
        } else if (NotificationUtils.isDisableNotificationDescriptor(descriptor)) {
            handleBleEvent(BleEvent.Type.DISABLE_NOTIFICATION, descriptor.getCharacteristic(), descriptor, status);
        } else {
            handleBleEvent(BleEvent.Type.WRITE_DESCRIPTOR, descriptor, status);
        }
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

    private void handleBleEvent(BleEvent event, BluetoothGattCharacteristic characteristic, BluetoothGattDescriptor descriptor, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            event.getDeferred().resolve(event.getValue());
        } else {
            event.getDeferred().reject(new BletiaException(BleErrorType.valueOf(status), characteristic, descriptor));
        }
    }

    private void handleBleEvent(BleEvent.Type type, BluetoothGattCharacteristic characteristic, BluetoothGattDescriptor descriptor, int status) {
        BleEvent<BluetoothGattCharacteristic> event = mEventStore.closeEvent(type, characteristic.getUuid());
        handleBleEvent(event, characteristic, descriptor, status);
    }

    private void handleBleEvent(BleEvent.Type type, BluetoothGattCharacteristic characteristic, int status) {
        BleEvent<BluetoothGattCharacteristic> event = mEventStore.closeEvent(type, characteristic.getUuid());
        handleBleEvent(event, characteristic, null, status);
    }

    private void handleBleEvent(BleEvent.Type type, BluetoothGattDescriptor descriptor, int status) {
        BleEvent<BluetoothGattDescriptor> event = mEventStore.closeEvent(type, descriptor.getUuid());
        handleBleEvent(event, null, descriptor, status);
    }

    interface Callback {
        void onConnect(BluetoothGattWrapper gatt);
        void onDisconnect(BluetoothGattWrapper gatt);
        void onServiceDiscovered(int status);
        void onCharacteristicChanged(BluetoothGattCharacteristic characteristic);
        void onError(BletiaException exception);
    }
}
