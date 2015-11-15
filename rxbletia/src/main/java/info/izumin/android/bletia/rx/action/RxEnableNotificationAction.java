package info.izumin.android.bletia.rx.action;

import android.bluetooth.BluetoothGattCharacteristic;

import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.action.AbstractEnableNotificationAction;
import info.izumin.android.bletia.rx.RxObservableStrategy;
import rx.Observable;

/**
 * Created by izumin on 11/15/15.
 */
public class RxEnableNotificationAction extends AbstractEnableNotificationAction<Observable<BluetoothGattCharacteristic>> {
    public static final String TAG = RxEnableNotificationAction.class.getSimpleName();

    public RxEnableNotificationAction(BluetoothGattCharacteristic characteristic, boolean enabled) {
        super(characteristic, enabled, new RxObservableStrategy<BluetoothGattCharacteristic, BletiaException>());
    }
}
