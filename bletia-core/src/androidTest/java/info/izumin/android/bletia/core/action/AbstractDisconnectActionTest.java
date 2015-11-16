package info.izumin.android.bletia.core.action;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import info.izumin.android.bletia.core.BleState;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.ResolveStrategy;
import info.izumin.android.bletia.core.StateContainer;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by izumin on 11/14/15.
 */
@RunWith(AndroidJUnit4.class)
public class AbstractDisconnectActionTest {
    public static final String TAG = AbstractDisconnectActionTest.class.getSimpleName();

    class ActionImpl extends AbstractDisconnectAction {
        public ActionImpl(ResolveStrategy<Void, BletiaException, Void> resolveStrategy, StateContainer container) {
            super(resolveStrategy, container);
        }
    }

    @Mock private ResolveStrategy<Void, BletiaException, Void> mStrategy;
    @Mock private BluetoothGattWrapper mGattWrapper;
    @Mock private StateContainer mContainer;

    private ActionImpl mAction;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mAction = new ActionImpl(mStrategy, mContainer);
    }

    @Test
    public void execute() throws Exception {
        mAction.execute(mGattWrapper);
        verify(mContainer, times(1)).setState(BleState.DISCONNECTING);
        verify(mGattWrapper, times(1)).disconnect();
    }
}
