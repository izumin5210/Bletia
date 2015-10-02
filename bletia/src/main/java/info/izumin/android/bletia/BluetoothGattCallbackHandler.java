package info.izumin.android.bletia;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import info.izumin.android.bletia.action.Action;
import info.izumin.android.bletia.action.CharacteristicAction;
import info.izumin.android.bletia.action.DescriptorAction;
import info.izumin.android.bletia.action.EnableNotificationAction;
import info.izumin.android.bletia.action.ReadRemoteRssiAction;
import info.izumin.android.bletia.util.NotificationUtils;
import info.izumin.android.bletia.wrapper.BluetoothGattCallbackWrapper;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/7/15.
 */
public class BluetoothGattCallbackHandler extends BluetoothGattCallbackWrapper {

    private Callback mCallback;
    private BleActionStore mActionStore;

    public BluetoothGattCallbackHandler(Callback callback, BleActionStore actionStore) {
        mCallback = callback;
        mActionStore = actionStore;
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
        handleBleAction(Action.Type.READ_CHARACTERISTIC, characteristic, status);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGattWrapper gatt, BluetoothGattCharacteristic characteristic, int status) {
        handleBleAction(Action.Type.WRITE_CHARACTERISTIC, characteristic, status);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGattWrapper gatt, BluetoothGattCharacteristic characteristic) {
        mCallback.onCharacteristicChanged(characteristic);
    }

    @Override
    public void onDescriptorRead(BluetoothGattWrapper gatt, BluetoothGattDescriptor descriptor, int status) {
        handleBleAction(Action.Type.READ_DESCRIPTOR, descriptor, status);
    }

    @Override
    public void onDescriptorWrite(BluetoothGattWrapper gatt, BluetoothGattDescriptor descriptor, int status) {
        if (NotificationUtils.isEnableNotificationDescriptor(descriptor)) {
            handleBleAction(Action.Type.ENABLE_NOTIFICATION, descriptor.getCharacteristic(), descriptor, status);
        } else {
            handleBleAction(Action.Type.WRITE_DESCRIPTOR, descriptor, status);
        }
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGattWrapper gatt, int status) {
        // TODO: Not yet implemented.
    }

    @Override
    public void onReadRemoteRssi(BluetoothGattWrapper gatt, int rssi, int status) {
        ReadRemoteRssiAction action = (ReadRemoteRssiAction) mActionStore.dequeue(Action.Type.READ_REMOTE_RSSI);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            action.getDeferred().resolve(rssi);
        } else {
            action.getDeferred().reject(new BletiaException(BleErrorType.valueOf(status)));
        }
    }

    @Override
    public void onMtuChanged(BluetoothGattWrapper gatt, int mtu, int status) {
        // TODO: Not yet implemented.
    }

    private void handleBleAction(Action.Type type, BluetoothGattCharacteristic characteristic, BluetoothGattDescriptor descriptor, int status) {
        EnableNotificationAction action = (EnableNotificationAction) mActionStore.dequeue(type);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            action.getDeferred().resolve(characteristic);
        } else {
            action.getDeferred().reject(new BletiaException(action, BleErrorType.valueOf(status)));
        }
    }

    private void handleBleAction(Action.Type type, BluetoothGattCharacteristic characteristic, int status) {
        CharacteristicAction action = (CharacteristicAction) mActionStore.dequeue(type);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            action.getDeferred().resolve(characteristic);
        } else {
            action.getDeferred().reject(new BletiaException(action, BleErrorType.valueOf(status)));
        }
    }

    private void handleBleAction(Action.Type type, BluetoothGattDescriptor descriptor, int status) {
        DescriptorAction action = (DescriptorAction) mActionStore.dequeue(type);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            action.getDeferred().resolve(descriptor);
        } else {
            action.getDeferred().reject(new BletiaException(action, BleErrorType.valueOf(status)));
        }
    }

    interface Callback {
        void onConnect(BluetoothGattWrapper gatt);
        void onDisconnect(BluetoothGattWrapper gatt);
        void onServiceDiscovered(int status);
        void onCharacteristicChanged(BluetoothGattCharacteristic characteristic);
        void onError(BletiaException exception);
    }
}
