package com.zenobase.cardio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class EventDialog {

	private final Context context;
	private final Preferences preferences;

	public EventDialog(Context context, Preferences preferences) {
		this.context = context;
		this.preferences = preferences;
	}

	public void show(final Event event) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context)
			.setTitle(context.getResources().getString(R.string.event_dialog_title))
			.setMessage(context.getResources().getString(R.string.event_dialog_message, event.getFrequency(), event.getRating()))
			.setNegativeButton(R.string.event_dialog_negative, null);
		if (preferences.hasAuthorization()) {
			dialog.setPositiveButton(R.string.event_dialog_positive, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new SaveEventTask(context, preferences.getAuthorization(), event).execute();
				}
			});
		}
		dialog.show();
	}
}
