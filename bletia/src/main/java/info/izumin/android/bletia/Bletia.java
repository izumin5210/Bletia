package info.izumin.android.bletia;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.HandlerThread;

import org.jdeferred.Promise;

import java.util.UUID;

import info.izumin.android.bletia.event.EnableNotificationEvent;
import info.izumin.android.bletia.event.Event;
import info.izumin.android.bletia.event.ReadCharacteristicEvent;
import info.izumin.android.bletia.event.ReadDescriptorEvent;
import info.izumin.android.bletia.event.ReadRemoteRssiEvent;
import info.izumin.android.bletia.event.WriteCharacteristicEvent;
import info.izumin.android.bletia.event.WriteDescriptorEvent;
import info.izumin.android.bletia.helper.ConnectionHelper;
import info.izumin.android.bletia.wrapper.BluetoothDeviceWrapper;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/7/15.
 */
public class Bletia implements BluetoothGattCallbackHandler.Callback {

    public static UUID BRETIA_UUID = UUID.fromString("00000000-0000-4e6c-abcf-960c2715e72d");
    public static UUID CLIENT_CHARCTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private Context mContext;
    private BluetoothGattWrapper mGattWrapper;

    private BleState mState = BleState.DISCONNECTED;

    private ConnectionHelper mConnectionHelper;

    private EventEmitter mEmitter;
    private BleEventStore mEventStore;
    private BluetoothGattCallbackHandler mCallbackHandler;
    private BleMessageThread mMessageThread;

    public Bletia(Context context) {
        mContext = context;
        mConnectionHelper = new ConnectionHelper(mContext);
        mEmitter = new EventEmitter(this);
        mEventStore = new BleEventStore();
        mCallbackHandler = new BluetoothGattCallbackHandler(this, mEventStore);
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
        mConnectionHelper.connect(new BluetoothDeviceWrapper(device), mCallbackHandler);

        HandlerThread thread = new HandlerThread(device.getName());
        thread.start();
        mMessageThread = new BleMessageThread(thread, mGattWrapper, mEventStore);
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

    public <T> Promise<T, BletiaException, Object> execute(Event<T> event) {
        return mMessageThread.execute(event);
    }

    public WriteCharacteristicEvent getWriteCharacteristicEvent(BluetoothGattCharacteristic characteristic) {
        return new WriteCharacteristicEvent(characteristic);
    }

    public Promise<BluetoothGattCharacteristic, BletiaException, Object> writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        return execute(getWriteCharacteristicEvent(characteristic));
    }

    public ReadCharacteristicEvent getReadCharacteristicEvent(BluetoothGattCharacteristic characteristic) {
        return new ReadCharacteristicEvent(characteristic);
    }

    public Promise<BluetoothGattCharacteristic, BletiaException, Object> readCharacteristic(BluetoothGattCharacteristic characteristic) {
        return execute(getReadCharacteristicEvent(characteristic));
    }

    public WriteDescriptorEvent getWriteDescriptorEvent(BluetoothGattDescriptor descriptor) {
        return new WriteDescriptorEvent(descriptor);
    }

    public Promise<BluetoothGattDescriptor, BletiaException, Object> writeDescriptor(BluetoothGattDescriptor descriptor) {
        return execute(getWriteDescriptorEvent(descriptor));
    }

    public ReadDescriptorEvent getReadDescriptorEvent(BluetoothGattDescriptor descriptor) {
        return new ReadDescriptorEvent(descriptor);
    }

    public Promise<BluetoothGattDescriptor, BletiaException, Object> readDescriptor(BluetoothGattDescriptor descriptor) {
        return execute(getReadDescriptorEvent(descriptor));
    }

    public EnableNotificationEvent getEnableNotificationEvent(BluetoothGattCharacteristic characteristic, boolean enabled) {
        return new EnableNotificationEvent(characteristic, enabled);
    }

    public Promise<BluetoothGattCharacteristic, BletiaException, Object> enableNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        return execute(getEnableNotificationEvent(characteristic, enabled));
    }

    public ReadRemoteRssiEvent getReadRemoteRssiEvent() {
        return new ReadRemoteRssiEvent();
    }

    public Promise<Integer, BletiaException, Object> readRemoteRssi() {
        return execute(getReadRemoteRssiEvent());
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
