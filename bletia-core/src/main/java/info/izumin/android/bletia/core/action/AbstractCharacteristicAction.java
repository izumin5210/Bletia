package info.izumin.android.bletia.core.action;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.UUID;

import info.izumin.android.bletia.core.BletiaException;
import info.izumin.android.bletia.core.ActionResolver;

/**
 * Created by izumin on 9/15/15.
 */
public abstract class AbstractCharacteristicAction extends AbstractAction<BluetoothGattCharacteristic, BletiaException, UUID> {

    private final BluetoothGattCharacteristic mCharacteristic;

    public AbstractCharacteristicAction(BluetoothGattCharacteristic characteristic, Type type,
                                        ActionResolver<BluetoothGattCharacteristic, BletiaException> actionResolver) {
        super(characteristic.getUuid(), type, actionResolver);
        mCharacteristic = characteristic;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return mCharacteristic;
    }
}
