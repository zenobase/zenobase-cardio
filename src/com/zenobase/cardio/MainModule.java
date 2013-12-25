package com.zenobase.cardio;

import android.bluetooth.BluetoothAdapter;
import com.google.inject.AbstractModule;
import com.squareup.otto.Bus;

public class MainModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Bus.class).asEagerSingleton();
		bind(Timer.class).asEagerSingleton();
		bind(Scoreboard.class).asEagerSingleton();
		bind(BluetoothAdapter.class).toProvider(BluetoothAdapterProvider.class);
		// bind(HeartRateMonitorFactory.class).to(FakeHeartRateMonitorFactory.class).asEagerSingleton();
		bind(HeartRateMonitorFactory.class).to(BLEHeartRateMonitorFactory.class).asEagerSingleton();
	}
}
