package info.izumin.android.bletia.core;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.HandlerThread;

import java.util.List;
import java.util.UUID;

import info.izumin.android.bletia.core.action.AbstractAction;
import info.izumin.android.bletia.core.helper.ConnectionHelper;
import info.izumin.android.bletia.core.wrapper.BluetoothDeviceWrapper;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 11/12/15.
 */
public abstract class AbstractBletia {
    public static final String TAG = AbstractBletia.class.getSimpleName();

    private Context mContext;
    private BluetoothGattWrapper mGattWrapper;

    private BleState mState = BleState.DISCONNECTED;

    private ConnectionHelper mConnectionHelper;

    private ActionQueueContainer mQueueContainer;
    private BluetoothGattCallbackHandler mCallbackHandler;
    private BleMessageThread mMessageThread;

    private BleEventListener mSubListener;

    public AbstractBletia(Context context) {
        mContext = context;
        mConnectionHelper = new ConnectionHelper(mContext);
        mQueueContainer = new ActionQueueContainer();
        mCallbackHandler = new BluetoothGattCallbackHandler(mListener, mQueueContainer);
    }

    public BleState getState() {
        return mState;
    }

    public boolean isConnected() {
        return (mState == BleState.CONNECTED) || (mState == BleState.SERVICE_DISCOVERING) || isReady();
    }

    public boolean isReady() {
        return mState == BleState.SERVICE_DISCOVERED;
    }

    public void connect(BluetoothDevice device) {
        mState = BleState.CONNECTING;
        mGattWrapper = mConnectionHelper.connect(new BluetoothDeviceWrapper(device), mCallbackHandler);

        HandlerThread thread = new HandlerThread(device.getName());
        thread.start();
        mMessageThread = new BleMessageThread(thread, mGattWrapper, mQueueContainer);
    }

    public void disconenct() {
        mState = BleState.DISCONNECTING;
        mConnectionHelper.disconnect();
        mMessageThread.stop();
    }

    public boolean discoverServices() {
        mState = BleState.SERVICE_DISCOVERING;
        return mGattWrapper.discoverServices();
    }

    public BluetoothGattService getService(UUID uuid) {
        return mGattWrapper.getService(uuid);
    }

    public List<BluetoothGattService> getServices() {
        return mGattWrapper.getServices();
    }

    public BluetoothDevice getDevice() {
        return (mGattWrapper == null) ? null : mGattWrapper.getDevice();
    }

    protected void setSubListener(BleEventListener subListener) {
        mSubListener = subListener;
    }

    protected BluetoothGattWrapper getBluetoothGattWrapper() {
        return mGattWrapper;
    }

    protected void dispatchAction(AbstractAction action) {
        mMessageThread.dispatchAction(action);
    }

    private final BleEventListener mListener = new BleEventListener() {
        @Override
        public void onConnect(BluetoothGattWrapper gatt) {
            mState = BleState.CONNECTED;
            if (mSubListener != null) {
                mSubListener.onConnect(gatt);
            }
        }

        @Override
        public void onDisconnect(BluetoothGattWrapper gatt) {
            mState = BleState.DISCONNECTED;
            mConnectionHelper.close();
            if (mSubListener != null) {
                mSubListener.onDisconnect(gatt);
            }
        }

        @Override
        public void onServiceDiscovered(int status) {
            mState = BleState.SERVICE_DISCOVERED;
            if (mSubListener != null) {
                mSubListener.onServiceDiscovered(status);
            }
        }

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
        void onConnect(BluetoothGattWrapper gatt);
        void onDisconnect(BluetoothGattWrapper gatt);
        void onServiceDiscovered(int status);
        void onCharacteristicChanged(BluetoothGattCharacteristic characteristic);
        void onError(BletiaException exception);
    }
}
