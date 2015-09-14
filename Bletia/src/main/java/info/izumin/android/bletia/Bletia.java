package info.izumin.android.bletia;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.HandlerThread;

import org.jdeferred.Promise;

import java.util.UUID;

import info.izumin.android.bletia.helper.ConnectionHelper;
import info.izumin.android.bletia.wrapper.BluetoothDeviceWrapper;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/7/15.
 */
public class Bletia implements BluetoothGattCallbackHandler.Callback {

    public static UUID BRETIA_UUID = UUID.fromString("00000000-0000-4e6c-abcf-960c2715e72d");

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
        mEmitter = new EventEmitter();
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

    public Promise<BluetoothGattCharacteristic, BletiaException, Object> writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        BleEvent<BluetoothGattCharacteristic> event =
                new BleEvent<>(BleEvent.Type.WRITE_CHARACTERISTIC, characteristic.getUuid(), characteristic);

        return mMessageThread.sendEvent(event);
    }

    public Promise<BluetoothGattCharacteristic, BletiaException, Object> readCharacteristic(BluetoothGattCharacteristic characteristic) {
        BleEvent<BluetoothGattCharacteristic> event =
                new BleEvent<>(BleEvent.Type.READ_CHARACTERISTIC, characteristic.getUuid(), characteristic);

        return mMessageThread.sendEvent(event);
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
    public void onError(BletiaException exception) {
        mEmitter.emitError(exception);
    }
}
