package info.izumin.android.bletia;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;
import android.os.HandlerThread;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import info.izumin.android.bletia.action.EnableNotificationAction;
import info.izumin.android.bletia.action.ReadCharacteristicAction;
import info.izumin.android.bletia.action.ReadDescriptorAction;
import info.izumin.android.bletia.action.ReadRemoteRssiAction;
import info.izumin.android.bletia.action.WriteCharacteristicAction;
import info.izumin.android.bletia.action.WriteDescriptorAction;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by izumin on 9/8/15.
 */
@RunWith(AndroidJUnit4.class)
public class BletiaTest extends AndroidTestCase {

    @Mock private BluetoothGattCharacteristic mCharacteristic;
    @Mock private BluetoothGattDescriptor mDescriptor;
    @Mock private BluetoothGattWrapper mBluetoothGattWrapper;

    private ActionQueueContainer mQueueContainer;
    private BleMessageThread mMessageThread;
    private BluetoothGattCallbackHandler mCallbackHandler;
    private Bletia mBletia;
    private Context mContext;

    private CountDownLatch mLatch;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mContext = getContext();
        mBletia = new Bletia(mContext);

        mQueueContainer = (ActionQueueContainer) Whitebox.getInternalState(mBletia, "mQueueContainer");
        mCallbackHandler = (BluetoothGattCallbackHandler) Whitebox.getInternalState(mBletia, "mCallbackHandler");
        HandlerThread thread = new HandlerThread("test");
        thread.start();
        mMessageThread = new BleMessageThread(thread, mBluetoothGattWrapper, mQueueContainer);
        Whitebox.setInternalState(mBletia, "mMessageThread", mMessageThread);
        Whitebox.setInternalState(mBletia, "mGattWrapper", mBluetoothGattWrapper);

        when(mCharacteristic.getUuid()).thenReturn(UUID.randomUUID());
        when(mDescriptor.getUuid()).thenReturn(UUID.randomUUID());
        when(mBluetoothGattWrapper.writeCharacteristic(mCharacteristic)).thenReturn(true);
        when(mBluetoothGattWrapper.readCharacteristic(mCharacteristic)).thenReturn(true);
        when(mBluetoothGattWrapper.writeDescriptor(mDescriptor)).thenReturn(true);
        when(mBluetoothGattWrapper.readDescriptor(mDescriptor)).thenReturn(true);
        when(mBluetoothGattWrapper.setCharacteristicNotification(eq(mCharacteristic), anyBoolean())).thenReturn(true);

        final ArgumentCaptor<UUID> uuidCaptor = ArgumentCaptor.forClass(UUID.class);
        final ArgumentCaptor<byte[]> valueCaptor = ArgumentCaptor.forClass(byte[].class);
        when(mCharacteristic.getDescriptor(uuidCaptor.capture())).thenAnswer(new Answer<BluetoothGattDescriptor>() {
            @Override
            public BluetoothGattDescriptor answer(InvocationOnMock invocation) throws Throwable {
                when(mDescriptor.getUuid()).thenReturn(uuidCaptor.getValue());
                when(mDescriptor.getCharacteristic()).thenReturn(mCharacteristic);
                return mDescriptor;
            }
        });
        when(mDescriptor.setValue(valueCaptor.capture())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                when(mDescriptor.getValue()).thenReturn(valueCaptor.getValue());
                return true;
            }
        });

        mLatch = new CountDownLatch(1);
    }

    @Override
    public void tearDown() throws Exception {
        mMessageThread.stop();
    }

    @Test
    public void writeCharacteristicSuccessfully() throws Exception {
        mBletia.writeCharacteristic(mCharacteristic)
                .then(new DoneCallback<BluetoothGattCharacteristic>() {
                    @Override
                    public void onDone(BluetoothGattCharacteristic result) {
                        assertThat(result.getUuid()).isEqualTo(mCharacteristic.getUuid());
                        mLatch.countDown();
                    }
                }).fail(mNeverCalledFailCallback);

        Thread.sleep(300);
        mCallbackHandler.onCharacteristicWrite(
                mBluetoothGattWrapper, mCharacteristic, BluetoothGatt.GATT_SUCCESS);
        await();
    }

    @Test
    public void writeCharacteristicFailure() throws Exception {
        mBletia.writeCharacteristic(mCharacteristic)
                .done(mNeverCalledDoneCallback)
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        assertThat(result.getType()).isEqualTo(BleErrorType.FAILURE);
                        assertThat(result.getAction()).isInstanceOf(WriteCharacteristicAction.class);
                        mLatch.countDown();
                    }
                });

        Thread.sleep(300);
        mCallbackHandler.onCharacteristicWrite(
                mBluetoothGattWrapper, mCharacteristic, BluetoothGatt.GATT_FAILURE);
        await();
    }

    @Test
    public void writeCharacteristicWhenOperationIsInitiatedFailure() throws Exception {
        when(mBluetoothGattWrapper.writeCharacteristic(any(BluetoothGattCharacteristic.class))).thenReturn(false);
        mBletia.writeCharacteristic(mCharacteristic)
                .done(mNeverCalledDoneCallback)
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        assertThat(result.getType()).isEqualTo(BleErrorType.OPERATION_INITIATED_FAILURE);
                        assertThat(result.getAction()).isInstanceOf(WriteCharacteristicAction.class);
                        mLatch.countDown();
                    }
                });
        await();
    }

    @Test
    public void readCharacteristicSuccessfully() throws Exception {
        mBletia.readCharacteristic(mCharacteristic)
                .then(new DoneCallback<BluetoothGattCharacteristic>() {
                    @Override
                    public void onDone(BluetoothGattCharacteristic result) {
                        assertThat(result.getUuid()).isEqualTo(mCharacteristic.getUuid());
                        mLatch.countDown();
                    }
                }).fail(mNeverCalledFailCallback);

        Thread.sleep(300);
        mCallbackHandler.onCharacteristicRead(
                mBluetoothGattWrapper, mCharacteristic, BluetoothGatt.GATT_SUCCESS);
        await();
    }

    @Test
    public void readCharacteristicFailure() throws Exception {
        mBletia.readCharacteristic(mCharacteristic)
                .done(mNeverCalledDoneCallback)
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        assertThat(result.getType()).isEqualTo(BleErrorType.FAILURE);
                        assertThat(result.getAction()).isInstanceOf(ReadCharacteristicAction.class);
                        mLatch.countDown();
                    }
                });

        Thread.sleep(300);
        mCallbackHandler.onCharacteristicRead(
                mBluetoothGattWrapper, mCharacteristic, BluetoothGatt.GATT_FAILURE);
        await();
    }

    @Test
    public void readCharacteristicWhenOperationIsInitiatedFailure() throws Exception {
        when(mBluetoothGattWrapper.readCharacteristic(any(BluetoothGattCharacteristic.class))).thenReturn(false);
        mBletia.readCharacteristic(mCharacteristic)
                .done(mNeverCalledDoneCallback)
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        assertThat(result.getType()).isEqualTo(BleErrorType.OPERATION_INITIATED_FAILURE);
                        assertThat(result.getAction()).isInstanceOf(ReadCharacteristicAction.class);
                        mLatch.countDown();
                    }
                });
        await();
    }

    public void writeDescriptorSuccessfully() throws Exception {
        mBletia.writeDescriptor(mDescriptor).then(new DoneCallback<BluetoothGattDescriptor>() {
            @Override
            public void onDone(BluetoothGattDescriptor result) {
                assertThat(result.getUuid()).isEqualTo(mDescriptor.getUuid());
                mLatch.countDown();
            }
        }).fail(mNeverCalledFailCallback);

        Thread.sleep(300);
        mCallbackHandler.onDescriptorWrite(
                mBluetoothGattWrapper, mDescriptor, BluetoothGatt.GATT_SUCCESS);
        await();
    }

    @Test
    public void writeDescriptorFailure() throws Exception {
        mBletia.writeDescriptor(mDescriptor)
                .done(mNeverCalledDoneCallback)
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        assertThat(result.getType()).isEqualTo(BleErrorType.FAILURE);
                        assertThat(result.getAction()).isInstanceOf(WriteDescriptorAction.class);
                        mLatch.countDown();
                    }
                });

        Thread.sleep(300);
        mCallbackHandler.onDescriptorWrite(
                mBluetoothGattWrapper, mDescriptor, BluetoothGatt.GATT_FAILURE);
        await();
    }

    @Test
    public void writeDescriptorWhenOperationIsInitiatedFailure() throws Exception {
        when(mBluetoothGattWrapper.writeDescriptor(any(BluetoothGattDescriptor.class))).thenReturn(false);
        mBletia.writeDescriptor(mDescriptor)
                .done(mNeverCalledDoneCallback)
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        assertThat(result.getType()).isEqualTo(BleErrorType.OPERATION_INITIATED_FAILURE);
                        assertThat(result.getAction()).isInstanceOf(WriteDescriptorAction.class);
                        mLatch.countDown();
                    }
                });
        await();
    }

    @Test
    public void readDescriptorSuccessfully() throws Exception {
        mBletia.readDescriptor(mDescriptor)
                .then(new DoneCallback<BluetoothGattDescriptor>() {
                    @Override
                    public void onDone(BluetoothGattDescriptor result) {
                        assertThat(result.getUuid()).isEqualTo(mDescriptor.getUuid());
                        mLatch.countDown();
                    }
                })
                .fail(mNeverCalledFailCallback);

        Thread.sleep(300);
        mCallbackHandler.onDescriptorRead(
                mBluetoothGattWrapper, mDescriptor, BluetoothGatt.GATT_SUCCESS);
        await();
    }

    @Test
    public void readDescriptorFailure() throws Exception {
        mBletia.readDescriptor(mDescriptor)
                .done(mNeverCalledDoneCallback)
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        assertThat(result.getType()).isEqualTo(BleErrorType.FAILURE);
                        assertThat(result.getAction()).isInstanceOf(ReadDescriptorAction.class);
                        mLatch.countDown();
                    }
                });

        Thread.sleep(300);
        mCallbackHandler.onDescriptorRead(
                mBluetoothGattWrapper, mDescriptor, BluetoothGatt.GATT_FAILURE);
        await();
    }

    @Test
    public void readDescriptorWhenOperationIsInitiatedFailure() throws Exception {
        when(mBluetoothGattWrapper.readDescriptor(any(BluetoothGattDescriptor.class))).thenReturn(false);
        mBletia.readDescriptor(mDescriptor)
                .done(mNeverCalledDoneCallback)
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        assertThat(result.getType()).isEqualTo(BleErrorType.OPERATION_INITIATED_FAILURE);
                        assertThat(result.getAction()).isInstanceOf(ReadDescriptorAction.class);
                        mLatch.countDown();
                    }
                });
        await();
    }

    @Test
    public void enableNotificationSuccessfully() throws Exception {
        mBletia.enableNotification(mCharacteristic, true)
                .done(new DoneCallback<BluetoothGattCharacteristic>() {
                    @Override
                    public void onDone(BluetoothGattCharacteristic result) {
                        mLatch.countDown();
                    }
                })
                .fail(mNeverCalledFailCallback);

        Thread.sleep(300);
        mCallbackHandler.onDescriptorWrite(mBluetoothGattWrapper, mDescriptor, BluetoothGatt.GATT_SUCCESS);
        await();
    }

    @Test
    public void enableNotificationWhenRequestFailure() throws Exception {
        when(mBluetoothGattWrapper.setCharacteristicNotification(eq(mCharacteristic), anyBoolean())).thenReturn(false);
        mBletia.enableNotification(mCharacteristic, true)
                .done(mNeverCalledDoneCallback)
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        assertThat(result.getType()).isEqualTo(BleErrorType.REQUEST_FAILURE);
                        assertThat(result.getAction()).isInstanceOf(EnableNotificationAction.class);
                        mLatch.countDown();
                    }
                });
        await();
    }

    @Test
    public void enableNotificationWhenOperationInitiatedFailure() throws Exception {
        when(mBluetoothGattWrapper.writeDescriptor(mDescriptor)).thenReturn(false);
        mBletia.enableNotification(mCharacteristic, true)
                .done(mNeverCalledDoneCallback)
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        assertThat(result.getType()).isEqualTo(BleErrorType.OPERATION_INITIATED_FAILURE);
                        assertThat(result.getAction()).isInstanceOf(EnableNotificationAction.class);
                        mLatch.countDown();
                    }
                });
        await();
    }

    @Test
    public void enableNotificationWhenOperationFailure() throws Exception {
        mBletia.enableNotification(mCharacteristic, true)
                .done(mNeverCalledDoneCallback)
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        assertThat(result.getType()).isEqualTo(BleErrorType.FAILURE);
                        assertThat(result.getAction()).isInstanceOf(EnableNotificationAction.class);
                        mLatch.countDown();
                    }
                });

        Thread.sleep(300);
        verify(mBluetoothGattWrapper, times(1)).writeDescriptor(mDescriptor);
        mCallbackHandler.onDescriptorWrite(
                mBluetoothGattWrapper, mDescriptor, BluetoothGatt.GATT_FAILURE);
        await();
    }

    @Test
    public void disableNotificationSuccessfully() throws Exception {
        mBletia.enableNotification(mCharacteristic, false)
                .done(new DoneCallback<BluetoothGattCharacteristic>() {
                    @Override
                    public void onDone(BluetoothGattCharacteristic result) {
                        mLatch.countDown();
                    }
                })
                .fail(mNeverCalledFailCallback);

        Thread.sleep(300);
        verify(mBluetoothGattWrapper, times(1)).writeDescriptor(mDescriptor);
        mCallbackHandler.onDescriptorWrite(mBluetoothGattWrapper, mDescriptor, BluetoothGatt.GATT_SUCCESS);
        await();
    }

    @Test
    public void disableNotificationWhenRequestFailure() throws Exception {
        when(mBluetoothGattWrapper.setCharacteristicNotification(eq(mCharacteristic), anyBoolean())).thenReturn(false);
        mBletia.enableNotification(mCharacteristic, false)
                .done(mNeverCalledDoneCallback)
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        assertThat(result.getType()).isEqualTo(BleErrorType.REQUEST_FAILURE);
                        assertThat(result.getAction()).isInstanceOf(EnableNotificationAction.class);
                        mLatch.countDown();
                        mLatch.countDown();
                    }
                });
        await();
    }

    @Test
    public void disableNotificationWhenOperationInitiatedFailure() throws Exception {
        when(mBluetoothGattWrapper.writeDescriptor(mDescriptor)).thenReturn(false);
        mBletia.enableNotification(mCharacteristic, false)
                .done(mNeverCalledDoneCallback)
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        assertThat(result.getType()).isEqualTo(BleErrorType.OPERATION_INITIATED_FAILURE);
                        assertThat(result.getAction()).isInstanceOf(EnableNotificationAction.class);
                        mLatch.countDown();
                    }
                });
        await();
    }

    @Test
    public void disableNotificationWhenOperationFailure() throws Exception {
        mBletia.enableNotification(mCharacteristic, false)
                .done(mNeverCalledDoneCallback)
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        assertThat(result.getType()).isEqualTo(BleErrorType.FAILURE);
                        assertThat(result.getAction()).isInstanceOf(EnableNotificationAction.class);
                        mLatch.countDown();
                    }
                });

        Thread.sleep(300);
        verify(mBluetoothGattWrapper, times(1)).writeDescriptor(mDescriptor);
        mCallbackHandler.onDescriptorWrite(
                mBluetoothGattWrapper, mDescriptor, BluetoothGatt.GATT_FAILURE);
        await();
    }

    @Test
    public void readRemoteRssiSuccessfully() throws Exception {
        when(mBluetoothGattWrapper.readRemoteRssi()).thenReturn(true);
        mBletia.readRemoteRssi()
                .done(new DoneCallback<Integer>() {
                    @Override
                    public void onDone(Integer result) {
                        assertThat(result).isEqualTo(100);
                        mLatch.countDown();
                    }
                })
                .fail(mNeverCalledFailCallback);

        Thread.sleep(300);
        mCallbackHandler.onReadRemoteRssi(
                mBluetoothGattWrapper, 100, BluetoothGatt.GATT_SUCCESS);
        await();
    }

    @Test
    public void readRemoteRssiRequestFailure() throws Exception {
        when(mBluetoothGattWrapper.readRemoteRssi()).thenReturn(false);
        mBletia.readRemoteRssi()
                .done(mNeverCalledDoneCallback)
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        assertThat(result.getType()).isEqualTo(BleErrorType.REQUEST_FAILURE);
                        assertThat(result.getAction()).isInstanceOf(ReadRemoteRssiAction.class);
                        mLatch.countDown();
                    }
                });

        await();
    }

    @Test
    public void readRemoteRssiOperationFailure() throws Exception {
        when(mBluetoothGattWrapper.readRemoteRssi()).thenReturn(true);
        mBletia.readRemoteRssi()
                .done(mNeverCalledDoneCallback)
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        assertThat(result.getType()).isEqualTo(BleErrorType.FAILURE);
                        assertThat(result.getAction()).isInstanceOf(ReadRemoteRssiAction.class);
                        mLatch.countDown();
                    }
                });

        Thread.sleep(300);
        mCallbackHandler.onReadRemoteRssi(
                mBluetoothGattWrapper, 100, BluetoothGatt.GATT_FAILURE);
        await();
    }

    private void await() throws InterruptedException {
        boolean res = mLatch.await(1000, TimeUnit.MILLISECONDS);
        assertThat(res).isTrue();
    }

    private DoneCallback mNeverCalledDoneCallback = new DoneCallback() {
        @Override public void onDone(Object result) { fail(); }
    };

    private FailCallback mNeverCalledFailCallback = new FailCallback() {
        @Override public void onFail(Object result) { fail(); }
    };
}
