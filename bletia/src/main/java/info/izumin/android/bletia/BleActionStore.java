package info.izumin.android.bletia;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import info.izumin.android.bletia.action.Action;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/14/15.
 */
public class BleActionStore {
    private EnumMap<Action.Type, List<Action>> mWaitingActionQueue;
    private EnumMap<Action.Type, List<Action>> mRunningActionQueue;

    public BleActionStore() {
        mWaitingActionQueue = new EnumMap<>(Action.Type.class);
        mRunningActionQueue = new EnumMap<>(Action.Type.class);
    }

    public void enqueue(Action action) {
        Action.Type type = action.getType();
        if (!mWaitingActionQueue.containsKey(type)) {
            mWaitingActionQueue.put(type, new ArrayList<Action>());
        }
        mWaitingActionQueue.get(type).add(action);
    }

    public void execute(Action.Type type, BluetoothGattWrapper gattWrapper) {
        Action action = mWaitingActionQueue.get(type).remove(0);
        if (!mRunningActionQueue.containsKey(type)) {
            mRunningActionQueue.put(type, new ArrayList<Action>());
        }
        mRunningActionQueue.get(type).add(action);
        action.execute(gattWrapper);
    }

    public Action dequeue(Action.Type type) {
        return mRunningActionQueue.get(type).remove(0);
    }

    public boolean isRunning(Action.Type type) {
        return mRunningActionQueue.containsKey(type) && (mRunningActionQueue.get(type).size() > 0);
    }
}
