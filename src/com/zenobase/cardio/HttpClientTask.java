package com.zenobase.cardio;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import roboguice.util.Ln;
import roboguice.util.RoboAsyncTask;
import android.content.Context;

public abstract class HttpClientTask extends RoboAsyncTask<Void> {

	protected final Authorization authorization;

	public HttpClientTask(Context context, Authorization authorization) {
		super(context);
		this.authorization = authorization;
	}

	public HttpResponse call(HttpUriRequest request) throws Exception {
		HttpClient client = new DefaultHttpClient();
		request.setHeader("Authorization", "Bearer " + authorization.getToken());
		Ln.d("Request: %s", request.getURI());
		return client.execute(request);
	}
}
