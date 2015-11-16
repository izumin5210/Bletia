package info.izumin.android.bletia.rx.action;

import android.bluetooth.BluetoothGattCharacteristic;

import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.action.AbstractReadCharacteristicAction;
import info.izumin.android.bletia.rx.RxObservableStrategy;
import rx.Observable;

/**
 * Created by izumin on 11/15/15.
 */
public class RxReadCharacteristicAction extends AbstractReadCharacteristicAction<Observable<BluetoothGattCharacteristic>> {
    public static final String TAG = RxReadCharacteristicAction.class.getSimpleName();

    public RxReadCharacteristicAction(BluetoothGattCharacteristic characteristic) {
        super(characteristic, new RxObservableStrategy<BluetoothGattCharacteristic, BletiaException>());
    }
}
