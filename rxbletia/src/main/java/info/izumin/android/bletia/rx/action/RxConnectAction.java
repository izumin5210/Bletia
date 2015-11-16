package info.izumin.android.bletia.rx.action;

import android.bluetooth.BluetoothDevice;

import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.StateContainer;
import info.izumin.android.bletia.core.action.AbstractConnectAction;
import info.izumin.android.bletia.core.wrapper.BluetoothDeviceWrapper;
import info.izumin.android.bletia.rx.RxObservableStrategy;
import rx.Observable;

/**
 * Created by izumin on 11/15/15.
 */
public class RxConnectAction extends AbstractConnectAction<Observable<Void>> {
    public static final String TAG = RxConnectAction.class.getSimpleName();

    public RxConnectAction(BluetoothDevice device, StateContainer container) {
        super(new RxObservableStrategy<Void, BletiaException>(), new BluetoothDeviceWrapper(device), container);
    }
}
