package com.zenobase.cardio;

import com.zenobase.cardio.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AuthorizeIntent {

	private static final String URI_TEMPLATE = "%s/#/oauth/authorize?response_type=token&client_id=%s&redirect_uri=%s://%s%s?";

	private final Context context;

	public AuthorizeIntent(Context context) {
		this.context = context;
	}

	public void start() {
		context.startActivity(new Intent(Intent.ACTION_VIEW, uri()));
	}

	private Uri uri() {
		return Uri.parse(String.format(URI_TEMPLATE,
			getText(R.string.host),
			getText(R.string.oauth_client_id),
			getText(R.string.data_scheme),
			getText(R.string.data_host),
			getText(R.string.data_path)));
	}

	private CharSequence getText(int resId) {
		return context.getText(resId);
	}
}
