package com.zenobase.cardio;

import android.content.Context;

public abstract class HeartRateMonitorFactory {

    private HeartRateMonitor monitor;

	public abstract void scan(Context context);

	public void select(HeartRateMonitor monitor) {
		this.monitor = monitor;
	}

	public HeartRateMonitor getSelected() {
		return monitor;
	}
}
