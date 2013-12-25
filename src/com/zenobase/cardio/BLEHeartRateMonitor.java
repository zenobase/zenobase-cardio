package com.zenobase.cardio;

import java.util.UUID;

import javax.inject.Inject;

import roboguice.util.Ln;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import com.squareup.otto.Bus;

public class BLEHeartRateMonitor extends BluetoothGattCallback implements HeartRateMonitor {

	public static final UUID BLE_HEART_RATE_SERVICE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
	public static final UUID BLE_HEART_RATE_CHARACTERISTIC = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
	public static final UUID BLE_CLIENT_CONFIG_CHARACTERISTIC = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private final Handler handler = new Handler();
	private final Context context;
	private final BluetoothDevice device;
	private final Bus bus;
	private BluetoothGatt gatt;
	private State state = State.DISCONNECTED;

	@Inject
	public BLEHeartRateMonitor(Context context, BluetoothDevice device, Bus bus) {
		this.context = context;
		this.device = device;
		this.bus = bus;
    }

	@Override
	public void connect() {
		device.connectGatt(context, false, this);
	}

	@Override
	public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
		switch (newState) {
			case BluetoothProfile.STATE_CONNECTING:
				Ln.d("Connecting to %s...", device.getName());
				break;
			case BluetoothProfile.STATE_CONNECTED:
				Ln.i("Connected to %s", device.getName());
				this.gatt = gatt;
				gatt.discoverServices();
				break;
			case BluetoothProfile.STATE_DISCONNECTING:
				Ln.d("Disconnecting from %s...", device.getName());
				break;
			case BluetoothProfile.STATE_DISCONNECTED:
				Ln.d("Disconnected from %s...", device.getName());
				disconnect();
				setState(HeartRateMonitor.State.DISCONNECTED);
				break;
			default:
				Ln.w("Unhandled state: %d", newState);
		}
	}

	@Override
	public void onServicesDiscovered(BluetoothGatt gatt, int status) {
		if (status == BluetoothGatt.GATT_SUCCESS) {
			BluetoothGattService service = gatt.getService(BLE_HEART_RATE_SERVICE);
			if (service != null) {
				BluetoothGattCharacteristic c = service.getCharacteristic(BLE_HEART_RATE_CHARACTERISTIC);
				if (c != null) {
					Ln.d("Subscribing to heart rate characteristic...");
					gatt.setCharacteristicNotification(c, true);
					BluetoothGattDescriptor descriptor = c.getDescriptor(BLE_CLIENT_CONFIG_CHARACTERISTIC);
					descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
					gatt.writeDescriptor(descriptor);
					setState(HeartRateMonitor.State.CONNECTED);
				} else {
					Ln.w("Could not find heart rate characteristic");
				}
			} else {
				Ln.w("Could not find heart rate service");
			}
		} else {
			Ln.w("Could not discover any services: %d", status);
		}
	}

	@Override
	public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
		int flag = characteristic.getProperties();
		int format = (flag & 0x01) != 0 ? BluetoothGattCharacteristic.FORMAT_UINT16 : BluetoothGattCharacteristic.FORMAT_UINT8;
		int offset = 1;
		int hr = characteristic.getIntValue(format, offset);
		Ln.d("Heart Rate: %d", hr);
		if (format == BluetoothGattCharacteristic.FORMAT_UINT16) {
			++offset;
		}
		if ((flag & 0x8) != 0) { // energy
			offset += 2;
		}
		if ((flag & 0x16) != 0) { // RR intervals
			++offset;
			for (int i = 0; i < 7; ++i) {
				Integer rr = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset + i * 2);
				if (rr == null) {
					break;
				}
				Ln.d("RR(%d): %d", i, rr);
				post(new RRValue(rr));
			}
		}
	}

	private void post(final Object value) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				bus.post(value);
			}
		});
	}

	@Override
	public void disconnect() {
		if (gatt != null) {
			Ln.d("Closing the GATT...");
			gatt.close();
			gatt = null;
		}
    }

	@Override
	public State getState() {
		return state;
	}

	private void setState(State state) {
		this.state = state;
		post(state);
	}

	@Override
	public String toString() {
		return device.getName();
	}
}
