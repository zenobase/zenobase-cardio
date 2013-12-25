package com.zenobase.cardio;

import javax.inject.Inject;

import roboguice.activity.RoboListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class ListActivity extends RoboListActivity {

	@Inject
	private Bus bus;

	@Inject
	private Preferences preferences;

	@Inject
	private HeartRateMonitorFactory factory;

	private Menu menu;

	private ArrayAdapter<HeartRateMonitor> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	list = new ArrayAdapter<HeartRateMonitor>(getApplicationContext(), android.R.layout.simple_list_item_1);
    	setListAdapter(list);
    	bus.register(this);
		factory.scan(this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Authorization authorization = Authorization.parse(intent.getData());
		if (authorization != null) {
			preferences.setAuthorization(authorization);
			invalidateOptionsMenu();
			Toast.makeText(this, R.string.toast_sign_in_success, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, R.string.toast_sign_in_error, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
    	getMenuInflater().inflate(R.menu.list, menu);
		setVisible(R.id.menu_sign_in, !preferences.hasAuthorization());
		setVisible(R.id.menu_sign_out, preferences.hasAuthorization());
    	return super.onCreateOptionsMenu(menu);
	}

	private void setVisible(int id, boolean enabled) {
		menu.findItem(id).setVisible(enabled);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_sign_in:
				signIn();
				return true;
			case R.id.menu_sign_out:
				signOut();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void signIn() {
		new AuthorizeIntent(this).start();
	}

	private void signOut() {
		new SignOutTask(this, preferences.getAuthorization()).execute();
		preferences.setAuthorization(null);
		invalidateOptionsMenu();
	}

	@Subscribe
	public void onHeartRateMonitor(final HeartRateMonitorFound monitor) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				list.add(monitor.getMonitor());
			}
		});
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
    	factory.select(list.getItem(position));
    	startActivity(new Intent(this, DashboardActivity.class));
    }

    @Override
    protected void onDestroy() {
    	bus.unregister(this);
    	super.onDestroy();
    }
}
