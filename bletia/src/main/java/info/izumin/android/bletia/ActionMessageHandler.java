package info.izumin.android.bletia;

import android.os.Message;

import java.util.UUID;

import info.izumin.android.bletia.action.Action;
import info.izumin.android.bletia.action.EnableNotificationAction;
import info.izumin.android.bletia.action.ReadCharacteristicAction;
import info.izumin.android.bletia.action.ReadDescriptorAction;
import info.izumin.android.bletia.action.ReadRemoteRssiAction;
import info.izumin.android.bletia.action.WriteCharacteristicAction;
import info.izumin.android.bletia.action.WriteDescriptorAction;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 10/3/15.
 */
enum ActionMessageHandler {
    READ_CHARACTERISTIC(Action.Type.READ_CHARACTERISTIC) {
        @Override
        public void enqueue(Action action, ActionQueueContainer container) {
            container.getReadCharacteristicActionQueue().enqueue((ReadCharacteristicAction) action);
        }

        @Override
        public boolean execute(Message msg, ActionQueueContainer container, BluetoothGattWrapper gattWrapper) {
            UUID uuid = (UUID) msg.getData().getSerializable(ReadCharacteristicAction.KEY_UUID);
            return container.getReadCharacteristicActionQueue().execute(uuid, gattWrapper);
        }
    },
    WRITE_CHARACTERISTIC(Action.Type.WRITE_CHARACTERISTIC) {
        @Override
        public void enqueue(Action action, ActionQueueContainer container) {
            container.getWriteCharacteristicActionQueue().enqueue((WriteCharacteristicAction) action);
        }

        @Override
        public boolean execute(Message msg, ActionQueueContainer container, BluetoothGattWrapper gattWrapper) {
            UUID uuid = (UUID) msg.getData().getSerializable(WriteCharacteristicAction.KEY_UUID);
            return container.getWriteCharacteristicActionQueue().execute(uuid, gattWrapper);
        }
    },
    READ_DESCRIPTOR(Action.Type.READ_DESCRIPTOR) {
        @Override
        public void enqueue(Action action, ActionQueueContainer container) {
            container.getReadDescriptorActionQueue().enqueue((ReadDescriptorAction) action);
        }

        @Override
        public boolean execute(Message msg, ActionQueueContainer container, BluetoothGattWrapper gattWrapper) {
            UUID uuid = (UUID) msg.getData().getSerializable(ReadDescriptorAction.KEY_UUID);
            return container.getReadDescriptorActionQueue().execute(uuid, gattWrapper);
        }
    },
    WRITE_DESCRIPTOR(Action.Type.WRITE_DESCRIPTOR) {
        @Override
        public void enqueue(Action action, ActionQueueContainer container) {
            container.getWriteDescriptorActionQueue().enqueue((WriteDescriptorAction) action);
        }

        @Override
        public boolean execute(Message msg, ActionQueueContainer container, BluetoothGattWrapper gattWrapper) {
            UUID uuid = (UUID) msg.getData().getSerializable(WriteDescriptorAction.KEY_UUID);
            return container.getWriteDescriptorActionQueue().execute(uuid, gattWrapper);
        }
    },
    ENABLE_NOTIFICATION(Action.Type.ENABLE_NOTIFICATION) {
        @Override
        public void enqueue(Action action, ActionQueueContainer container) {
            container.getEnableNotificationActionQueue().enqueue((EnableNotificationAction) action);
        }

        @Override
        public boolean execute(Message msg, ActionQueueContainer container, BluetoothGattWrapper gattWrapper) {
            UUID uuid = (UUID) msg.getData().getSerializable(EnableNotificationAction.KEY_UUID);
            return container.getEnableNotificationActionQueue().execute(uuid, gattWrapper);
        }
    },
    READ_REMOTE_RSSI(Action.Type.READ_REMOTE_RSSI) {
        @Override
        public void enqueue(Action action, ActionQueueContainer container) {
            container.getReadRemoteRssiActionQueue().enqueue((ReadRemoteRssiAction) action);
        }

        @Override
        public boolean execute(Message msg, ActionQueueContainer container, BluetoothGattWrapper gattWrapper) {
            return container.getReadRemoteRssiActionQueue().execute(null, gattWrapper);
        }
    };

    private final Action.Type mType;

    ActionMessageHandler(Action.Type type) {
        mType = type;
    }

    public Action.Type getType() {
        return mType;
    }

    public static ActionMessageHandler valueOf(Action.Type type) {
        return valueOf(type.getCode());
    }

    public static ActionMessageHandler valueOf(int what) {
        for (ActionMessageHandler handler : values()) {
            if (what == handler.getType().getCode()) { return handler; }
        }
        throw new IllegalArgumentException();
    }

    public abstract void enqueue(Action action, ActionQueueContainer container);
    public abstract boolean execute(Message msg, ActionQueueContainer container, BluetoothGattWrapper gattWrapper);
}
