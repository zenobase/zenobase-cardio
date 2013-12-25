package com.zenobase.cardio;

import java.util.UUID;

import javax.annotation.Nullable;

import roboguice.util.Ln;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;
import com.google.inject.Inject;
import com.squareup.otto.Bus;

public class BLEHeartRateMonitorFactory extends HeartRateMonitorFactory {

	private final Bus bus;
	private final BluetoothAdapter adapter;
    private final Handler handler = new Handler();

	@Inject
	public BLEHeartRateMonitorFactory(Bus bus, @Nullable BluetoothAdapter adapter) {
		this.bus = bus;
		this.adapter = adapter;
	}

	@Override
	public void scan(final Context context) {
		if (adapter != null && adapter.isEnabled()) {
			final BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
				@Override
				public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
					Ln.d("Found: %s", device.getName());
					handler.post(new Runnable() {
						@Override
						public void run() {
							bus.post(new HeartRateMonitorFound(new BLEHeartRateMonitor(context, device, bus)));
						}
					});
				}
			};
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					adapter.stopLeScan(callback);
					Ln.d("Stopped scan");
				}
			}, 2500);
			Ln.d("Starting scan...");
			adapter.startLeScan(new UUID[] { BLEHeartRateMonitor.BLE_HEART_RATE_SERVICE }, callback);
		} else {
			Toast.makeText(context, R.string.message_bluetooth_disabled, Toast.LENGTH_LONG).show();
		}
	}
}
