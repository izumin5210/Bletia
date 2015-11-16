package info.izumin.android.bletia.core.action;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import info.izumin.android.bletia.core.BleState;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.BluetoothGattCallbackHandler;
import info.izumin.android.bletia.core.ResolveStrategy;
import info.izumin.android.bletia.core.StateContainer;
import info.izumin.android.bletia.core.wrapper.BluetoothDeviceWrapper;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by izumin on 11/14/15.
 */
@RunWith(AndroidJUnit4.class)
public class AbstractConnectActionTest extends AndroidTestCase {
    public static final String TAG = AbstractConnectActionTest.class.getSimpleName();

    class ActionImpl extends AbstractConnectAction {
        public ActionImpl(ResolveStrategy<Void, BletiaException, Void> resolveStrategy, BluetoothDeviceWrapper device, StateContainer container) {
            super(resolveStrategy, device, container);
        }
    }

    @Mock private ResolveStrategy<Void, BletiaException, Void> mStrategy;
    @Mock private BluetoothDeviceWrapper mDevice;
    @Mock private StateContainer mContainer;
    @Mock private BluetoothGattCallbackHandler mCallbackHandler;

    private ActionImpl mAction;
    private Context mContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mDevice.getName()).thenReturn("testdevice");
        when(mContainer.getContext()).thenReturn(mContext);
        when(mContainer.getCallbackHandler()).thenReturn(mCallbackHandler);
        mAction = new ActionImpl(mStrategy, mDevice, mContainer);
        mContext = getContext();
    }

    @Test
    public void execute() throws Exception {
        mAction.execute(null);
        verify(mContainer, times(1)).setState(BleState.CONNECTING);
        verify(mDevice, times(1)).connectGatt(mContext, false, mCallbackHandler);
        verify(mContainer, times(1)).setGattWrapper(any(BluetoothGattWrapper.class));
    }
}
