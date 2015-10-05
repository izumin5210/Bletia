package info.izumin.android.bletia;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import info.izumin.android.bletia.action.Action;
import info.izumin.android.bletia.util.NotificationUtils;
import info.izumin.android.bletia.wrapper.BluetoothGattCallbackWrapper;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/7/15.
 */
class BluetoothGattCallbackHandler extends BluetoothGattCallbackWrapper {

    private Callback mCallback;
    private ActionQueueContainer mQueueContainer;

    public BluetoothGattCallbackHandler(Callback callback, ActionQueueContainer queueContainer) {
        mCallback = callback;
        mQueueContainer = queueContainer;
    }

    @Override
    public void onConnectionStateChange(BluetoothGattWrapper gatt, int status, int newState) {
        if (newState == BluetoothGatt.STATE_DISCONNECTED) {
            mCallback.onDisconnect(gatt);
        } else if (status == BluetoothGatt.GATT_SUCCESS) {
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                mCallback.onConnect(gatt);
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
        handleAction(mQueueContainer.getReadCharacteristicActionQueue(), characteristic, characteristic.getUuid(), status);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGattWrapper gatt, BluetoothGattCharacteristic characteristic, int status) {
        handleAction(mQueueContainer.getWriteCharacteristicActionQueue(), characteristic, characteristic.getUuid(), status);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGattWrapper gatt, BluetoothGattCharacteristic characteristic) {
        mCallback.onCharacteristicChanged(characteristic);
    }

    @Override
    public void onDescriptorRead(BluetoothGattWrapper gatt, BluetoothGattDescriptor descriptor, int status) {
        handleAction(mQueueContainer.getReadDescriptorActionQueue(), descriptor, descriptor.getUuid(), status);
    }

    @Override
    public void onDescriptorWrite(BluetoothGattWrapper gatt, BluetoothGattDescriptor descriptor, int status) {
        if (NotificationUtils.isEnableNotificationDescriptor(descriptor)) {
            BluetoothGattCharacteristic characteristic = descriptor.getCharacteristic();
            handleAction(mQueueContainer.getEnableNotificationActionQueue(), characteristic, characteristic.getUuid(), status);
        } else {
            handleAction(mQueueContainer.getWriteDescriptorActionQueue(), descriptor, descriptor.getUuid(), status);
        }
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGattWrapper gatt, int status) {
        // TODO: Not yet implemented.
    }

    @Override
    public void onReadRemoteRssi(BluetoothGattWrapper gatt, int rssi, int status) {
        handleAction(mQueueContainer.getReadRemoteRssiActionQueue(), rssi, null, status);
    }

    @Override
    public void onMtuChanged(BluetoothGattWrapper gatt, int mtu, int status) {
        // TODO: Not yet implemented.
    }

    private <A extends Action<T, I>, T, I> void handleAction(ActionQueue<A, I> queue, T result, I identity, int status) {
        A action = queue.dequeue(identity);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            action.getDeferred().resolve(result);
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
