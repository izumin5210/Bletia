package info.izumin.android.bletia.rx.action;

import android.bluetooth.BluetoothGattDescriptor;

import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.action.AbstractReadDescriptorAction;
import info.izumin.android.bletia.rx.RxObservableStrategy;
import rx.Observable;

/**
 * Created by izumin on 11/15/15.
 */
public class RxReadDescriptorAction extends AbstractReadDescriptorAction<Observable<BluetoothGattDescriptor>> {
    public static final String TAG = RxReadDescriptorAction.class.getSimpleName();

    public RxReadDescriptorAction(BluetoothGattDescriptor descriptor) {
        super(descriptor, new RxObservableStrategy<BluetoothGattDescriptor, BletiaException>());
    }
}
