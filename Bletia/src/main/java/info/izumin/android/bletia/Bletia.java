package info.izumin.android.bletia;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import org.jdeferred.Deferred;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/7/15.
 */
public class Bletia {

    private Context mContext;
    private BluetoothGattWrapper mGattWrapper;
    private BluetoothGattCallbackHandler mCallbackHandler;

    public Bletia(Context context) {
        mContext = context;
        mCallbackHandler = new BluetoothGattCallbackHandler();
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
}
