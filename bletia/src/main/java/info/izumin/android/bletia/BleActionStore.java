package info.izumin.android.bletia;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import info.izumin.android.bletia.action.Action;

/**
 * Created by izumin on 9/14/15.
 */
public class BleActionStore {
    private EnumMap<Action.Type, Map<UUID, Action>> mWaitingActionMap;
    private EnumMap<Action.Type, Map<UUID, Action>> mRunningActionMap;
    private EnumMap<Action.Type, List<Action>> mWaitingActionList;
    private EnumMap<Action.Type, List<Action>> mRunningActionList;

    public BleActionStore() {
        mWaitingActionMap = new EnumMap<>(Action.Type.class);
        mRunningActionMap = new EnumMap<>(Action.Type.class);
        mWaitingActionList = new EnumMap<>(Action.Type.class);
        mRunningActionList = new EnumMap<>(Action.Type.class);
    }

    public void add(Action action) {
        Action.Type type = action.getType();
        if (action.getUuid() == null) {
            if (!mWaitingActionList.containsKey(type)) {
                mWaitingActionList.put(type, new ArrayList<Action>());
            }
            mWaitingActionList.get(type).add(action);
        } else {
            if (!mWaitingActionMap.containsKey(type)) {
                mWaitingActionMap.put(type, new HashMap<UUID, Action>());
            }
            mWaitingActionMap.get(type).put(action.getUuid(), action);
        }
    }

    public Action execute(Action.Type type) {
        Action action = mWaitingActionList.get(type).remove(0);
        if (!mRunningActionList.containsKey(type)) {
            mRunningActionList.put(type, new ArrayList<Action>());
        }
        mRunningActionList.get(type).add(action);
        return action;
    }

    public Action execute(Action.Type type, UUID uuid) {
        if (uuid == null) { return execute(type); }

        Action action = mWaitingActionMap.get(type).remove(uuid);
        if (!mRunningActionMap.containsKey(type)) {
            mRunningActionMap.put(type, new HashMap<UUID, Action>());
        }
        mRunningActionMap.get(type).put(uuid, action);
        return action;
    }

    public Action close(Action.Type type) {
        return mRunningActionList.get(type).remove(0);
    }

    public Action close(Action.Type type, UUID uuid) {
        return (uuid == null) ? close(type) : mRunningActionMap.get(type).remove(uuid);
    }

    public boolean isRunning(Action.Type type) {
        return mRunningActionList.containsKey(type) && (mRunningActionList.get(type).size() > 0);
    }

    public boolean isRunning(Action.Type type, UUID uuid) {
        if (uuid == null) { return isRunning(type); }
        return mRunningActionMap.containsKey(type) && mRunningActionMap.get(type).containsKey(uuid);
    }
}
