package info.izumin.android.bletia.core.action;

import info.izumin.android.bletia.core.BleState;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.ResolveStrategy;
import info.izumin.android.bletia.core.StateContainer;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 11/14/15.
 */
public abstract class AbstractDisconnectAction<R> extends AbstractAction<Void, BletiaException, Void, R> {
    public static final String TAG = AbstractDisconnectAction.class.getSimpleName();

    private final StateContainer mContainer;

    public AbstractDisconnectAction(ResolveStrategy<Void, BletiaException, R> resolveStrategy, StateContainer container) {
        super(null, Type.DISCONNECT, resolveStrategy);
        mContainer = container;
    }

    @Override
    public boolean execute(BluetoothGattWrapper gattWrapper) {
        mContainer.setState(BleState.DISCONNECTING);
        gattWrapper.disconnect();
        return true;
    }
}
