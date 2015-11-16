package info.izumin.android.bletia.rx.action;

import android.bluetooth.BluetoothGattCharacteristic;

import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.action.AbstractWriteCharacteristicAction;
import info.izumin.android.bletia.rx.RxObservableStrategy;
import rx.Observable;

/**
 * Created by izumin on 11/15/15.
 */
public class RxWriteCharacteristicAction extends AbstractWriteCharacteristicAction<Observable<BluetoothGattCharacteristic>> {
    public static final String TAG = RxWriteCharacteristicAction.class.getSimpleName();

    public RxWriteCharacteristicAction(BluetoothGattCharacteristic characteristic) {
        super(characteristic, new RxObservableStrategy<BluetoothGattCharacteristic, BletiaException>());
    }
}
