package info.izumin.android.bletia;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;

import org.jdeferred.Promise;

import info.izumin.android.bletia.action.EnableNotificationAction;
import info.izumin.android.bletia.action.ReadCharacteristicAction;
import info.izumin.android.bletia.action.ReadDescriptorAction;
import info.izumin.android.bletia.action.ReadRemoteRssiAction;
import info.izumin.android.bletia.action.WriteCharacteristicAction;
import info.izumin.android.bletia.action.WriteDescriptorAction;
import info.izumin.android.bletia.core.AbstractBletia;
import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/7/15.
 */
public class Bletia extends AbstractBletia {

    private EventEmitter mEmitter;

    public Bletia(Context context) {
        super(context);
        setSubListener(mListener);
        mEmitter = new EventEmitter(this);
    }
    public void addListener(BletiaListener listener) {
        mEmitter.addListener(listener);
    }

    public void removeListener(BletiaListener listener) {
        mEmitter.removeListener(listener);
    }

    public Promise<BluetoothGattCharacteristic, BletiaException, Void> writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        return execute(new WriteCharacteristicAction(characteristic));
    }

    public Promise<BluetoothGattCharacteristic, BletiaException, Void> readCharacteristic(BluetoothGattCharacteristic characteristic) {
        return execute(new ReadCharacteristicAction(characteristic));
    }

    public Promise<BluetoothGattDescriptor, BletiaException, Void> writeDescriptor(BluetoothGattDescriptor descriptor) {
        return execute(new WriteDescriptorAction(descriptor));
    }

    public Promise<BluetoothGattDescriptor, BletiaException, Void> readDescriptor(BluetoothGattDescriptor descriptor) {
        return execute(new ReadDescriptorAction(descriptor));
    }

    public Promise<BluetoothGattCharacteristic, BletiaException, Void> enableNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        return execute(new EnableNotificationAction(characteristic, enabled));
    }

    public Promise<Integer, BletiaException, Void> readRemoteRssi() {
        return execute(new ReadRemoteRssiAction());
    }

    private final AbstractBletia.BleEventListener mListener = new BleEventListener() {
        @Override
        public void onConnect(BluetoothGattWrapper gatt) {
            mEmitter.emitConnectEvent();
        }

        @Override
        public void onDisconnect(BluetoothGattWrapper gatt) {
            mEmitter.emitDisconnectEvent();
        }

        @Override
        public void onServiceDiscovered(int status) {
            mEmitter.emitServiceDiscovered(status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
            mEmitter.emitCharacteristicChanged(characteristic);
        }

        @Override
        public void onError(BletiaException exception) {
            mEmitter.emitError(exception);
        }
    };
}
