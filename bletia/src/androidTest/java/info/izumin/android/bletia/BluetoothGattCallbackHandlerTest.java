package info.izumin.android.bletia;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.test.runner.AndroidJUnit4;

import org.jdeferred.Deferred;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import info.izumin.android.bletia.action.EnableNotificationAction;
import info.izumin.android.bletia.action.ReadCharacteristicAction;
import info.izumin.android.bletia.action.ReadDescriptorAction;
import info.izumin.android.bletia.action.ReadRemoteRssiAction;
import info.izumin.android.bletia.action.WriteCharacteristicAction;
import info.izumin.android.bletia.action.WriteDescriptorAction;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

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
    private ActionQueueContainer mQueueContainer;

    @Mock private BluetoothGattCallbackHandler.Callback mCallback;
    @Mock private BluetoothGattWrapper mGattWrapper;
    @Mock private BluetoothGattCharacteristic mCharacteristic;
    @Mock private BluetoothGattDescriptor mDescriptor;

    @Mock private ReadCharacteristicAction mReadCharacteristicAction;
    @Mock private WriteCharacteristicAction mWriteCharacteristicAction;
    @Mock private ReadDescriptorAction mReadDescriptorAction;
    @Mock private WriteDescriptorAction mWriteDescriptorAction;
    @Mock private EnableNotificationAction mNotificationAction;
    @Mock private ReadRemoteRssiAction mRssiAction;

    @Mock private Deferred<BluetoothGattCharacteristic, BletiaException, Void> mCharacteristicDeferred;
    @Mock private Deferred<BluetoothGattDescriptor, BletiaException, Void> mDescriptorDeferred;
    @Mock private Deferred<Integer, BletiaException, Void> mIntegerDeferred;

    private ArgumentCaptor<BletiaException> mExceptionCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mQueueContainer = Mockito.mock(ActionQueueContainer.class, Mockito.RETURNS_DEEP_STUBS);
        mCallbackHandler = new BluetoothGattCallbackHandler(mCallback, mQueueContainer);
        when(mReadCharacteristicAction.getDeferred()).thenReturn(mCharacteristicDeferred);
        when(mWriteCharacteristicAction.getDeferred()).thenReturn(mCharacteristicDeferred);
        when(mReadDescriptorAction.getDeferred()).thenReturn(mDescriptorDeferred);
        when(mWriteDescriptorAction.getDeferred()).thenReturn(mDescriptorDeferred);
        when(mNotificationAction.getDeferred()).thenReturn(mCharacteristicDeferred);
        when(mRssiAction.getDeferred()).thenReturn(mIntegerDeferred);
        when(mQueueContainer.getReadCharacteristicActionQueue().dequeue(any(UUID.class))).thenReturn(mReadCharacteristicAction);
        when(mQueueContainer.getWriteCharacteristicActionQueue().dequeue(any(UUID.class))).thenReturn(mWriteCharacteristicAction);
        when(mQueueContainer.getReadDescriptorActionQueue().dequeue(any(UUID.class))).thenReturn(mReadDescriptorAction);
        when(mQueueContainer.getWriteDescriptorActionQueue().dequeue(any(UUID.class))).thenReturn(mWriteDescriptorAction);
        when(mQueueContainer.getEnableNotificationActionQueue().dequeue(any(UUID.class))).thenReturn(mNotificationAction);
        when(mQueueContainer.getReadRemoteRssiActionQueue().dequeue(null)).thenReturn(mRssiAction);
        when(mDescriptor.getUuid()).thenReturn(UUID.randomUUID());
        when(mDescriptor.getCharacteristic()).thenReturn(mCharacteristic);
        mExceptionCaptor = ArgumentCaptor.forClass(BletiaException.class);
    }

    @Test
    public void onConnectionStateChange_WhenStatusIsSuccessAndNewStateIsConnected() throws Exception {
        mCallbackHandler.onConnectionStateChange(mGattWrapper, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED);
        verify(mCallback, times(1)).onConnect(mGattWrapper);
    }

    @Test
    public void onConnectionStateChange_WhenStatusIsSuccessAndNewStateIsDisconnected() throws Exception {
        mCallbackHandler.onConnectionStateChange(mGattWrapper, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED);
        verify(mCallback, times(1)).onDisconnect(mGattWrapper);
    }

    @Test
    public void onConnectionStateChange_WhenStatusIsFailureAndNewStateIsConnected() throws Exception {
        mCallbackHandler.onConnectionStateChange(mGattWrapper, BluetoothGatt.GATT_FAILURE, BluetoothGatt.STATE_CONNECTED);
        verify(mCallback, times(1)).onError(mExceptionCaptor.capture());
        BletiaException e = mExceptionCaptor.getValue();
        assertThat(e.getTag()).isEqualTo("onConnectionStateChange");
    }

    @Test
    public void onConnectionStateChange_WhenStatusIsFailureAndNewStateIsDisconnected() throws Exception {
        mCallbackHandler.onConnectionStateChange(mGattWrapper, BluetoothGatt.GATT_FAILURE, BluetoothGatt.STATE_DISCONNECTED);
        verify(mCallback, times(1)).onDisconnect(mGattWrapper);
    }

    @Test
    public void onServiceDiscovered_WhenStatusIsSuccess() throws Exception {
        mCallbackHandler.onServicesDiscovered(mGattWrapper, BluetoothGatt.GATT_SUCCESS);
        verify(mCallback, times(1)).onServiceDiscovered(BluetoothGatt.GATT_SUCCESS);
    }

    @Test
    public void onServiceDiscovered_WhenStatusIsFailure() throws Exception {
        mCallbackHandler.onServicesDiscovered(mGattWrapper, BluetoothGatt.GATT_FAILURE);
        verify(mCallback, times(1)).onServiceDiscovered(BluetoothGatt.GATT_FAILURE);
    }

    @Test
    public void onCharacteristicRead_WhenStatusIsSuccess() throws Exception {
        mCallbackHandler.onCharacteristicRead(mGattWrapper, mCharacteristic, BluetoothGatt.GATT_SUCCESS);
        verify(mCharacteristicDeferred, times(1)).resolve(mCharacteristic);
    }

    @Test
    public void onCharacteristicRead_WhenStatusIsFailure() throws Exception {
        mCallbackHandler.onCharacteristicRead(mGattWrapper, mCharacteristic, BluetoothGatt.GATT_FAILURE);
        verify(mCharacteristicDeferred, times(1)).reject(mExceptionCaptor.capture());
        BletiaException e = mExceptionCaptor.getValue();
        assertThat(e.getAction()).isEqualTo(mReadCharacteristicAction);
        assertThat(e.getType()).isEqualTo(BleErrorType.FAILURE);
    }

    @Test
    public void onCharacteristicWrite_WhenStatusIsSuccess() throws Exception {
        mCallbackHandler.onCharacteristicWrite(mGattWrapper, mCharacteristic, BluetoothGatt.GATT_SUCCESS);
        verify(mCharacteristicDeferred, times(1)).resolve(mCharacteristic);
    }

    @Test
    public void onCharacteristicWrite_WhenStatusIsFailure() throws Exception {
        mCallbackHandler.onCharacteristicWrite(mGattWrapper, mCharacteristic, BluetoothGatt.GATT_FAILURE);
        verify(mCharacteristicDeferred, times(1)).reject(mExceptionCaptor.capture());
        BletiaException e = mExceptionCaptor.getValue();
        assertThat(e.getAction()).isEqualTo(mWriteCharacteristicAction);
        assertThat(e.getType()).isEqualTo(BleErrorType.FAILURE);
    }

    @Test
    public void onCharacteristicChanged() throws Exception {
        mCallbackHandler.onCharacteristicChanged(mGattWrapper, mCharacteristic);
        verify(mCallback, times(1)).onCharacteristicChanged(mCharacteristic);
    }

    @Test
    public void onDescriptorRead_WhenStatusIsSuccess() throws Exception {
        mCallbackHandler.onDescriptorRead(mGattWrapper, mDescriptor, BluetoothGatt.GATT_SUCCESS);
        verify(mDescriptorDeferred, times(1)).resolve(mDescriptor);
    }

    @Test
    public void onDescriptorRead_WhenStatusIsFailure() throws Exception {
        mCallbackHandler.onDescriptorRead(mGattWrapper, mDescriptor, BluetoothGatt.GATT_FAILURE);
        verify(mDescriptorDeferred, times(1)).reject(mExceptionCaptor.capture());
        BletiaException e = mExceptionCaptor.getValue();
        assertThat(e.getAction()).isEqualTo(mReadDescriptorAction);
        assertThat(e.getType()).isEqualTo(BleErrorType.FAILURE);
    }

    @Test
    public void onDescriptorWrite_WhenStatusIsSuccess() throws Exception {
        mCallbackHandler.onDescriptorWrite(mGattWrapper, mDescriptor, BluetoothGatt.GATT_SUCCESS);
        verify(mDescriptorDeferred, times(1)).resolve(mDescriptor);
    }

    @Test
    public void onDescriptorWrite_WhenStatusIsFailure() throws Exception {
        mCallbackHandler.onDescriptorWrite(mGattWrapper, mDescriptor, BluetoothGatt.GATT_FAILURE);
        verify(mDescriptorDeferred, times(1)).reject(mExceptionCaptor.capture());
        BletiaException e = mExceptionCaptor.getValue();
        assertThat(e.getAction()).isEqualTo(mWriteDescriptorAction);
        assertThat(e.getType()).isEqualTo(BleErrorType.FAILURE);
    }

    @Test
    public void onDescriptorWrite_ThatIsNotificationDescriptor_WhenStatusIsSuccess() throws Exception {
        when(mDescriptor.getUuid()).thenReturn(Bletia.CLIENT_CHARCTERISTIC_CONFIG);
        when(mDescriptor.getValue()).thenReturn(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mCallbackHandler.onDescriptorWrite(mGattWrapper, mDescriptor, BluetoothGatt.GATT_SUCCESS);
        verify(mCharacteristicDeferred, times(1)).resolve(mCharacteristic);
    }

    @Test
    public void onDescriptorWrite_ThatIsNotificationDescriptor_WhenStatusIsFailure() throws Exception {
        when(mDescriptor.getUuid()).thenReturn(Bletia.CLIENT_CHARCTERISTIC_CONFIG);
        when(mDescriptor.getValue()).thenReturn(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mCallbackHandler.onDescriptorWrite(mGattWrapper, mDescriptor, BluetoothGatt.GATT_FAILURE);
        verify(mCharacteristicDeferred, times(1)).reject(mExceptionCaptor.capture());
        BletiaException e = mExceptionCaptor.getValue();
        assertThat(e.getAction()).isEqualTo(mNotificationAction);
        assertThat(e.getType()).isEqualTo(BleErrorType.FAILURE);
    }

    @Test
    public void onReadRemoteRssi_WhenStatusIsSuccess() throws Exception {
        mCallbackHandler.onReadRemoteRssi(mGattWrapper, 10, BluetoothGatt.GATT_SUCCESS);
        verify(mIntegerDeferred, times(1)).resolve(10);
    }

    @Test
    public void onReadRemoteRssi_WhenStatusIsFailure() throws Exception {
        mCallbackHandler.onReadRemoteRssi(mGattWrapper, 10, BluetoothGatt.GATT_FAILURE);
        verify(mIntegerDeferred, times(1)).reject(mExceptionCaptor.capture());
        BletiaException e = mExceptionCaptor.getValue();
        assertThat(e.getAction()).isEqualTo(mRssiAction);
        assertThat(e.getType()).isEqualTo(BleErrorType.FAILURE);
    }
}
