package info.izumin.android.bletia;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by izumin on 9/14/15.
 */
public class BleEventStore {
    private EnumMap<BleEvent.Type, Map<UUID, BleEvent>> mWaitingEventMap;
    private EnumMap<BleEvent.Type, Map<UUID, BleEvent>> mRunningEventMap;

    public BleEventStore() {
        mWaitingEventMap = new EnumMap<>(BleEvent.Type.class);
        mRunningEventMap = new EnumMap<>(BleEvent.Type.class);
    }

    public void addEvent(BleEvent event) {
        if (!mWaitingEventMap.containsKey(event.getType())) {
            mWaitingEventMap.put(event.getType(), new HashMap<UUID, BleEvent>());
        }
        mWaitingEventMap.get(event.getType()).put(event.getUuid(), event);
    }

    public BleEvent runEvent(BleEvent.Type type, UUID uuid) {
        BleEvent event = mWaitingEventMap.get(type).remove(uuid);
        if (!mRunningEventMap.containsKey(type)) {
            mRunningEventMap.put(type, new HashMap<UUID, BleEvent>());
        }
        mRunningEventMap.get(type).put(uuid, event);
        return event;
    }

    public BleEvent closeEvent(BleEvent.Type type, UUID uuid) {
        return mRunningEventMap.get(type).remove(uuid);
    }

    public boolean isRunning(BleEvent.Type type, UUID uuid) {
        return mRunningEventMap.containsKey(type) && mRunningEventMap.get(type).containsKey(uuid);
    }
}
