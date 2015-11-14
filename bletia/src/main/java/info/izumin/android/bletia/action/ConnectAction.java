package info.izumin.android.bletia.action;

import android.bluetooth.BluetoothDevice;

import org.jdeferred.Promise;

import info.izumin.android.bletia.DeferredStrategy;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.StateContainer;
import info.izumin.android.bletia.core.action.AbstractConnectAction;
import info.izumin.android.bletia.core.wrapper.BluetoothDeviceWrapper;

/**
 * Created by izumin on 11/14/15.
 */
public class ConnectAction extends AbstractConnectAction<Promise<Void, BletiaException, Void>> {
    public static final String TAG = ConnectAction.class.getSimpleName();

    public ConnectAction(BluetoothDevice device, StateContainer container) {
        super(new DeferredStrategy<Void, BletiaException>(), new BluetoothDeviceWrapper(device), container);
    }
}
