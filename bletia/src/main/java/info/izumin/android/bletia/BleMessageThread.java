package info.izumin.android.bletia;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import org.jdeferred.Promise;

import info.izumin.android.bletia.action.Action;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/14/15.
 */
class BleMessageThread extends Handler {

    private static final int DELAY_MILLIS = 10;

    private final HandlerThread mHandlerThread;
    private final BluetoothGattWrapper mGattWrapper;
    private final ActionQueueContainer mQueueContainer;

    public BleMessageThread(HandlerThread handlerThread, BluetoothGattWrapper gattWrapper, ActionQueueContainer queueContainer) {
        super(handlerThread.getLooper());
        mHandlerThread = handlerThread;
        mGattWrapper = gattWrapper;
        mQueueContainer = queueContainer;
    }

    public void stop() {
        mHandlerThread.quitSafely();
    }

    public <T> Promise<T, BletiaException, Void> execute(Action<T, ?> action) {
        ActionMessageHandler.valueOf(action.getType()).enqueue(action, mQueueContainer);
        dispatchMessage(action.obtainMessage());

        return action.getDeferred().promise();
    }

    @Override
    public void handleMessage(Message msg) {
        ActionMessageHandler handler = ActionMessageHandler.valueOf(msg.what);
        if (handler.isRunning(msg, mQueueContainer)) {
            Message delayed = obtainMessage();
            delayed.copyFrom(msg);
            sendMessageDelayed(delayed, DELAY_MILLIS);
        } else {
            ActionMessageHandler.valueOf(msg.what).execute(msg, mQueueContainer, mGattWrapper);
        }
    }
}
