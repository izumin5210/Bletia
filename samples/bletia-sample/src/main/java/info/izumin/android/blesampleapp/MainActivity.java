package info.izumin.android.blesampleapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jdeferred.DoneCallback;
import org.jdeferred.DonePipe;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;

import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import info.izumin.android.bletia.Bletia;
import info.izumin.android.bletia.BletiaListener;
import info.izumin.android.bletia.core.BleState;
import info.izumin.android.bletia.core.BletiaException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final UUID ALERT_SERVICE_UUID = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");
    public static final UUID BATTERY_SERVICE_UUID = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");
    public static final UUID BATTERY_UUID = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");
    public static final UUID BATTERY_POWER_STATE_UUID = UUID.fromString("00002a1b-0000-1000-8000-00805f9b34fb");
    public static final UUID ALERT_LEVEL_UUID = UUID.fromString("00002a06-0000-1000-8000-00805f9b34fb");

    private final static int REQUEST_ENABLE_BT = 1;
    private final static int REQUEST_LOCATION = 3;

    private Bletia mBletia;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mScanner;

    private BleDeviceAdapter mDeviceAdapter;

    private Menu mMenu;

    @Bind(R.id.list_devices)
    ListView mDeviceListView;
    @Bind(R.id.main)
    View mMainContent;
    @Bind(R.id.text_battery_level)
    TextView mTextBatteryLevel;
    @Bind(R.id.text_battery_state)
    TextView mTextBatteryState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDeviceAdapter = new BleDeviceAdapter(this);
        mDeviceListView.setAdapter(mDeviceAdapter);

        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        mBletia = new Bletia(getApplicationContext());
        mBletia.addListener(mBletiaListener);
    }

    @Override
    protected void onDestroy() {
        if (mScanner != null) {
            mScanner.stopScan(mScanCallback);
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (isEnabledBle()) { scanBleDevice(); }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanBleDevice();
            } else {
                Toast.makeText(this, "permission does not granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_connect:
                scanBleDevice();
                return true;
            case R.id.action_disconnect:
                mBletia.disconnect()
                        .then(new DoneCallback<Void>() {
                            @Override
                            public void onDone(Void result) {
                                Log.d(TAG, "Device disconnected...");
                                refresh(false);
                            }
                        })
                        .fail(mFailCallback);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnItemClick(R.id.list_devices)
    void onDeviceSelected(int position) {
        BluetoothDevice device = mDeviceAdapter.getItem(position);
        Log.d(TAG, "Connected to " + device.getName() + " !");
        mScanner.stopScan(mScanCallback);
        mBletia.connect(device)
                .then(new DonePipe<Void, Void, BletiaException, Void>() {
                    @Override
                    public Promise<Void, BletiaException, Void> pipeDone(Void result) {
                        Log.d(TAG, "Device connected!");
                        return mBletia.discoverServices();
                    }
                })
                .then(new DoneCallback<Void>() {
                    @Override
                    public void onDone(Void result) {
                        Log.d(TAG, "Services discovered!");
                        refresh(true);
                        mBletia.enableNotification(getBatteryStateCharacteristic(), true)
                                .fail(mFailCallback);
                    }
                })
                .fail(mFailCallback);
    }

    @OnClick(R.id.btn_alert_off)
    void onClickBtnAlertOff() {
        mBletia.writeCharacteristic(getAlertLevelCharacteristic(0))
                .fail(mFailCallback);
    }

    @OnClick(R.id.btn_alert_vibration)
    void onClickBtnAlertVibration() {
        mBletia.writeCharacteristic(getAlertLevelCharacteristic(1))
                .fail(mFailCallback);
    }

    @OnClick(R.id.btn_alert_alarm)
    void onClickBtnAlertAlarm() {
        mBletia.writeCharacteristic(getAlertLevelCharacteristic(2))
                .fail(mFailCallback);
    }

    @OnClick(R.id.btn_read_battery)
    void onClickBtnReadBattery() {
        mBletia.readCharacteristic(mBletia.getService(BATTERY_SERVICE_UUID).getCharacteristic(BATTERY_UUID))
                .then(new DoneCallback<BluetoothGattCharacteristic>() {
                    @Override
                    public void onDone(final BluetoothGattCharacteristic result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTextBatteryLevel.setText(String.valueOf(result.getValue()[0]));
                            }
                        });
                    }
                })
                .fail(mFailCallback);
    }

    private boolean isEnabledBle() {
        return (mBluetoothAdapter != null) && mBluetoothAdapter.isEnabled();
    }

    private void scanBleDevice() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
                return;
            }
        }
        mScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mScanner.startScan(mScanCallback);
    }

    private void refresh(final boolean isConnected) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMenu.findItem(R.id.action_connect).setVisible(!isConnected);
                mMenu.findItem(R.id.action_disconnect).setVisible(isConnected);
                mDeviceListView.setVisibility(isConnected ? View.GONE : View.VISIBLE);
                mMainContent.setVisibility(isConnected ? View.VISIBLE : View.GONE);
            }
        });
    }

    private BluetoothGattCharacteristic getAlertLevelCharacteristic(int level) {
        BluetoothGattCharacteristic characteristic = mBletia.getService(ALERT_SERVICE_UUID).getCharacteristic(ALERT_LEVEL_UUID);
        characteristic.setValue(new byte[]{(byte) level});
        return characteristic;
    }

    private BluetoothGattCharacteristic getBatteryStateCharacteristic() {
        return mBletia.getService(BATTERY_SERVICE_UUID).getCharacteristic(BATTERY_POWER_STATE_UUID);
    }

    private final ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            Log.d(TAG, callbackType + ": " + device.getName() + ", " + device.getAddress());
            if ((device.getName() != null) && !mDeviceAdapter.hasDevice(device)) {
                mDeviceAdapter.add(device);
            }
        }
    };

    private final BletiaListener mBletiaListener = new BletiaListener() {
        @Override
        public void onError(BletiaException exception, BleState state) {
            Log.d(TAG, "[" + state.name() + "] " + exception.getMessage());
            if (state == BleState.DISCONNECTED) {
                refresh(false);
            }
        }

        @Override
        public void onCharacteristicChanged(Bletia bletia, final BluetoothGattCharacteristic characteristic) {
            if (characteristic.getUuid().equals(BATTERY_POWER_STATE_UUID)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextBatteryState.setText(String.valueOf(characteristic.getValue()[0]));
                    }
                });
            }
        }
    };

    private final FailCallback<BletiaException> mFailCallback = new FailCallback<BletiaException>() {
        @Override
        public void onFail(BletiaException result) {
            Log.d(TAG, result.getMessage());
        }
    };
}
