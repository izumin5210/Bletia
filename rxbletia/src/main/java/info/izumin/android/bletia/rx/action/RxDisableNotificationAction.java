package info.izumin.android.bletia.rx.action;

import android.bluetooth.BluetoothGattCharacteristic;

import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.action.AbstractEnableNotificationAction;
import info.izumin.android.bletia.rx.RxObservableStrategy;
import rx.Observable;

/**
 * Created by izumin on 11/15/15.
 */
public class RxDisableNotificationAction extends AbstractEnableNotificationAction<Observable<BluetoothGattCharacteristic>> {
    public static final String TAG = RxDisableNotificationAction.class.getSimpleName();

    public RxDisableNotificationAction(BluetoothGattCharacteristic characteristic) {
        super(characteristic, false, new RxObservableStrategy<BluetoothGattCharacteristic, BletiaException>());
    }
}
