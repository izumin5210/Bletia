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
    private EnumMap<BleEvent.Type, Map<UUID, BleEvent>> mWaitingEventMap;
    private EnumMap<BleEvent.Type, Map<UUID, BleEvent>> mRunningEventMap;
    private EnumMap<BleEvent.Type, List<BleEvent>> mWaitingEventList;
    private EnumMap<BleEvent.Type, List<BleEvent>> mRunningEventList;

    public BleEventStore() {
        mWaitingEventMap = new EnumMap<>(BleEvent.Type.class);
        mRunningEventMap = new EnumMap<>(BleEvent.Type.class);
        mWaitingEventList = new EnumMap<>(BleEvent.Type.class);
        mRunningEventList = new EnumMap<>(BleEvent.Type.class);
    }

    public void addEvent(BleEvent event) {
        BleEvent.Type type = event.getType();
        if (event.getUuid() == null) {
            if (!mWaitingEventList.containsKey(type)) {
                mWaitingEventList.put(type, new ArrayList<BleEvent>());
            }
            mWaitingEventList.get(type).add(event);
        } else {
            if (!mWaitingEventMap.containsKey(type)) {
                mWaitingEventMap.put(type, new HashMap<UUID, BleEvent>());
            }
            mWaitingEventMap.get(type).put(event.getUuid(), event);
        }
    }

    public BleEvent runEvent(BleEvent.Type type) {
        BleEvent event = mWaitingEventList.get(type).remove(0);
        if (!mRunningEventList.containsKey(type)) {
            mRunningEventList.put(type, new ArrayList<BleEvent>());
        }
        mRunningEventList.get(type).add(event);
        return event;
    }

    public BleEvent runEvent(BleEvent.Type type, UUID uuid) {
        if (uuid == null) { return runEvent(type); }

        BleEvent event = mWaitingEventMap.get(type).remove(uuid);
        if (!mRunningEventMap.containsKey(type)) {
            mRunningEventMap.put(type, new HashMap<UUID, BleEvent>());
        }
        mRunningEventMap.get(type).put(uuid, event);
        return event;
    }

    public BleEvent closeEvent(BleEvent.Type type) {
        return mRunningEventList.get(type).remove(0);
    }

    public BleEvent closeEvent(BleEvent.Type type, UUID uuid) {
        return (uuid == null) ? closeEvent(type) : mRunningEventMap.get(type).remove(uuid);
    }

    public boolean isRunning(BleEvent.Type type) {
        return mRunningEventList.containsKey(type) && (mRunningEventList.get(type).size() > 0);
    }

    public boolean isRunning(BleEvent.Type type, UUID uuid) {
        if (uuid == null) { return isRunning(type); }
        return mRunningEventMap.containsKey(type) && mRunningEventMap.get(type).containsKey(uuid);
    }
}
