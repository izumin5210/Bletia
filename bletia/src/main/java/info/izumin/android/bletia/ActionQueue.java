package info.izumin.android.bletia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.izumin.android.bletia.action.Action;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 10/3/15.
 */
class ActionQueue<A extends Action<?, I>, I> {

    private List<A> mWaitingActionList;
    private Map<I, A> mRunningActionMap;
    private BluetoothGattWrapper mGattWrapper;

    public ActionQueue(BluetoothGattWrapper gattWrapper) {
        mRunningActionMap = new HashMap<>();
        mGattWrapper = gattWrapper;
    }

    public synchronized boolean enqueue(A action) {
        return mWaitingActionList.add(action);
    }

    public synchronized boolean execute(I identity) {
        if (isRunning(identity)) {
            return false;
        } else {
            for (A action : mWaitingActionList) {
                if ((identity == null) ? (action.getIdentity() == null) : (identity.equals(action.getIdentity()))) {
                    action.execute(mGattWrapper);
                    mWaitingActionList.remove(action);
                    mRunningActionMap.put(action.getIdentity(), action);
                    return true;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public synchronized A dequeue(I identity) {
        return mRunningActionMap.remove(identity);
    }

    public boolean isRunning(I identity) {
        return mRunningActionMap.containsKey(identity);
    }
}
