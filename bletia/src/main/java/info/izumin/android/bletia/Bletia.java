package info.izumin.android.bletia;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.HandlerThread;

import org.jdeferred.Promise;

import java.util.UUID;

import info.izumin.android.bletia.action.Action;
import info.izumin.android.bletia.action.EnableNotificationAction;
import info.izumin.android.bletia.action.ReadCharacteristicAction;
import info.izumin.android.bletia.action.ReadDescriptorAction;
import info.izumin.android.bletia.action.ReadRemoteRssiAction;
import info.izumin.android.bletia.action.WriteCharacteristicAction;
import info.izumin.android.bletia.action.WriteDescriptorAction;
import info.izumin.android.bletia.helper.ConnectionHelper;
import info.izumin.android.bletia.wrapper.BluetoothDeviceWrapper;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/7/15.
 */
public class Bletia implements BluetoothGattCallbackHandler.Callback {

    public static UUID CLIENT_CHARCTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private Context mContext;
    private BluetoothGattWrapper mGattWrapper;

    private BleState mState = BleState.DISCONNECTED;

    private ConnectionHelper mConnectionHelper;

    private EventEmitter mEmitter;
    private ActionQueueContainer mQueueContainer;
    private BluetoothGattCallbackHandler mCallbackHandler;
    private BleMessageThread mMessageThread;

    public Bletia(Context context) {
        mContext = context;
        mConnectionHelper = new ConnectionHelper(mContext);
        mEmitter = new EventEmitter(this);
        mQueueContainer = new ActionQueueContainer();
        mCallbackHandler = new BluetoothGattCallbackHandler(this, mQueueContainer);
    }

    public BleState getState() {
        return mState;
    }

    public void addListener(BletiaListener listener) {
        mEmitter.addListener(listener);
    }

    public void removeListener(BletiaListener listener) {
        mEmitter.removeListener(listener);
    }

    public void connect(BluetoothDevice device) {
        mState = BleState.CONNECTING;
        mGattWrapper = mConnectionHelper.connect(new BluetoothDeviceWrapper(device), mCallbackHandler);

        HandlerThread thread = new HandlerThread(device.getName());
        thread.start();
        mMessageThread = new BleMessageThread(thread, mGattWrapper, mQueueContainer);
    }

    public void disconenct() {
        mState = BleState.DISCONNECTING;
        mConnectionHelper.disconnect();
        mMessageThread.stop();
    }

    public boolean discoverServices() {
        mState = BleState.SERVICE_DISCOVERING;
        return mGattWrapper.discoverServices();
    }

    public BluetoothGattService getService(UUID uuid) {
        return mGattWrapper.getService(uuid);
    }

    public <T> Promise<T, BletiaException, Void> execute(Action<T, ?> action) {
        return mMessageThread.execute(action);
    }

    public Promise<BluetoothGattCharacteristic, BletiaException, Void> writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        return execute(new WriteCharacteristicAction(characteristic));
    }

    public Promise<BluetoothGattCharacteristic, BletiaException, Void> readCharacteristic(BluetoothGattCharacteristic characteristic) {
        return execute(new ReadCharacteristicAction(characteristic));
    }

    public Promise<BluetoothGattDescriptor, BletiaException, Void> writeDescriptor(BluetoothGattDescriptor descriptor) {
        return execute(new WriteDescriptorAction(descriptor));
    }

    public Promise<BluetoothGattDescriptor, BletiaException, Void> readDescriptor(BluetoothGattDescriptor descriptor) {
        return execute(new ReadDescriptorAction(descriptor));
    }

    public Promise<BluetoothGattCharacteristic, BletiaException, Void> enableNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        return execute(new EnableNotificationAction(characteristic, enabled));
    }

    public Promise<Integer, BletiaException, Void> readRemoteRssi() {
        return execute(new ReadRemoteRssiAction());
    }

    @Override
    public void onConnect(BluetoothGattWrapper gatt) {
        mState = BleState.CONNECTED;
        mEmitter.emitConnectEvent();
    }

    @Override
    public void onDisconnect(BluetoothGattWrapper gatt) {
        mState = BleState.DISCONNECTED;
        mConnectionHelper.close();
        mEmitter.emitDisconnectEvent();
    }

    @Override
    public void onServiceDiscovered(int status) {
        mState = BleState.SERVICE_DISCOVERED;
        mEmitter.emitServiceDiscovered(status);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
        mEmitter.emitCharacteristicChanged(characteristic);
    }

    @Override
    public void onError(BletiaException exception) {
        mEmitter.emitError(exception);
    }
}
