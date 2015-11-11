package info.izumin.android.bletia.core.action;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.izumin.android.bletia.core.action.AbstractAction;
import info.izumin.android.bletia.core.action.ActionQueue;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by izumin on 10/5/15.
 */
@RunWith(AndroidJUnit4.class)
public class ActionQueueTest {
    public class ActionImpl extends AbstractAction<String, Exception, String> {
        public ActionImpl(String identity) {
            super(identity, null, null);
        }

        @Override
        public boolean execute(BluetoothGattWrapper gattWrapper) {
            return false;
        }
    }

    private ActionQueue<ActionImpl, String> mQueue;
    @Mock private ActionImpl mAction;
    @Spy private List<ActionImpl> mWaitingActionList = new ArrayList<>();
    @Spy private Map<String, ActionImpl> mRunningActionMap = new HashMap<>();
    @Mock private BluetoothGattWrapper mGattWrapper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mQueue = new ActionQueue<>();
        Whitebox.setInternalState(mAction, "mIdentity", "test");
        Whitebox.setInternalState(mQueue, "mWaitingActionList", mWaitingActionList);
        Whitebox.setInternalState(mQueue, "mRunningActionMap", mRunningActionMap);
        mWaitingActionList.add(mAction);
    }

    @Test
    public void enqueue() throws Exception {
        ActionImpl action = new ActionImpl("action");
        assertThat(mQueue.enqueue(action)).isTrue();
        verify(mWaitingActionList, times(1)).add(action);
    }

    @Test
    public void execute_WhenActionThatHasTheSameIdentityIsRunning() throws Exception {
        when(mQueue.isRunning("test")).thenReturn(true);
        assertThat(mQueue.execute("test", mGattWrapper)).isFalse();
        verify(mAction, never()).execute(mGattWrapper);
        verify(mWaitingActionList, never()).remove(mAction);
        verify(mRunningActionMap, never()).put("test", mAction);
    }

    @Test
    public void execute_WhenActionExecuteReturnFalse() throws Exception {
        when(mAction.execute(mGattWrapper)).thenReturn(false);
        assertThat(mQueue.execute("test", mGattWrapper)).isFalse();
        verify(mAction, times(1)).execute(mGattWrapper);
        verify(mWaitingActionList, times(1)).remove(mAction);
        verify(mRunningActionMap, never()).put("test", mAction);
    }

    @Test
    public void execute_WhenActionExecuteReturnTrue() throws Exception {
        when(mAction.execute(mGattWrapper)).thenReturn(true);
        assertThat(mQueue.execute("test", mGattWrapper)).isTrue();
        verify(mAction, times(1)).execute(mGattWrapper);
        verify(mWaitingActionList, times(1)).remove(mAction);
        verify(mRunningActionMap, times(1)).put("test", mAction);
    }
}
