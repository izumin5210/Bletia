package info.izumin.android.bletia.action;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;

import java.util.UUID;

/**
 * Created by izumin on 9/15/15.
 */
public abstract class CharacteristicAction extends Action<BluetoothGattCharacteristic, UUID> {

    private final BluetoothGattCharacteristic mCharacteristic;

    public CharacteristicAction(BluetoothGattService service, UUID uuid) {
        super(uuid);
        mCharacteristic = service.getCharacteristic(uuid);
    }

    public CharacteristicAction(BluetoothGattCharacteristic characteristic) {
        super(characteristic.getUuid());
        mCharacteristic = characteristic;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return mCharacteristic;
    }

    @Override
    public Bundle getBundle() {
        Bundle bundle = super.getBundle();
        bundle.putSerializable(KEY_UUID, mCharacteristic.getUuid());
        return bundle;
    }
}
