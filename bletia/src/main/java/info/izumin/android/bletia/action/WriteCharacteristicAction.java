package info.izumin.android.bletia.action;

import android.bluetooth.BluetoothGattCharacteristic;

import org.jdeferred.Promise;

import info.izumin.android.bletia.DeferredStrategy;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.action.AbstractWriteCharacteristicAction;

/**
 * Created by izumin on 9/15/15.
 */
public class WriteCharacteristicAction extends AbstractWriteCharacteristicAction<Promise<BluetoothGattCharacteristic, BletiaException, Void>> {

    public WriteCharacteristicAction(BluetoothGattCharacteristic characteristic) {
        super(characteristic, new DeferredStrategy<BluetoothGattCharacteristic, BletiaException>());
    }
}
