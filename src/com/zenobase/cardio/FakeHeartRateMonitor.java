package com.zenobase.cardio;

import java.util.Random;

import javax.inject.Inject;

import roboguice.util.Ln;
import android.os.Handler;
import com.squareup.otto.Bus;

public class FakeHeartRateMonitor implements HeartRateMonitor, Runnable {

	private final Handler handler = new Handler();
	private final Random rand = new Random();
	private final Bus bus;
	private State state = State.DISCONNECTED;

	@Inject
	public FakeHeartRateMonitor(Bus bus) {
		this.bus = bus;
	}

	@Override
	public void connect() {
		Ln.d("Connecting fake heart rate monitor...");
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				setState(State.CONNECTED);
				handler.post(FakeHeartRateMonitor.this);
			}
		}, 2000);
	}

	@Override
	public void run() {
		bus.post(new RRValue(950 + rand.nextInt(100)));
		handler.postDelayed(this, 1000);
	}

	@Override
	public void disconnect() {
		Ln.d("Disconnecting fake heart rate monitor...");
		handler.removeCallbacks(this);
		setState(State.DISCONNECTED);
	}

	@Override
	public State getState() {
		return state;
	}

	private void setState(State state) {
		this.state = state;
		bus.post(state);
	}

	@Override
	public String toString() {
		return "Random Data";
	}
}
