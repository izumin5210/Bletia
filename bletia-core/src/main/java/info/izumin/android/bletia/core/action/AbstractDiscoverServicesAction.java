package info.izumin.android.bletia.core.action;

import info.izumin.android.bletia.core.BleErrorType;
import info.izumin.android.bletia.core.BleState;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.ResolveStrategy;
import info.izumin.android.bletia.core.StateContainer;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 11/14/15.
 */
public abstract class AbstractDiscoverServicesAction<R> extends AbstractAction<Void, BletiaException, Void, R> {
    public static final String TAG = AbstractDiscoverServicesAction.class.getSimpleName();

    private final StateContainer mContaienr;

    public AbstractDiscoverServicesAction(ResolveStrategy<Void, BletiaException, R> resolveStrategy, StateContainer container) {
        super(null, Type.DISCOVER_SERVICES, resolveStrategy);
        mContaienr = container;
    }

    @Override
    public boolean execute(BluetoothGattWrapper gattWrapper) {
        if (!gattWrapper.discoverServices()) {
            reject(new BletiaException(this, BleErrorType.REQUEST_FAILURE));
            return false;
        }
        mContaienr.setState(BleState.SERVICE_DISCOVERING);
        return true;
    }
}
