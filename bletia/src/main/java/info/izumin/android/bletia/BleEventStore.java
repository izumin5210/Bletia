package info.izumin.android.bletia;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by izumin on 9/14/15.
 */
public class BleEventStore {
    private EnumMap<BletiaEvent.Type, Map<UUID, BletiaEvent>> mWaitingEventMap;
    private EnumMap<BletiaEvent.Type, Map<UUID, BletiaEvent>> mRunningEventMap;
    private EnumMap<BletiaEvent.Type, List<BletiaEvent>> mWaitingEventList;
    private EnumMap<BletiaEvent.Type, List<BletiaEvent>> mRunningEventList;

    public BleEventStore() {
        mWaitingEventMap = new EnumMap<>(BletiaEvent.Type.class);
        mRunningEventMap = new EnumMap<>(BletiaEvent.Type.class);
        mWaitingEventList = new EnumMap<>(BletiaEvent.Type.class);
        mRunningEventList = new EnumMap<>(BletiaEvent.Type.class);
    }

    public void addEvent(BletiaEvent event) {
        BletiaEvent.Type type = event.getType();
        if (event.getUuid() == null) {
            if (!mWaitingEventList.containsKey(type)) {
                mWaitingEventList.put(type, new ArrayList<BletiaEvent>());
            }
            mWaitingEventList.get(type).add(event);
        } else {
            if (!mWaitingEventMap.containsKey(type)) {
                mWaitingEventMap.put(type, new HashMap<UUID, BletiaEvent>());
            }
            mWaitingEventMap.get(type).put(event.getUuid(), event);
        }
    }

    public BletiaEvent runEvent(BletiaEvent.Type type) {
        BletiaEvent event = mWaitingEventList.get(type).remove(0);
        if (!mRunningEventList.containsKey(type)) {
            mRunningEventList.put(type, new ArrayList<BletiaEvent>());
        }
        mRunningEventList.get(type).add(event);
        return event;
    }

    public BletiaEvent runEvent(BletiaEvent.Type type, UUID uuid) {
        if (uuid == null) { return runEvent(type); }

        BletiaEvent event = mWaitingEventMap.get(type).remove(uuid);
        if (!mRunningEventMap.containsKey(type)) {
            mRunningEventMap.put(type, new HashMap<UUID, BletiaEvent>());
        }
        mRunningEventMap.get(type).put(uuid, event);
        return event;
    }

    public BletiaEvent closeEvent(BletiaEvent.Type type) {
        return mRunningEventList.get(type).remove(0);
    }

    public BletiaEvent closeEvent(BletiaEvent.Type type, UUID uuid) {
        return (uuid == null) ? closeEvent(type) : mRunningEventMap.get(type).remove(uuid);
    }

    public boolean isRunning(BletiaEvent.Type type) {
        return mRunningEventList.containsKey(type) && (mRunningEventList.get(type).size() > 0);
    }

    public boolean isRunning(BletiaEvent.Type type, UUID uuid) {
        if (uuid == null) { return isRunning(type); }
        return mRunningEventMap.containsKey(type) && mRunningEventMap.get(type).containsKey(uuid);
    }
}
