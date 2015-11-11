package info.izumin.android.blesampleapp;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by izumin on 10/12/15.
 */
public class BleDeviceAdapter extends ArrayAdapter<BluetoothDevice> {

    private static final int LAYOUT_RESOURCE_ID = R.layout.bluetooth_device_item;
    private final LayoutInflater mInflater;

    public BleDeviceAdapter(Context context) {
        super(context, LAYOUT_RESOURCE_ID);
        mInflater = LayoutInflater.from(context);
    }

    public boolean hasDevice(BluetoothDevice device) {
        return getPosition(device) != -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final BluetoothDevice device = getItem(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.bluetooth_device_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(device.getName());
        holder.address.setText(device.getAddress());

        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.text_device_name) TextView name;
        @Bind(R.id.text_device_address) TextView address;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
