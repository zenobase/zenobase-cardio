package com.zenobase.cardio;

public class TimerValue {

	private final long value;

	public TimerValue(long value) {
		this.value = value;
	}

	public long getMillis() {
		return value;
	}

	@Override
	public boolean equals(Object that) {
		return that instanceof TimerValue &&
			equals((TimerValue) that);
	}

	public boolean equals(TimerValue that) {
		return value == that.getMillis();
	}

	@Override
	public int hashCode() {
		return (int) value;
	}

	@Override
	public String toString() {
		return Long.toString(value);
	}
}
