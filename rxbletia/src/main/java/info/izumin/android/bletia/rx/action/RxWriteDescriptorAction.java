package info.izumin.android.bletia.rx.action;

import android.bluetooth.BluetoothGattDescriptor;

import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.action.AbstractWriteDescriptorAction;
import info.izumin.android.bletia.rx.RxObservableStrategy;
import rx.Observable;

/**
 * Created by izumin on 11/15/15.
 */
public class RxWriteDescriptorAction extends AbstractWriteDescriptorAction<Observable<BluetoothGattDescriptor>> {
    public static final String TAG = RxWriteDescriptorAction.class.getSimpleName();

    public RxWriteDescriptorAction(BluetoothGattDescriptor descriptor) {
        super(descriptor, new RxObservableStrategy<BluetoothGattDescriptor, BletiaException>());
    }
}
