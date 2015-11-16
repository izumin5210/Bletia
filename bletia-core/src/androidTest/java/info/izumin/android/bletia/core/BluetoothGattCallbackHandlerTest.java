package info.izumin.android.bletia.core;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import info.izumin.android.bletia.core.action.AbstractConnectAction;
import info.izumin.android.bletia.core.action.AbstractDisconnectAction;
import info.izumin.android.bletia.core.action.AbstractDiscoverServicesAction;
import info.izumin.android.bletia.core.action.AbstractEnableNotificationAction;
import info.izumin.android.bletia.core.action.AbstractReadCharacteristicAction;
import info.izumin.android.bletia.core.action.AbstractReadDescriptorAction;
import info.izumin.android.bletia.core.action.AbstractReadRemoteRssiAction;
import info.izumin.android.bletia.core.action.AbstractWriteCharacteristicAction;
import info.izumin.android.bletia.core.action.AbstractWriteDescriptorAction;
import info.izumin.android.bletia.core.util.NotificationUtils;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by izumin on 10/2/15.
 */
@RunWith(AndroidJUnit4.class)
public class BluetoothGattCallbackHandlerTest {

    private BluetoothGattCallbackHandler mCallbackHandler;
    private StateContainer mContainer;

    @Mock private AbstractBletia.BleEventListener mListener;
    @Mock private BluetoothGattWrapper mGattWrapper;
    @Mock private BluetoothGattCharacteristic mCharacteristic;
    @Mock private BluetoothGattDescriptor mDescriptor;

    @Mock private AbstractReadCharacteristicAction mReadCharacteristicAction;
    @Mock private AbstractWriteCharacteristicAction mWriteCharacteristicAction;
    @Mock private AbstractReadDescriptorAction mReadDescriptorAction;
    @Mock private AbstractWriteDescriptorAction mWriteDescriptorAction;
    @Mock private AbstractEnableNotificationAction mNotificationAction;
    @Mock private AbstractReadRemoteRssiAction mRssiAction;

    @Mock private AbstractConnectAction mConnectAction;
    @Mock private AbstractDiscoverServicesAction mDiscoverServicesAction;
    @Mock private AbstractDisconnectAction mDisconnectAction;

    private ArgumentCaptor<BletiaException> mExceptionCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mContainer = Mockito.mock(StateContainer.class, Mockito.RETURNS_DEEP_STUBS);
        mCallbackHandler = new BluetoothGattCallbackHandler(mListener, mContainer);
        when(mContainer.getReadCharacteristicActionQueue().dequeue(any(UUID.class))).thenReturn(mReadCharacteristicAction);
        when(mContainer.getWriteCharacteristicActionQueue().dequeue(any(UUID.class))).thenReturn(mWriteCharacteristicAction);
        when(mContainer.getReadDescriptorActionQueue().dequeue(any(UUID.class))).thenReturn(mReadDescriptorAction);
        when(mContainer.getWriteDescriptorActionQueue().dequeue(any(UUID.class))).thenReturn(mWriteDescriptorAction);
        when(mContainer.getEnableNotificationActionQueue().dequeue(any(UUID.class))).thenReturn(mNotificationAction);
        when(mContainer.getReadRemoteRssiActionQueue().dequeue(null)).thenReturn(mRssiAction);
        when(mContainer.getConnectActionQueue().dequeue(null)).thenReturn(mConnectAction);
        when(mContainer.getDiscoverServicesActionQueue().dequeue(null)).thenReturn(mDiscoverServicesAction);
        when(mContainer.getDisconnectActionQueue().dequeue(null)).thenReturn(mDisconnectAction);
        when(mDescriptor.getUuid()).thenReturn(UUID.randomUUID());
        when(mDescriptor.getCharacteristic()).thenReturn(mCharacteristic);
        mExceptionCaptor = ArgumentCaptor.forClass(BletiaException.class);
    }

    @Test
    public void onConnectionStateChange_WhenStatusIsSuccessAndNewStateIsConnected() throws Exception {
        mCallbackHandler.onConnectionStateChange(mGattWrapper, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        verify(mConnectAction, times(1)).resolve(null);
    }

    @Test
    public void onConnectionStateChange_WhenStatusIsSuccessAndNewStateIsDisconnected() throws Exception {
        mCallbackHandler.onConnectionStateChange(mGattWrapper, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);
        verify(mDisconnectAction, times(1)).resolve(null);
    }

    @Test
    public void onConnectionStateChange_WhenStatusIsFailureAndNewStateIsConnected() throws Exception {
        mCallbackHandler.onConnectionStateChange(mGattWrapper, BluetoothGatt.GATT_FAILURE, BluetoothGatt.STATE_CONNECTED);
        verify(mConnectAction, times(1)).reject(mExceptionCaptor.capture());
        BletiaException e = mExceptionCaptor.getValue();
        assertThat(e.getType()).isEqualTo(BleErrorType.FAILURE);
        assertThat(e.getAction()).isEqualTo(mConnectAction);
    }

    @Test
    public void onConnectionStateChange_WhenStatusIsFailureAndNewStateIsDisconnected() throws Exception {
        mCallbackHandler.onConnectionStateChange(mGattWrapper, BluetoothGatt.GATT_FAILURE, BluetoothGatt.STATE_DISCONNECTED);
        verify(mDisconnectAction, times(1)).reject(mExceptionCaptor.capture());
        BletiaException e = mExceptionCaptor.getValue();
        assertThat(e.getType()).isEqualTo(BleErrorType.FAILURE);
        assertThat(e.getAction()).isEqualTo(mDisconnectAction);
    }

    @Test
    public void onServiceDiscovered_WhenStatusIsSuccess() throws Exception {
        mCallbackHandler.onServicesDiscovered(mGattWrapper, BluetoothGatt.GATT_SUCCESS);
        verify(mContainer, times(1)).setState(BleState.SERVICE_DISCOVERED);
        verify(mDiscoverServicesAction, times(1)).resolve(null);
    }

    @Test
    public void onServiceDiscovered_WhenStatusIsFailure() throws Exception {
        mCallbackHandler.onServicesDiscovered(mGattWrapper, BluetoothGatt.GATT_FAILURE);
        verify(mDiscoverServicesAction, times(1)).reject(mExceptionCaptor.capture());
        BletiaException e = mExceptionCaptor.getValue();
        assertThat(e.getType()).isEqualTo(BleErrorType.FAILURE);
        assertThat(e.getAction()).isEqualTo(mDiscoverServicesAction);
    }

    @Test
    public void onCharacteristicRead_WhenStatusIsSuccess() throws Exception {
        mCallbackHandler.onCharacteristicRead(mGattWrapper, mCharacteristic, BluetoothGatt.GATT_SUCCESS);
        verify(mReadCharacteristicAction, times(1)).resolve(mCharacteristic);
    }

    @Test
    public void onCharacteristicRead_WhenStatusIsFailure() throws Exception {
        mCallbackHandler.onCharacteristicRead(mGattWrapper, mCharacteristic, BluetoothGatt.GATT_FAILURE);
        verify(mReadCharacteristicAction, times(1)).reject(mExceptionCaptor.capture());
        BletiaException e = mExceptionCaptor.getValue();
        assertThat(e.getAction()).isEqualTo(mReadCharacteristicAction);
        assertThat(e.getType()).isEqualTo(BleErrorType.FAILURE);
    }

    @Test
    public void onCharacteristicWrite_WhenStatusIsSuccess() throws Exception {
        mCallbackHandler.onCharacteristicWrite(mGattWrapper, mCharacteristic, BluetoothGatt.GATT_SUCCESS);
        verify(mWriteCharacteristicAction, times(1)).resolve(mCharacteristic);
    }

    @Test
    public void onCharacteristicWrite_WhenStatusIsFailure() throws Exception {
        mCallbackHandler.onCharacteristicWrite(mGattWrapper, mCharacteristic, BluetoothGatt.GATT_FAILURE);
        verify(mWriteCharacteristicAction, times(1)).reject(mExceptionCaptor.capture());
        BletiaException e = mExceptionCaptor.getValue();
        assertThat(e.getAction()).isEqualTo(mWriteCharacteristicAction);
        assertThat(e.getType()).isEqualTo(BleErrorType.FAILURE);
    }

    @Test
    public void onCharacteristicChanged() throws Exception {
        mCallbackHandler.onCharacteristicChanged(mGattWrapper, mCharacteristic);
        verify(mListener, times(1)).onCharacteristicChanged(mCharacteristic);
    }

    @Test
    public void onDescriptorRead_WhenStatusIsSuccess() throws Exception {
        mCallbackHandler.onDescriptorRead(mGattWrapper, mDescriptor, BluetoothGatt.GATT_SUCCESS);
        verify(mReadDescriptorAction, times(1)).resolve(mDescriptor);
    }

    @Test
    public void onDescriptorRead_WhenStatusIsFailure() throws Exception {
        mCallbackHandler.onDescriptorRead(mGattWrapper, mDescriptor, BluetoothGatt.GATT_FAILURE);
        verify(mReadDescriptorAction, times(1)).reject(mExceptionCaptor.capture());
        BletiaException e = mExceptionCaptor.getValue();
        assertThat(e.getAction()).isEqualTo(mReadDescriptorAction);
        assertThat(e.getType()).isEqualTo(BleErrorType.FAILURE);
    }

    @Test
    public void onDescriptorWrite_WhenStatusIsSuccess() throws Exception {
        mCallbackHandler.onDescriptorWrite(mGattWrapper, mDescriptor, BluetoothGatt.GATT_SUCCESS);
        verify(mWriteDescriptorAction, times(1)).resolve(mDescriptor);
    }

    @Test
    public void onDescriptorWrite_WhenStatusIsFailure() throws Exception {
        mCallbackHandler.onDescriptorWrite(mGattWrapper, mDescriptor, BluetoothGatt.GATT_FAILURE);
        verify(mWriteDescriptorAction, times(1)).reject(mExceptionCaptor.capture());
        BletiaException e = mExceptionCaptor.getValue();
        assertThat(e.getAction()).isEqualTo(mWriteDescriptorAction);
        assertThat(e.getType()).isEqualTo(BleErrorType.FAILURE);
    }

    @Test
    public void onDescriptorWrite_ThatIsNotificationDescriptor_WhenStatusIsSuccess() throws Exception {
        when(mDescriptor.getUuid()).thenReturn(NotificationUtils.DESCRIPTOR_UUID);
        when(mDescriptor.getValue()).thenReturn(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mCallbackHandler.onDescriptorWrite(mGattWrapper, mDescriptor, BluetoothGatt.GATT_SUCCESS);
        verify(mNotificationAction, times(1)).resolve(mCharacteristic);
    }

    @Test
    public void onDescriptorWrite_ThatIsNotificationDescriptor_WhenStatusIsFailure() throws Exception {
        when(mDescriptor.getUuid()).thenReturn(NotificationUtils.DESCRIPTOR_UUID);
        when(mDescriptor.getValue()).thenReturn(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mCallbackHandler.onDescriptorWrite(mGattWrapper, mDescriptor, BluetoothGatt.GATT_FAILURE);
        verify(mNotificationAction, times(1)).reject(mExceptionCaptor.capture());
        BletiaException e = mExceptionCaptor.getValue();
        assertThat(e.getAction()).isEqualTo(mNotificationAction);
        assertThat(e.getType()).isEqualTo(BleErrorType.FAILURE);
    }

    @Test
    public void onReadRemoteRssi_WhenStatusIsSuccess() throws Exception {
        mCallbackHandler.onReadRemoteRssi(mGattWrapper, 10, BluetoothGatt.GATT_SUCCESS);
        verify(mRssiAction, times(1)).resolve(10);
    }

    @Test
    public void onReadRemoteRssi_WhenStatusIsFailure() throws Exception {
        mCallbackHandler.onReadRemoteRssi(mGattWrapper, 10, BluetoothGatt.GATT_FAILURE);
        verify(mRssiAction, times(1)).reject(mExceptionCaptor.capture());
        BletiaException e = mExceptionCaptor.getValue();
        assertThat(e.getAction()).isEqualTo(mRssiAction);
        assertThat(e.getType()).isEqualTo(BleErrorType.FAILURE);
    }
}
