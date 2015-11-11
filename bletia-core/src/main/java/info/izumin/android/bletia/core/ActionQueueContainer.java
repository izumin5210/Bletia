package info.izumin.android.bletia.core;

import java.util.UUID;

import info.izumin.android.bletia.core.action.AbstractAction;
import info.izumin.android.bletia.core.action.AbstractEnableNotificationAction;
import info.izumin.android.bletia.core.action.AbstractReadCharacteristicAction;
import info.izumin.android.bletia.core.action.AbstractReadDescriptorAction;
import info.izumin.android.bletia.core.action.AbstractReadRemoteRssiAction;
import info.izumin.android.bletia.core.action.AbstractWriteCharacteristicAction;
import info.izumin.android.bletia.core.action.AbstractWriteDescriptorAction;

/**
 * Created by izumin on 10/3/15.
 */
public class ActionQueueContainer {
    private ActionQueue<AbstractReadCharacteristicAction, UUID> mReadCharacteristicActionQueue;
    private ActionQueue<AbstractWriteCharacteristicAction, UUID> mWriteCharacteristicActionQueue;
    private ActionQueue<AbstractReadDescriptorAction, UUID> mReadDescriptorActionQueue;
    private ActionQueue<AbstractWriteDescriptorAction, UUID> mWriteDescriptorActionQueue;
    private ActionQueue<AbstractEnableNotificationAction, UUID> mEnableNotificationActionQueue;
    private ActionQueue<AbstractReadRemoteRssiAction, Void> mReadRemoteRssiActionQueue;

    public ActionQueueContainer() {
        mReadCharacteristicActionQueue = new ActionQueue<>();
        mWriteCharacteristicActionQueue = new ActionQueue<>();
        mReadDescriptorActionQueue = new ActionQueue<>();
        mWriteDescriptorActionQueue = new ActionQueue<>();
        mEnableNotificationActionQueue = new ActionQueue<>();
        mReadRemoteRssiActionQueue = new ActionQueue<>();
    }

    public ActionQueue<AbstractReadCharacteristicAction, UUID> getReadCharacteristicActionQueue() {
        return mReadCharacteristicActionQueue;
    }

    public ActionQueue<AbstractWriteCharacteristicAction, UUID> getWriteCharacteristicActionQueue() {
        return mWriteCharacteristicActionQueue;
    }

    public ActionQueue<AbstractReadDescriptorAction, UUID> getReadDescriptorActionQueue() {
        return mReadDescriptorActionQueue;
    }

    public ActionQueue<AbstractWriteDescriptorAction, UUID> getWriteDescriptorActionQueue() {
        return mWriteDescriptorActionQueue;
    }

    public ActionQueue<AbstractEnableNotificationAction, UUID> getEnableNotificationActionQueue() {
        return mEnableNotificationActionQueue;
    }

    public ActionQueue<AbstractReadRemoteRssiAction, Void> getReadRemoteRssiActionQueue() {
        return mReadRemoteRssiActionQueue;
    }

    public void enqueue(AbstractAction action) {
        if (action instanceof AbstractReadCharacteristicAction) {
            mReadCharacteristicActionQueue.enqueue((AbstractReadCharacteristicAction) action);
        } else if (action instanceof AbstractWriteCharacteristicAction) {
            mWriteCharacteristicActionQueue.enqueue((AbstractWriteCharacteristicAction) action);
        } else if (action instanceof AbstractReadDescriptorAction) {
            mReadDescriptorActionQueue.enqueue((AbstractReadDescriptorAction) action);
        } else if (action instanceof AbstractWriteDescriptorAction) {
            mWriteDescriptorActionQueue.enqueue((AbstractWriteDescriptorAction) action);
        } else if (action instanceof AbstractEnableNotificationAction) {
            mEnableNotificationActionQueue.enqueue((AbstractEnableNotificationAction) action);
        } else if (action instanceof AbstractReadRemoteRssiAction) {
            mReadRemoteRssiActionQueue.enqueue((AbstractReadRemoteRssiAction) action);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
