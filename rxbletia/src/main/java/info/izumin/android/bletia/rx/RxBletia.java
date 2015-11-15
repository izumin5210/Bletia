package info.izumin.android.bletia.rx;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;

import info.izumin.android.bletia.core.AbstractBletia;
import info.izumin.android.bletia.rx.action.RxConnectAction;
import info.izumin.android.bletia.rx.action.RxDisconnectAction;
import info.izumin.android.bletia.rx.action.RxDiscoverServicesAction;
import info.izumin.android.bletia.rx.action.RxEnableNotificationAction;
import info.izumin.android.bletia.rx.action.RxReadCharacteristicAction;
import info.izumin.android.bletia.rx.action.RxReadDescriptorAction;
import info.izumin.android.bletia.rx.action.RxReadRemoteRssiAction;
import info.izumin.android.bletia.rx.action.RxWriteCharacteristicAction;
import info.izumin.android.bletia.rx.action.RxWriteDescriptorAction;
import rx.Observable;

/**
 * Created by izumin on 11/15/15.
 */
public class RxBletia extends AbstractBletia {
    public static final String TAG = RxBletia.class.getSimpleName();

    public RxBletia(Context context) {
        super(context);
    }

    public Observable<Void> conncet(BluetoothDevice device) {
        return execute(new RxConnectAction(device, getStateContainer()));
    }

    public Observable<Void> disconnect() {
        return execute(new RxDisconnectAction(getStateContainer()));
    }

    public Observable<Void> discoverServices() {
        return execute(new RxDiscoverServicesAction(getStateContainer()));
    }

    public Observable<BluetoothGattCharacteristic> writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        return execute(new RxWriteCharacteristicAction(characteristic));
    }

    public Observable<BluetoothGattCharacteristic> readCharacteristic(BluetoothGattCharacteristic characteristic) {
        return execute(new RxReadCharacteristicAction(characteristic));
    }

    public Observable<BluetoothGattDescriptor> writeDescriptor(BluetoothGattDescriptor descriptor) {
        return execute(new RxWriteDescriptorAction(descriptor));
    }

    public Observable<BluetoothGattDescriptor> readDescriptor(BluetoothGattDescriptor descriptor) {
        return execute(new RxReadDescriptorAction(descriptor));
    }

    public Observable<BluetoothGattCharacteristic> enableNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        return execute(new RxEnableNotificationAction(characteristic, enabled));
    }

    public Observable<Integer> readRemoteRssi() {
        return execute(new RxReadRemoteRssiAction());
    }
}
