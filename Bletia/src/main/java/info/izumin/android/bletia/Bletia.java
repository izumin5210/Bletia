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

    public Promise<BluetoothGattCharacteristic, Object, Object> writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        Deferred<BluetoothGattCharacteristic, Object, Object> deferred = new DeferredObject<>();
        Promise<BluetoothGattCharacteristic, Object, Object> promise = deferred.promise();
        BleEvent event = new BleEvent(deferred);

        if (mGattWrapper.writeCharacteristic(characteristic)) {
            event.setCharacteristic(characteristic);
            mCallbackHandler.append(BleEvent.Type.WRITING_CHARACTERISTIC, event);
        } else {
            // TODO: Not yet implemented.
        }

        return promise;
    }
}
