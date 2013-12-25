package com.zenobase.cardio;

import javax.inject.Inject;
import javax.inject.Provider;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

public class BluetoothAdapterProvider implements Provider<BluetoothAdapter> {

	private final Context context;

	@Inject
	public BluetoothAdapterProvider(Context context) {
		this.context = context;
	}

	@Override
	public BluetoothAdapter get() {
		BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
		return manager != null ? manager.getAdapter() : null;
	}
}
