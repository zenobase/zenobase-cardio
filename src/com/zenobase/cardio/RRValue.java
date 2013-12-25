package com.zenobase.cardio;

public class RRValue {

	private final int value;

	public RRValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public boolean equals(Object that) {
		return that instanceof RRValue &&
			equals((RRValue) that);
	}

	public boolean equals(RRValue that) {
		return value == that.getValue();
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}
}
