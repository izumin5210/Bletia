package info.izumin.android.bletia;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import org.jdeferred.Promise;

import java.util.UUID;

import info.izumin.android.bletia.action.Action;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/14/15.
 */
public class BleMessageThread extends Handler {

    private static final int DELAY_MILLIS = 300;

    private final HandlerThread mHandlerThread;
    private final BluetoothGattWrapper mGattWrapper;
    private final BleActionStore mActionStore;

    public BleMessageThread(HandlerThread handlerThread, BluetoothGattWrapper gattWrapper, BleActionStore actionStore) {
        super(handlerThread.getLooper());
        mHandlerThread = handlerThread;
        mGattWrapper = gattWrapper;
        mActionStore = actionStore;
    }

    public void stop() {
        mHandlerThread.quitSafely();
    }

    public <T> Promise<T, BletiaException, Object> execute(Action<T> action) {
        mActionStore.add(action);
        dispatchMessage(action.obtainMessage());

        return action.getDeferred().promise();
    }

    @Override
    public void handleMessage(Message msg) {
        UUID uuid = (UUID) msg.getData().getSerializable(Action.KEY_UUID);
        Action.Type type = Action.Type.valueOf(msg.what);

        if (mActionStore.isRunning(type, uuid)) {
            sendMessageDelayed(msg, DELAY_MILLIS);
        } else {
            mActionStore.execute(type, uuid).handle(mGattWrapper);
        }
    }
}
