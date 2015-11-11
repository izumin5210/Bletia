package info.izumin.android.bletia.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.izumin.android.bletia.core.action.AbstractAction;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 10/3/15.
 */
class ActionQueue<A extends AbstractAction<?, ?, I>, I> {

    private List<A> mWaitingActionList;
    private Map<I, A> mRunningActionMap;

    public ActionQueue() {
        mWaitingActionList = new ArrayList<>();
        mRunningActionMap = new HashMap<>();
    }

    public synchronized boolean enqueue(A action) {
        return mWaitingActionList.add(action);
    }

    public synchronized boolean execute(I identity, BluetoothGattWrapper gattWrapper) {
        if (isRunning(identity)) {
            return false;
        } else {
            for (A action : mWaitingActionList) {
                if ((identity == null) ? (action.getIdentity() == null) : (identity.equals(action.getIdentity()))) {
                    mWaitingActionList.remove(action);
                    if (action.execute(gattWrapper)) {
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
        return mRunningActionMap.remove(identity);
    }

    public boolean isRunning(I identity) {
        return mRunningActionMap.containsKey(identity);
    }
}
