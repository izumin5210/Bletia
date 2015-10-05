package info.izumin.android.bletia;

import java.util.UUID;

import info.izumin.android.bletia.action.EnableNotificationAction;
import info.izumin.android.bletia.action.ReadCharacteristicAction;
import info.izumin.android.bletia.action.ReadDescriptorAction;
import info.izumin.android.bletia.action.ReadRemoteRssiAction;
import info.izumin.android.bletia.action.WriteCharacteristicAction;
import info.izumin.android.bletia.action.WriteDescriptorAction;

/**
 * Created by izumin on 10/3/15.
 */
public class ActionQueueContainer {
    private ActionQueue<ReadCharacteristicAction, UUID> mReadCharacteristicActionQueue;
    private ActionQueue<WriteCharacteristicAction, UUID> mWriteCharacteristicActionQueue;
    private ActionQueue<ReadDescriptorAction, UUID> mReadDescriptorActionQueue;
    private ActionQueue<WriteDescriptorAction, UUID> mWriteDescriptorActionQueue;
    private ActionQueue<EnableNotificationAction, UUID> mEnableNotificationActionQueue;
    private ActionQueue<ReadRemoteRssiAction, Void> mReadRemoteRssiActionQueue;

    public ActionQueueContainer() {
        mReadCharacteristicActionQueue = new ActionQueue<>();
        mWriteCharacteristicActionQueue = new ActionQueue<>();
        mReadDescriptorActionQueue = new ActionQueue<>();
        mWriteDescriptorActionQueue = new ActionQueue<>();
        mEnableNotificationActionQueue = new ActionQueue<>();
        mReadRemoteRssiActionQueue = new ActionQueue<>();
    }

    public ActionQueue<ReadCharacteristicAction, UUID> getReadCharacteristicActionQueue() {
        return mReadCharacteristicActionQueue;
    }

    public ActionQueue<WriteCharacteristicAction, UUID> getWriteCharacteristicActionQueue() {
        return mWriteCharacteristicActionQueue;
    }

    public ActionQueue<ReadDescriptorAction, UUID> getReadDescriptorActionQueue() {
        return mReadDescriptorActionQueue;
    }

    public ActionQueue<WriteDescriptorAction, UUID> getWriteDescriptorActionQueue() {
        return mWriteDescriptorActionQueue;
    }

    public ActionQueue<EnableNotificationAction, UUID> getEnableNotificationActionQueue() {
        return mEnableNotificationActionQueue;
    }

    public ActionQueue<ReadRemoteRssiAction, Void> getReadRemoteRssiActionQueue() {
        return mReadRemoteRssiActionQueue;
    }
}
