package com.zenobase.cardio;

import android.content.Context;
import com.google.inject.Inject;
import com.squareup.otto.Bus;

public class FakeHeartRateMonitorFactory extends HeartRateMonitorFactory {

	private final Bus bus;

	@Inject
	public FakeHeartRateMonitorFactory(Bus bus) {
		this.bus = bus;
	}

	@Override
	public void scan(Context context) {
		bus.post(new HeartRateMonitorFound(new FakeHeartRateMonitor(bus)));
	}
}
