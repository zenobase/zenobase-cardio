package com.zenobase.cardio;

import javax.inject.Inject;

import android.content.SharedPreferences;

public class Preferences {

	private static final String KEY_TOKEN = "oauth_token";
	private static final String KEY_SCOPE = "oauth_scope";
	private static final String KEY_CLIENT_ID = "oauth_client_id";

	private final SharedPreferences preferences;

	@Inject
	public Preferences(SharedPreferences preferences) {
		this.preferences = preferences;
	}

	public boolean hasAuthorization() {
		return preferences.contains(KEY_TOKEN);
	}

	public Authorization getAuthorization() {
		String token = preferences.getString(KEY_TOKEN, null);
		String scope = preferences.getString(KEY_SCOPE, null);
		String clientId = preferences.getString(KEY_CLIENT_ID, null);
		return token != null ? new Authorization(token, scope, clientId) : null;
	}

	public void setAuthorization(Authorization authorization) {
		SharedPreferences.Editor editor = preferences.edit();
		if (authorization != null) {
			editor.putString(KEY_TOKEN, authorization.getToken());
			editor.putString(KEY_SCOPE, authorization.getScope());
			editor.putString(KEY_CLIENT_ID, authorization.getClientId());
		} else {
			editor.remove(KEY_TOKEN);
			editor.remove(KEY_SCOPE);
			editor.remove(KEY_CLIENT_ID);
		}
		editor.apply();
	}
}
