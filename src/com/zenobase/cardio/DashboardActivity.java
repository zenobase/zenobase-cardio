package com.zenobase.cardio;

import java.util.Date;

import javax.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class DashboardActivity extends RoboActivity {

	@InjectView(R.id.sourceValue)
	private TextView sourceView;

	@InjectView(R.id.rrPlot)
	private RRChartView rrChartView;

	@InjectView(R.id.rrValue)
	private TextView rrView;

	@InjectView(R.id.timerValue)
	private TimerView timerView;

	@Inject
	private Bus bus;

	@Inject
	private Timer timer;

	@Inject
	private HeartRateMonitorFactory factory;

	@Inject
	private Scoreboard scoreboard;

	@Inject
	private Preferences preferences;

	private Menu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		bus.register(this);
    	setContentView(R.layout.dashboard);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		updateRRValues();
		timerView.setValue(timer.getCurrentValue());
	}

	@Override
	protected void onStart() {
		super.onStart();
		factory.getSelected().connect();
	}

	@Override
	protected void onPause() {
		super.onPause();
		pause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		factory.getSelected().disconnect();
	}

	@Override
	protected void onDestroy() {
		bus.unregister(this);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
    	getMenuInflater().inflate(R.menu.dashboard, menu);
		setVisible(R.id.menu_start, !scoreboard.isEnabled());
	    setEnabled(R.id.menu_start, factory.getSelected().getState() == HeartRateMonitor.State.CONNECTED);
		setVisible(R.id.menu_pause, scoreboard.isEnabled());
	    setEnabled(R.id.menu_stop, scoreboard.isEnabled() || scoreboard.getCount() > 0);
    	return super.onCreateOptionsMenu(menu);
	}

	private void setVisible(int id, boolean enabled) {
		menu.findItem(id).setVisible(enabled);
	}

	private void setEnabled(int id, boolean enabled) {
		MenuItem item = menu.findItem(id);
		item.setEnabled(enabled);
	    item.getIcon().setAlpha(enabled ? 255 : 64);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_start:
				start();
				return true;
			case R.id.menu_pause:
				pause();
				return true;
			case R.id.menu_stop:
				stop();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

    private void updateRRValues() {
    	rrView.setText(scoreboard.getCount() > 0 ? Integer.toString(scoreboard.getLast()) : "");
    	rrChartView.setIntegerValues(scoreboard.getLast(60));
    }

    @Subscribe
    public void onTimerValue(TimerValue value) {
    	timerView.setValue(value);
        if (value.getMillis() > 0L) {
        	updateRRValues();
        } else {
        	stop();
        }
    }

    @Subscribe
    public void onHeartRateMonitorState(HeartRateMonitor.State state) {
    	sourceView.setText(state == HeartRateMonitor.State.CONNECTED ? factory.getSelected().toString() : "");
    	invalidateOptionsMenu();
    }

	private void start() {
		timer.start();
		scoreboard.setEnabled(true);
		invalidateOptionsMenu();
	}

	private void pause() {
		timer.stop();
		scoreboard.setEnabled(false);
		invalidateOptionsMenu();
	}

	private void stop() {
		Event event = createEvent();
		timer.stop();
		timer.reset();
		scoreboard.setEnabled(false);
		scoreboard.reset();
		updateRRValues();
		invalidateOptionsMenu();
		new EventDialog(this, preferences).show(event);
	}

	private Event createEvent() {
		String tag = getText(R.string.event_tag).toString();
		return new Event(new Date(), tag, timer.getEllapsedMillis(), scoreboard.getHR(), Math.min(scoreboard.getSDNN(), 100));
	}
}
