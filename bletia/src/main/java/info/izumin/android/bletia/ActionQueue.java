package info.izumin.android.bletia;

import android.os.Handler;
import android.os.Looper;
import info.izumin.android.bletia.action.Action;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by izumin on 10/3/15.
 */
public class ActionQueue<A extends Action<?, I>, I> {

    private List<A> mWaitingActionList;
    private Map<I, A> mRunningActionMap;
    private Handler mHandler;

    public ActionQueue() {
        mWaitingActionList = new ArrayList<>();
        mRunningActionMap = new HashMap<>();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public synchronized boolean enqueue(A action) {
        return mWaitingActionList.add(action);
    }

    public synchronized boolean execute(I identity, BluetoothGattWrapper gattWrapper) {
        if (isRunning(identity)) {
            return false;
        } else {
            for (final A action : mWaitingActionList) {
                if ((identity == null) ? (action.getIdentity() == null) : (identity.equals(action.getIdentity()))) {
                    mWaitingActionList.remove(action);
                    if (action.execute(gattWrapper)) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!action.isDequeued()) {
                                    dequeue(action.getIdentity());
                                    action.getDeferred().reject(new BletiaException(BleErrorType.TIMEOUT));
                                }
                            }
                        }, action.getTimeoutMillis());
                        mRunningActionMap.put(action.getIdentity(), action);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public synchronized A dequeue(I identity) {
        A action = mRunningActionMap.remove(identity);
        action.setDequeued(true);
        return action;
    }

    public boolean isRunning(I identity) {
        return mRunningActionMap.containsKey(identity);
    }
}
