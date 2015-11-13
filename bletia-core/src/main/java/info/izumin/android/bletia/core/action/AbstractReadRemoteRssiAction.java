package info.izumin.android.bletia.core.action;

import info.izumin.android.bletia.core.BleErrorType;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.ActionResolver;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/15/15.
 */
public abstract class AbstractReadRemoteRssiAction extends AbstractAction<Integer, BletiaException, Void> {
    public AbstractReadRemoteRssiAction(ActionResolver<Integer, BletiaException> actionResolver) {
        super(null, Type.READ_REMOTE_RSSI, actionResolver);
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
