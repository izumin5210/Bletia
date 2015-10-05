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
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import info.izumin.android.bletia.action.Action;
import info.izumin.android.bletia.action.CharacteristicAction;
import info.izumin.android.bletia.action.DescriptorAction;
import info.izumin.android.bletia.action.EnableNotificationAction;
import info.izumin.android.bletia.action.ReadRemoteRssiAction;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by izumin on 10/2/15.
 */
@RunWith(AndroidJUnit4.class)
public class BluetoothGattCallbackHandlerTest {

    private BluetoothGattCallbackHandler mCallbackHandler;

    @Mock private BluetoothGattCallbackHandler.Callback mCallback;
    @Mock private BleActionStore mActionStore;
    @Mock private BluetoothGattWrapper mGattWrapper;
    @Mock private BluetoothGattCharacteristic mCharacteristic;
    @Mock private BluetoothGattDescriptor mDescriptor;
    @Mock private CharacteristicAction mCharacteristicAction;
    @Mock private DescriptorAction mDescriptorAction;
    @Mock private EnableNotificationAction mNotificationAction;
    @Mock private ReadRemoteRssiAction mRssiAction;
    @Mock private Deferred<BluetoothGattCharacteristic, BletiaException, Object> mCharacteristicDeferred;
    @Mock private Deferred<BluetoothGattDescriptor, BletiaException, Object> mDescriptorDeferred;
    @Mock private Deferred<Integer, BletiaException, Object> mIntegerDeferred;

    private ArgumentCaptor<BletiaException> mExceptionCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mCallbackHandler = new BluetoothGattCallbackHandler(mCallback, mActionStore);
        when(mCharacteristicAction.getDeferred()).thenReturn(mCharacteristicDeferred);
        when(mDescriptorAction.getDeferred()).thenReturn(mDescriptorDeferred);
        when(mNotificationAction.getDeferred()).thenReturn(mCharacteristicDeferred);
        when(mRssiAction.getDeferred()).thenReturn(mIntegerDeferred);
        when(mActionStore.dequeue(Action.Type.READ_CHARACTERISTIC)).thenReturn(mCharacteristicAction);
        when(mActionStore.dequeue(Action.Type.WRITE_CHARACTERISTIC)).thenReturn(mCharacteristicAction);
        when(mActionStore.dequeue(Action.Type.READ_DESCRIPTOR)).thenReturn(mDescriptorAction);
        when(mActionStore.dequeue(Action.Type.WRITE_DESCRIPTOR)).thenReturn(mDescriptorAction);
        when(mActionStore.dequeue(Action.Type.ENABLE_NOTIFICATION)).thenReturn(mNotificationAction);
        when(mActionStore.dequeue(Action.Type.READ_REMOTE_RSSI)).thenReturn(mRssiAction);
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
        assertThat(e.getAction()).isEqualTo(mCharacteristicAction);
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
        assertThat(e.getAction()).isEqualTo(mCharacteristicAction);
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
        assertThat(e.getAction()).isEqualTo(mDescriptorAction);
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
        assertThat(e.getAction()).isEqualTo(mDescriptorAction);
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
