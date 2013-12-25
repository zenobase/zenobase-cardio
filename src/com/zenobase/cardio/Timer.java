package com.zenobase.cardio;

import javax.inject.Inject;

import android.os.CountDownTimer;
import android.os.Vibrator;
import com.squareup.otto.Bus;

public class Timer {

	private static long TIMER_DURATION = 300000L;

	private final Bus bus;
	private final Vibrator vibrator;
	private long millisUntilFinished;
	private CountDownTimer timer;

	@Inject
	public Timer(Bus bus, Vibrator vibrator) {
		this.bus = bus;
		this.vibrator = vibrator;
		reset();
	}

	public void start() {
		timer = new CountDownTimer(millisUntilFinished, 1000L) {

			@Override
			public void onTick(long millisUntilFinished) {
				Timer.this.millisUntilFinished = millisUntilFinished;
				bus.post(getCurrentValue());
			}

			@Override
			public void onFinish() {
				vibrator.vibrate(1000L);
				Timer.this.millisUntilFinished = 0L;
				bus.post(getCurrentValue());
			}
		};
		timer.start();
	}

	public void stop() {
		if (timer != null) {
			timer.cancel();
		}
	}

	public void reset() {
		millisUntilFinished = TIMER_DURATION;
		bus.post(getCurrentValue());
	}

	public TimerValue getCurrentValue() {
		return new TimerValue(millisUntilFinished);
	}

	public long getEllapsedMillis() {
		return TIMER_DURATION - millisUntilFinished;
	}
}
