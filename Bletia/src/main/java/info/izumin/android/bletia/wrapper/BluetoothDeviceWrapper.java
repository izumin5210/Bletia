package info.izumin.android.bletia.wrapper;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Parcel;
import android.os.ParcelUuid;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by izumin on 9/9/15.
 */
public class BluetoothDeviceWrapper {
    private final BluetoothDevice mDevice;

    public BluetoothDeviceWrapper(BluetoothDevice device) {
        mDevice = device;
    }

    public BluetoothSocket createInsecureRfcommSocketToServiceRecord(UUID uuid) throws IOException {
        return mDevice.createInsecureRfcommSocketToServiceRecord(uuid);
    }

    public int getBondState() {
        return mDevice.getBondState();
    }

    public int getType() {
        return mDevice.getType();
    }

    public boolean setPairingConfirmation(boolean confirm) {
        return mDevice.setPairingConfirmation(confirm);
    }

    public boolean setPin(byte[] pin) {
        return mDevice.setPin(pin);
    }

    public boolean createBond() {
        return mDevice.createBond();
    }

    public boolean fetchUuidsWithSdp() {
        return mDevice.fetchUuidsWithSdp();
    }

    public String getName() {
        return mDevice.getName();
    }

    public void writeToParcel(Parcel out, int flags) {
        mDevice.writeToParcel(out, flags);
    }

    public int describeContents() {
        return mDevice.describeContents();
    }

    public ParcelUuid[] getUuids() {
        return mDevice.getUuids();
    }

    public BluetoothGattWrapper connectGatt(Context context, boolean autoConnect, BluetoothGattCallback callback) {
        return new BluetoothGattWrapper(mDevice.connectGatt(context, autoConnect, callback));
    }

    public BluetoothClass getBluetoothClass() {
        return mDevice.getBluetoothClass();
    }

    public BluetoothSocket createRfcommSocketToServiceRecord(UUID uuid) throws IOException {
        return mDevice.createRfcommSocketToServiceRecord(uuid);
    }

    public String getAddress() {
        return mDevice.getAddress();
    }
}
