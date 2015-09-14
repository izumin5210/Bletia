package info.izumin.android.bletia;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import org.jdeferred.Deferred;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

import java.util.UUID;

import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/14/15.
 */
public class BleMessageThread extends Handler {

    private static final int DELAY_MILLIS = 300;

    private final HandlerThread mHandlerThread;
    private final BluetoothGattWrapper mGattWrapper;
    private final BleEventStore mEventStore;

    public BleMessageThread(HandlerThread handlerThread, BluetoothGattWrapper gattWrapper, BleEventStore eventStore) {
        super(handlerThread.getLooper());
        mHandlerThread = handlerThread;
        mGattWrapper = gattWrapper;
        mEventStore = eventStore;
    }

    public void stop() {
        mHandlerThread.quitSafely();
    }

    public <T> Promise<T, BleStatus, Object> sendEvent(BleEvent<T> event) {
        Deferred<T, BleStatus, Object> deferred = new DeferredObject<>();
        Promise<T, BleStatus, Object> promise = deferred.promise();
        event.setDeferred(deferred);

        mEventStore.addEvent(event);
        dispatchMessage(event.obtainMessage());

        return promise;
    }

    @Override
    public void handleMessage(Message msg) {
        UUID uuid = (UUID) msg.getData().getSerializable(BleEvent.KEY_UUID);
        BleEvent.Type type = BleEvent.Type.valueOf(msg.what);

        if (mEventStore.isRunning(type, uuid)) {
            sendMessageDelayed(msg, DELAY_MILLIS);
            return;
        }

        type.handle(mGattWrapper, mEventStore.runEvent(type, uuid));
    }
}
