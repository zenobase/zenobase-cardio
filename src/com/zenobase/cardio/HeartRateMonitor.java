package com.zenobase.cardio;

public interface HeartRateMonitor {

	void connect();

	void disconnect();

	State getState();

	enum State {
		CONNECTED,
		DISCONNECTED;
	}
}
