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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by izumin on 9/8/15.
 */
@RunWith(AndroidJUnit4.class)
public class BletiaTest extends AndroidTestCase {

    @Mock private BluetoothGattCharacteristic mCharacteristic;
    @Mock private BluetoothGattDescriptor mDescriptor;
    @Mock private BluetoothGattWrapper mBluetoothGattWrapper;

    private BleEventStore mEventStore;
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

        mEventStore = (BleEventStore) Whitebox.getInternalState(mBletia, "mEventStore");
        mCallbackHandler = (BluetoothGattCallbackHandler) Whitebox.getInternalState(mBletia, "mCallbackHandler");
        HandlerThread thread = new HandlerThread("test");
        thread.start();
        mMessageThread = new BleMessageThread(thread, mBluetoothGattWrapper, mEventStore);
        Whitebox.setInternalState(mBletia, "mMessageThread", mMessageThread);
        Whitebox.setInternalState(mBletia, "mGattWrapper", mBluetoothGattWrapper);

        when(mCharacteristic.getUuid()).thenReturn(UUID.randomUUID());
        when(mBluetoothGattWrapper.writeCharacteristic(mCharacteristic)).thenReturn(true);
        when(mBluetoothGattWrapper.readCharacteristic(mCharacteristic)).thenReturn(true);

        mLatch = new CountDownLatch(1);
    }

    @Override
    public void tearDown() throws Exception {
        mMessageThread.stop();
    }

    @Test
    public void writeCharacteristicSuccessfully() throws Exception {
        mBletia.writeCharacteristic(mCharacteristic).then(new DoneCallback<BluetoothGattCharacteristic>() {
            @Override
            public void onDone(BluetoothGattCharacteristic result) {
                mLatch.countDown();
            }
        });

        Thread.sleep(300);
        mCallbackHandler.onCharacteristicWrite(
                mBluetoothGattWrapper, mCharacteristic, BluetoothGatt.GATT_SUCCESS);
        await();
    }

    @Test
    public void writeCharacteristicFailure() throws Exception {
        mBletia.writeCharacteristic(mCharacteristic).fail(new FailCallback<BleStatus>() {
            @Override
            public void onFail(BleStatus result) {
                assertThat(result).isEqualTo(BleStatus.FAILURE);
                mLatch.countDown();
            }
        });

        Thread.sleep(300);
        mCallbackHandler.onCharacteristicWrite(
                mBluetoothGattWrapper, mCharacteristic, BluetoothGatt.GATT_FAILURE);
        await();
    }

    @Test
    public void readCharacteristicSuccessfully() throws Exception {
        mBletia.readCharacteristic(mCharacteristic).then(new DoneCallback<BluetoothGattCharacteristic>() {
            @Override
            public void onDone(BluetoothGattCharacteristic result) {
                mLatch.countDown();
            }
        });

        Thread.sleep(300);
        mCallbackHandler.onCharacteristicRead(
                mBluetoothGattWrapper, mCharacteristic, BluetoothGatt.GATT_SUCCESS);
        await();
    }

    @Test
    public void readCharacteristicFailure() throws Exception {
        mBletia.readCharacteristic(mCharacteristic).fail(new FailCallback<BleStatus>() {
            @Override
            public void onFail(BleStatus result) {
                assertThat(result).isEqualTo(BleStatus.FAILURE);
                mLatch.countDown();
            }
        });

        Thread.sleep(300);
        mCallbackHandler.onCharacteristicRead(
                mBluetoothGattWrapper, mCharacteristic, BluetoothGatt.GATT_FAILURE);
        await();
    }

    @Test
    public void writeDescriptorSuccessfully() throws Exception {
        mBletia.writeDescriptor(mDescriptor).then(new DoneCallback<BluetoothGattDescriptor>() {
            @Override
            public void onDone(BluetoothGattDescriptor result) {
                mLatch.countDown();
            }
        });

        Thread.sleep(300);
        mCallbackHandler.onDescriptorWrite(
                mBluetoothGattWrapper, mDescriptor, BluetoothGatt.GATT_SUCCESS);
        await();
    }

    @Test
    public void writeDescriptorFailure() throws Exception {
        mBletia.writeDescriptor(mDescriptor).fail(new FailCallback<BleStatus>() {
            @Override
            public void onFail(BleStatus result) {
                assertThat(result).isEqualTo(BleStatus.FAILURE);
                mLatch.countDown();
            }
        });

        Thread.sleep(300);
        mCallbackHandler.onDescriptorWrite(
                mBluetoothGattWrapper, mDescriptor, BluetoothGatt.GATT_FAILURE);
        await();
    }

    @Test
    public void readDescriptorSuccessfully() throws Exception {
        mBletia.readDescriptor(mDescriptor).then(new DoneCallback<BluetoothGattDescriptor>() {
            @Override
            public void onDone(BluetoothGattDescriptor result) {
                mLatch.countDown();
            }
        });

        Thread.sleep(300);
        mCallbackHandler.onDescriptorRead(
                mBluetoothGattWrapper, mDescriptor, BluetoothGatt.GATT_SUCCESS);
        await();
    }

    @Test
    public void readDescriptorFailure() throws Exception {
        mBletia.readDescriptor(mDescriptor).fail(new FailCallback<BleStatus>() {
            @Override
            public void onFail(BleStatus result) {
                assertThat(result).isEqualTo(BleStatus.FAILURE);
                mLatch.countDown();
            }
        });

        Thread.sleep(300);
        mCallbackHandler.onDescriptorRead(
                mBluetoothGattWrapper, mDescriptor, BluetoothGatt.GATT_FAILURE);
        await();
    }

    private void await() throws InterruptedException {
        boolean res = mLatch.await(1000, TimeUnit.MILLISECONDS);
        assertThat(res).isTrue();
    }
}
