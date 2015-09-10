package info.izumin.android.bletia;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import org.jdeferred.Deferred;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

import info.izumin.android.bletia.helper.ConnectionHelper;
import info.izumin.android.bletia.wrapper.BluetoothDeviceWrapper;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/7/15.
 */
public class Bletia implements BluetoothGattCallbackHandler.Callback {

    private Context mContext;
    private BluetoothGattWrapper mGattWrapper;

    private BleState mState = BleState.DISCONNECTED;

    private ConnectionHelper mConnectionHelper;

    private EventEmitter mEmitter;
    private BluetoothGattCallbackHandler mCallbackHandler;

    public Bletia(Context context) {
        mContext = context;
        mConnectionHelper = new ConnectionHelper(mContext);
        mCallbackHandler = new BluetoothGattCallbackHandler(this);
        mEmitter = new EventEmitter();
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
    }

    public void disconenct() {
        mState = BleState.DISCONNECTING;
        mConnectionHelper.disconnect();
    }

    public Promise<BluetoothGattCharacteristic, BluetoothGattStatus, Object> writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        Deferred<BluetoothGattCharacteristic, BluetoothGattStatus, Object> deferred = new DeferredObject<>();
        Promise<BluetoothGattCharacteristic, BluetoothGattStatus, Object> promise = deferred.promise();
        BleEvent event = new BleEvent(deferred);
        event.setCharacteristic(characteristic);

        mGattWrapper.writeCharacteristic(characteristic);
        mCallbackHandler.append(BleEvent.Type.WRITING_CHARACTERISTIC, event);

        return promise;
    }

    public Promise<BluetoothGattCharacteristic, BluetoothGattStatus, Object> readCharacteristic(BluetoothGattCharacteristic characteristic) {
        Deferred<BluetoothGattCharacteristic, BluetoothGattStatus, Object> deferred = new DeferredObject<>();
        Promise<BluetoothGattCharacteristic, BluetoothGattStatus, Object> promise = deferred.promise();
        BleEvent event = new BleEvent(deferred);
        event.setCharacteristic(characteristic);

        mGattWrapper.readCharacteristic(characteristic);
        mCallbackHandler.append(BleEvent.Type.READING_CHARACTERISTIC, event);

        return promise;
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
    public void onError(BleStatus status) {
        mEmitter.emitError(status);
    }
}
