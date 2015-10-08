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
            getQueueFromContainer(container).enqueue((ReadCharacteristicAction) action);
        }

        @Override
        public boolean execute(Message msg, ActionQueueContainer container, BluetoothGattWrapper gattWrapper) {
            return getQueueFromContainer(container).execute(getIdentityFromMessage(msg), gattWrapper);
        }

        @Override
        public boolean isRunning(Message msg, ActionQueueContainer container) {
            return getQueueFromContainer(container).isRunning(getIdentityFromMessage(msg));
        }

        private UUID getIdentityFromMessage(Message msg) {
            return (UUID) msg.getData().getSerializable(ReadCharacteristicAction.KEY_UUID);
        }

        private ActionQueue<ReadCharacteristicAction, UUID> getQueueFromContainer(ActionQueueContainer container) {
            return container.getReadCharacteristicActionQueue();
        }
    },
    WRITE_CHARACTERISTIC(Action.Type.WRITE_CHARACTERISTIC) {
        @Override
        public void enqueue(Action action, ActionQueueContainer container) {
            getQueueFromContainer(container).enqueue((WriteCharacteristicAction) action);
        }

        @Override
        public boolean execute(Message msg, ActionQueueContainer container, BluetoothGattWrapper gattWrapper) {
            return getQueueFromContainer(container).execute(getIdentityFromMessage(msg), gattWrapper);
        }

        @Override
        public boolean isRunning(Message msg, ActionQueueContainer container) {
            return getQueueFromContainer(container).isRunning(getIdentityFromMessage(msg));
        }

        private UUID getIdentityFromMessage(Message msg) {
            return (UUID) msg.getData().getSerializable(WriteCharacteristicAction.KEY_UUID);
        }

        private ActionQueue<WriteCharacteristicAction, UUID> getQueueFromContainer(ActionQueueContainer container) {
            return container.getWriteCharacteristicActionQueue();
        }
    },
    READ_DESCRIPTOR(Action.Type.READ_DESCRIPTOR) {
        @Override
        public void enqueue(Action action, ActionQueueContainer container) {
            getQueueFromContainer(container).enqueue((ReadDescriptorAction) action);
        }

        @Override
        public boolean execute(Message msg, ActionQueueContainer container, BluetoothGattWrapper gattWrapper) {
            return getQueueFromContainer(container).execute(getIdentityFromMessage(msg), gattWrapper);
        }

        @Override
        public boolean isRunning(Message msg, ActionQueueContainer container) {
            return getQueueFromContainer(container).isRunning(getIdentityFromMessage(msg));
        }

        private UUID getIdentityFromMessage(Message msg) {
            return (UUID) msg.getData().getSerializable(ReadDescriptorAction.KEY_UUID);
        }

        private ActionQueue<ReadDescriptorAction, UUID> getQueueFromContainer(ActionQueueContainer container) {
            return container.getReadDescriptorActionQueue();
        }
    },
    WRITE_DESCRIPTOR(Action.Type.WRITE_DESCRIPTOR) {
        @Override
        public void enqueue(Action action, ActionQueueContainer container) {
            getQueueFromContainer(container).enqueue((WriteDescriptorAction) action);
        }

        @Override
        public boolean execute(Message msg, ActionQueueContainer container, BluetoothGattWrapper gattWrapper) {
            return getQueueFromContainer(container).execute(getIdentityFromMessage(msg), gattWrapper);
        }

        @Override
        public boolean isRunning(Message msg, ActionQueueContainer container) {
            return getQueueFromContainer(container).isRunning(getIdentityFromMessage(msg));
        }

        private UUID getIdentityFromMessage(Message msg) {
            return (UUID) msg.getData().getSerializable(WriteDescriptorAction.KEY_UUID);
        }

        private ActionQueue<WriteDescriptorAction, UUID> getQueueFromContainer(ActionQueueContainer container) {
            return container.getWriteDescriptorActionQueue();
        }
    },
    ENABLE_NOTIFICATION(Action.Type.ENABLE_NOTIFICATION) {
        @Override
        public void enqueue(Action action, ActionQueueContainer container) {
            getQueueFromContainer(container).enqueue((EnableNotificationAction) action);
        }

        @Override
        public boolean execute(Message msg, ActionQueueContainer container, BluetoothGattWrapper gattWrapper) {
            return getQueueFromContainer(container).execute(getIdentityFromMessage(msg), gattWrapper);
        }

        @Override
        public boolean isRunning(Message msg, ActionQueueContainer container) {
            return getQueueFromContainer(container).isRunning(getIdentityFromMessage(msg));
        }

        private UUID getIdentityFromMessage(Message msg) {
            return (UUID) msg.getData().getSerializable(EnableNotificationAction.KEY_UUID);
        }

        private ActionQueue<EnableNotificationAction, UUID> getQueueFromContainer(ActionQueueContainer container) {
            return container.getEnableNotificationActionQueue();
        }
    },
    READ_REMOTE_RSSI(Action.Type.READ_REMOTE_RSSI) {
        @Override
        public void enqueue(Action action, ActionQueueContainer container) {
            getQueueFromContainer(container).enqueue((ReadRemoteRssiAction) action);
        }

        @Override
        public boolean execute(Message msg, ActionQueueContainer container, BluetoothGattWrapper gattWrapper) {
            return getQueueFromContainer(container).execute(null, gattWrapper);
        }

        @Override
        public boolean isRunning(Message msg, ActionQueueContainer container) {
            return getQueueFromContainer(container).isRunning(null);
        }

        private ActionQueue<ReadRemoteRssiAction, Void> getQueueFromContainer(ActionQueueContainer container) {
            return container.getReadRemoteRssiActionQueue();
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
    public abstract boolean isRunning(Message msg, ActionQueueContainer container);
}
