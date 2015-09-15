package info.izumin.android.bletia;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import info.izumin.android.bletia.event.Event;

/**
 * Created by izumin on 9/14/15.
 */
public class BleEventStore {
    private EnumMap<Event.Type, Map<UUID, Event>> mWaitingEventMap;
    private EnumMap<Event.Type, Map<UUID, Event>> mRunningEventMap;
    private EnumMap<Event.Type, List<Event>> mWaitingEventList;
    private EnumMap<Event.Type, List<Event>> mRunningEventList;

    public BleEventStore() {
        mWaitingEventMap = new EnumMap<>(Event.Type.class);
        mRunningEventMap = new EnumMap<>(Event.Type.class);
        mWaitingEventList = new EnumMap<>(Event.Type.class);
        mRunningEventList = new EnumMap<>(Event.Type.class);
    }

    public void add(Event event) {
        Event.Type type = event.getType();
        if (event.getUuid() == null) {
            if (!mWaitingEventList.containsKey(type)) {
                mWaitingEventList.put(type, new ArrayList<Event>());
            }
            mWaitingEventList.get(type).add(event);
        } else {
            if (!mWaitingEventMap.containsKey(type)) {
                mWaitingEventMap.put(type, new HashMap<UUID, Event>());
            }
            mWaitingEventMap.get(type).put(event.getUuid(), event);
        }
    }

    public Event execute(Event.Type type) {
        Event event = mWaitingEventList.get(type).remove(0);
        if (!mRunningEventList.containsKey(type)) {
            mRunningEventList.put(type, new ArrayList<Event>());
        }
        mRunningEventList.get(type).add(event);
        return event;
    }

    public Event execute(Event.Type type, UUID uuid) {
        if (uuid == null) { return execute(type); }

        Event event = mWaitingEventMap.get(type).remove(uuid);
        if (!mRunningEventMap.containsKey(type)) {
            mRunningEventMap.put(type, new HashMap<UUID, Event>());
        }
        mRunningEventMap.get(type).put(uuid, event);
        return event;
    }

    public Event close(Event.Type type) {
        return mRunningEventList.get(type).remove(0);
    }

    public Event close(Event.Type type, UUID uuid) {
        return (uuid == null) ? close(type) : mRunningEventMap.get(type).remove(uuid);
    }

    public boolean isRunning(Event.Type type) {
        return mRunningEventList.containsKey(type) && (mRunningEventList.get(type).size() > 0);
    }

    public boolean isRunning(Event.Type type, UUID uuid) {
        if (uuid == null) { return isRunning(type); }
        return mRunningEventMap.containsKey(type) && mRunningEventMap.get(type).containsKey(uuid);
    }
}
