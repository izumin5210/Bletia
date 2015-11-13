package info.izumin.android.bletia.action;

import android.bluetooth.BluetoothGattCharacteristic;

import org.jdeferred.Promise;

import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.action.AbstractReadCharacteristicAction;

/**
 * Created by izumin on 9/15/15.
 */
public class ReadCharacteristicAction extends AbstractReadCharacteristicAction<Promise<BluetoothGattCharacteristic, BletiaException, Void>> {

    public ReadCharacteristicAction(BluetoothGattCharacteristic characteristic) {
        super(characteristic, new DeferredStrategy<BluetoothGattCharacteristic, BletiaException>());
    }
}
