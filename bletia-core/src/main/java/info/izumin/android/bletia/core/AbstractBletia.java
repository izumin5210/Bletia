package info.izumin.android.bletia.core;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;

import java.util.List;
import java.util.UUID;

import info.izumin.android.bletia.core.action.AbstractAction;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 11/12/15.
 */
public abstract class AbstractBletia {
    public static final String TAG = AbstractBletia.class.getSimpleName();

    private StateContainer mContainer;
    private BleEventListener mSubListener;

    public AbstractBletia(Context context) {
        mContainer = new StateContainer(context, mListener);
    }

    public BleState getState() {
        return mContainer.getState();
    }

    protected void setState(BleState state) {
        mContainer.setState(state);
    }

    public boolean isConnected() {
        return mContainer.isConnected();
    }

    public boolean isReady() {
        return mContainer.isReady();
    }

    public BluetoothGattService getService(UUID uuid) {
        return getBluetoothGattWrapper().getService(uuid);
    }

    public List<BluetoothGattService> getServices() {
        return getBluetoothGattWrapper().getServices();
    }

    public BluetoothDevice getDevice() {
        return (getBluetoothGattWrapper() == null) ? null : getBluetoothGattWrapper().getDevice();
    }

    public <T, E extends Throwable, R> R execute(AbstractAction<T, E, ?, R> action) {
        dispatchAction(action);
        return action.getResolver();
    }

    protected void setSubListener(BleEventListener subListener) {
        mSubListener = subListener;
    }

    protected BluetoothGattWrapper getBluetoothGattWrapper() {
        return mContainer.getGattWrapper();
    }

    protected StateContainer getStateContainer() {
        return mContainer;
    }

    protected void dispatchAction(AbstractAction action) {
        mContainer.getMessageThread().dispatchAction(action);
    }

    private final BleEventListener mListener = new BleEventListener() {
        @Override
        public void onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
            if (mSubListener != null) {
                mSubListener.onCharacteristicChanged(characteristic);
            }
        }

        @Override
        public void onError(BletiaException exception) {
            if (mSubListener != null) {
                mSubListener.onError(exception);
            }
        }
    };

    protected interface BleEventListener {
        void onCharacteristicChanged(BluetoothGattCharacteristic characteristic);
        void onError(BletiaException exception);
    }
}
