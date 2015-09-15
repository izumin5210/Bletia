package info.izumin.android.bletia.event;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;

import java.util.UUID;

/**
 * Created by izumin on 9/15/15.
 */
public abstract class CharacteristicEvent extends Event<BluetoothGattCharacteristic> {

    private final BluetoothGattCharacteristic mCharacteristic;

    public CharacteristicEvent(BluetoothGattCharacteristic characteristic) {
        super();
        mCharacteristic = characteristic;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return mCharacteristic;
    }

    @Override
    public UUID getUuid() {
        return mCharacteristic.getUuid();
    }

    @Override
    public Bundle getBundle() {
        Bundle bundle = super.getBundle();
        bundle.putSerializable(KEY_UUID, mCharacteristic.getUuid());
        return bundle;
    }
}
