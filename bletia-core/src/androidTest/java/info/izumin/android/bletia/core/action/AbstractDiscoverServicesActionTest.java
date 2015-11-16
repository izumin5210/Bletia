package info.izumin.android.bletia.core.action;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import info.izumin.android.bletia.core.BleErrorType;
import info.izumin.android.bletia.core.BleState;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.ResolveStrategy;
import info.izumin.android.bletia.core.StateContainer;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by izumin on 11/14/15.
 */
@RunWith(AndroidJUnit4.class)
public class AbstractDiscoverServicesActionTest {
    public static final String TAG = AbstractDiscoverServicesActionTest.class.getSimpleName();

    class ActionImpl extends AbstractDiscoverServicesAction {
        public ActionImpl(ResolveStrategy<Void, BletiaException, Void> resolveStrategy, StateContainer container) {
            super(resolveStrategy, container);
        }
    }

    @Mock private ResolveStrategy<Void, BletiaException, Void> mStrategy;
    @Mock private BluetoothGattWrapper mGattWrapper;
    @Mock private StateContainer mContainer;

    private ActionImpl mAction;
    private ArgumentCaptor<BletiaException> mExceptionCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mAction = new ActionImpl(mStrategy, mContainer);
        mExceptionCaptor = ArgumentCaptor.forClass(BletiaException.class);
    }

    @Test
    public void executeWhenDiscoverServicesReturnsFalse() throws Exception {
        when(mGattWrapper.discoverServices()).thenReturn(false);
        mAction.execute(mGattWrapper);
        verify(mStrategy, times(1)).reject(mExceptionCaptor.capture());
        assertThat(mExceptionCaptor.getValue().getType()).isEqualTo(BleErrorType.REQUEST_FAILURE);
    }

    @Test
    public void executeWhenDiscoverServicesReturnsTrue() throws Exception {
        when(mGattWrapper.discoverServices()).thenReturn(true);
        mAction.execute(mGattWrapper);
        verify(mContainer, times(1)).setState(BleState.SERVICE_DISCOVERING);
    }
}
