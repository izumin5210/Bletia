package info.izumin.android.bletia.core.action;

import android.os.HandlerThread;

import info.izumin.android.bletia.core.BleMessageThread;
import info.izumin.android.bletia.core.BleState;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.ResolveStrategy;
import info.izumin.android.bletia.core.StateContainer;
import info.izumin.android.bletia.core.wrapper.BluetoothDeviceWrapper;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 11/14/15.
 */
public abstract class AbstractConnectAction<R> extends AbstractAction<Void, BletiaException, Void, R> {
    public static final String TAG = AbstractConnectAction.class.getSimpleName();

    private final BluetoothDeviceWrapper mDevice;
    private final StateContainer mContainer;

    public AbstractConnectAction(ResolveStrategy<Void, BletiaException, R> resolveStrategy, BluetoothDeviceWrapper device, StateContainer container) {
        super(null, Type.CONNECT, resolveStrategy);
        mDevice = device;
        mContainer = container;
        HandlerThread thread = new HandlerThread(mDevice.getName());
        thread.start();
        mContainer.setMessageThread(new BleMessageThread(thread, mContainer));
    }

    @Override
    public boolean execute(BluetoothGattWrapper gattWrapper) {
        mContainer.setState(BleState.CONNECTING);
        mContainer.setGattWrapper(mDevice.connectGatt(
                mContainer.getContext(), false, mContainer.getCallbackHandler()));
        return true;
    }
}
