package info.izumin.android.bletia;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.HandlerThread;

import org.jdeferred.Promise;

import java.util.List;
import java.util.UUID;

import info.izumin.android.bletia.action.DeferredStrategy;
import info.izumin.android.bletia.action.EnableNotificationAction;
import info.izumin.android.bletia.action.ReadCharacteristicAction;
import info.izumin.android.bletia.action.ReadDescriptorAction;
import info.izumin.android.bletia.action.ReadRemoteRssiAction;
import info.izumin.android.bletia.action.WriteCharacteristicAction;
import info.izumin.android.bletia.action.WriteDescriptorAction;
import info.izumin.android.bletia.core.ActionQueueContainer;
import info.izumin.android.bletia.core.BleMessageThread;
import info.izumin.android.bletia.core.BleState;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.BluetoothGattCallbackHandler;
import info.izumin.android.bletia.core.action.AbstractAction;
import info.izumin.android.bletia.core.wrapper.BluetoothDeviceWrapper;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;
import info.izumin.android.bletia.core.helper.ConnectionHelper;

/**
 * Created by izumin on 9/7/15.
 */
public class Bletia implements BluetoothGattCallbackHandler.Callback {

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

    public boolean isConnected() {
        return (mState == BleState.CONNECTED) || (mState == BleState.SERVICE_DISCOVERING) || isReady();
    }

    public boolean isReady() {
        return mState == BleState.SERVICE_DISCOVERED;
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

    public List<BluetoothGattService> getServices() {
        return mGattWrapper.getServices();
    }

    public BluetoothDevice getDevice() {
        return (mGattWrapper == null) ? null : mGattWrapper.getDevice();
    }

    public <T, E extends Throwable> Promise<T, E, Void> execute(AbstractAction<T, E, ?> action) {
        mMessageThread.dispatchAction(action);
        return ((DeferredStrategy<T, E>) action.getResolveStrategy()).getDeferred().promise();
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
