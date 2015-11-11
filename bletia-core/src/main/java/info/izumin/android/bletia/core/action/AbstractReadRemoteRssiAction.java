package info.izumin.android.bletia.core.action;

import info.izumin.android.bletia.core.BleErrorType;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.ResolveStrategy;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/15/15.
 */
public abstract class AbstractReadRemoteRssiAction extends AbstractAction<Integer, BletiaException, Void> {
    public AbstractReadRemoteRssiAction(ResolveStrategy<Integer, BletiaException> resolveStrategy) {
        super(null, Type.READ_REMOTE_RSSI, resolveStrategy);
    }

    @Override
    public boolean execute(BluetoothGattWrapper gattWrapper) {
        if (!gattWrapper.readRemoteRssi()) {
            reject(new BletiaException(this, BleErrorType.REQUEST_FAILURE));
            return false;
        }
        return true;
    }
}
