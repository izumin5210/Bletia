package info.izumin.android.bletia.action;

import info.izumin.android.bletia.BleErrorType;
import info.izumin.android.bletia.BletiaException;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/15/15.
 */
public class ReadRemoteRssiAction extends Action<Integer, Void> {
    public ReadRemoteRssiAction() {
        super(null);
    }

    @Override
    public Type getType() {
        return Type.READ_REMOTE_RSSI;
    }

    @Override
    public boolean execute(BluetoothGattWrapper gattWrapper) {
        if (!gattWrapper.readRemoteRssi()) {
            getDeferred().reject(new BletiaException(this, BleErrorType.REQUEST_FAILURE));
            return false;
        }
        return true;
    }
}
