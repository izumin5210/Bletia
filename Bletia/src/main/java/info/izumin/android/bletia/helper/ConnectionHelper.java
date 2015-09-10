package info.izumin.android.bletia.helper;

import android.bluetooth.BluetoothGattCallback;
import android.content.Context;

import info.izumin.android.bletia.wrapper.BluetoothDeviceWrapper;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/10/15.
 */
public class ConnectionHelper {

    private final Context mContext;
    private BluetoothGattWrapper mGattWrapper;

    public ConnectionHelper(Context context) {
        mContext = context;
    }

    public BluetoothGattWrapper connect(BluetoothDeviceWrapper deviceWrapper, BluetoothGattCallback bluetoothGattCallback) {
        mGattWrapper = deviceWrapper.connectGatt(mContext, false, bluetoothGattCallback);
        return mGattWrapper;
    }

    public void disconnect() {
        mGattWrapper.disconnect();
    }

    public void close() {
        mGattWrapper.close();
    }
}