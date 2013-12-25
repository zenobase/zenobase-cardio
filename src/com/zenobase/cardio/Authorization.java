package com.zenobase.cardio;

import android.net.Uri;
import com.google.common.base.Preconditions;

public class Authorization {

	private final String token;
	private final String scope;
	private final String clientId;

	public Authorization(String token, String scope, String clientId) {
		Preconditions.checkNotNull(clientId);
		Preconditions.checkNotNull(scope);
		Preconditions.checkNotNull(token);
		this.clientId = clientId;
		this.scope = scope;
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public String getScope() {
		return scope;
	}

	public String getClientId() {
		return clientId;
	}

	public static Authorization parse(Uri uri) {
		String token = uri.getQueryParameter("access_token");
		String scope = uri.getQueryParameter("scope");
		String clientId = uri.getQueryParameter("client_id");
		return token != null ? new Authorization(token, scope, clientId) : null;
	}
}
