package info.izumin.android.bletia.core.action;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import info.izumin.android.bletia.core.BleErrorType;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.ActionResolver;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by izumin on 11/12/15.
 */
@RunWith(AndroidJUnit4.class)
public class AbstractEnableNotificationActionTest {
    public static final String TAG = AbstractEnableNotificationActionTest.class.getSimpleName();

    class ActionImpl extends AbstractEnableNotificationAction {
        public ActionImpl(BluetoothGattCharacteristic characteristic, boolean enable, ActionResolver<BluetoothGattCharacteristic, BletiaException> actionResolver) {
            super(characteristic, enable, actionResolver);
        }
    }

    @Mock private BluetoothGattWrapper mGattWrapper;
    @Mock private BluetoothGattCharacteristic mCharacteristic;
    @Mock private ActionResolver<BluetoothGattCharacteristic, BletiaException> mResolver;

    private ActionImpl mAction;
    private ArgumentCaptor<BletiaException> mExceptionCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mAction = new ActionImpl(mCharacteristic, true, mResolver);
        mExceptionCaptor = ArgumentCaptor.forClass(BletiaException.class);
    }

    @Test
    public void executeWhenSetCharacteristicNotificationReturnsFalse() throws Exception {
        when(mGattWrapper.setCharacteristicNotification(any(BluetoothGattCharacteristic.class), anyBoolean())).thenReturn(false);
        assertThat(mAction.execute(mGattWrapper)).isFalse();
        verify(mResolver, times(1)).reject(mExceptionCaptor.capture());
        assertThat(mExceptionCaptor.getValue().getType()).isEqualTo(BleErrorType.REQUEST_FAILURE);
    }

    @Test
    public void executeWhenWriteDescriptorReturnsFalse() throws Exception {
        when(mGattWrapper.setCharacteristicNotification(any(BluetoothGattCharacteristic.class), anyBoolean())).thenReturn(true);
        when(mGattWrapper.writeDescriptor(any(BluetoothGattDescriptor.class))).thenReturn(false);
        assertThat(mAction.execute(mGattWrapper)).isFalse();
        verify(mResolver, times(1)).reject(mExceptionCaptor.capture());
        assertThat(mExceptionCaptor.getValue().getType()).isEqualTo(BleErrorType.OPERATION_INITIATED_FAILURE);
    }

    @Test
    public void executeWhenWriteDescriptorReturnsTrue() throws Exception {
        when(mGattWrapper.setCharacteristicNotification(any(BluetoothGattCharacteristic.class), anyBoolean())).thenReturn(true);
        when(mGattWrapper.writeDescriptor(any(BluetoothGattDescriptor.class))).thenReturn(true);
        assertThat(mAction.execute(mGattWrapper)).isTrue();
        verify(mResolver, never()).reject(any(BletiaException.class));
    }
}

