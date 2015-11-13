package info.izumin.android.bletia.core;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import info.izumin.android.bletia.core.action.AbstractAction;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 11/11/15.
 */
public class BleMessageThread extends Handler {

    private static final int DELAY_MILLIS = 10;

    private final HandlerThread mHandlerThread;
    private final BluetoothGattWrapper mGattWrapper;
    private final StateContainer mContainer;
    private final int mDelayMillis;

    public BleMessageThread(HandlerThread handlerThread, BluetoothGattWrapper gattWrapper,
                            StateContainer queueContainer, int delayMillis) {
        super(handlerThread.getLooper());
        mHandlerThread = handlerThread;
        mGattWrapper = gattWrapper;
        mContainer = queueContainer;
        mDelayMillis = delayMillis;
    }

    public BleMessageThread(HandlerThread handlerThread, BluetoothGattWrapper gattWrapper,
                            StateContainer queueContainer) {
        this(handlerThread, gattWrapper, queueContainer, DELAY_MILLIS);
    }

    public void stop() {
        mHandlerThread.quitSafely();
    }

    public void dispatchAction(AbstractAction action) {
        mContainer.enqueue(action);
        dispatchMessage(action.obtainMessage());
    }

    @Override
    public void handleMessage(Message msg) {
        ActionMessageHandler handler = ActionMessageHandler.valueOf(msg.what);
        if (handler.isRunning(msg, mContainer)) {
            Message delayed = obtainMessage();
            delayed.copyFrom(msg);
            sendMessageDelayed(delayed, mDelayMillis);
        } else {
            ActionMessageHandler.valueOf(msg.what).execute(msg, mContainer, mGattWrapper);
        }
    }
}
