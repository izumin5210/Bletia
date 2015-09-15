package info.izumin.android.bletia;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import org.jdeferred.Promise;

import java.util.UUID;

import info.izumin.android.bletia.event.Event;
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

    public <T> Promise<T, BletiaException, Object> execute(Event<T> event) {
        mEventStore.add(event);
        dispatchMessage(event.obtainMessage());

        return event.getDeferred().promise();
    }

    @Override
    public void handleMessage(Message msg) {
        UUID uuid = (UUID) msg.getData().getSerializable(Event.KEY_UUID);
        Event.Type type = Event.Type.valueOf(msg.what);

        if (mEventStore.isRunning(type, uuid)) {
            sendMessageDelayed(msg, DELAY_MILLIS);
        } else {
            mEventStore.execute(type, uuid).handle(mGattWrapper);
        }
    }
}
