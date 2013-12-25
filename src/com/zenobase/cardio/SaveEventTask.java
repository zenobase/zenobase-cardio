package com.zenobase.cardio;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import roboguice.util.Ln;
import android.content.Context;
import android.widget.Toast;
import com.google.common.base.Preconditions;

public class SaveEventTask extends HttpClientTask {

	private final Event event;

	public SaveEventTask(Context context, Authorization authorization, Event event) {
		super(context, authorization);
		this.event = event;
	}

	@Override
	public Void call() throws Exception {
		HttpPost post = new HttpPost(context.getText(R.string.host) + "/buckets/" + authorization.getScope() + "/");
		post.setHeader("Content-Type", "application/json");
		post.setEntity(new StringEntity(event.toJson().toString()));
		HttpResponse response = call(post);
		Preconditions.checkState(response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED);
		return null;
	}

	@Override
	protected void onSuccess(Void v) {
		showToast(R.string.task_save_success);
	}

	@Override
	protected void onException(Exception e) {
		Ln.e(e);
		showToast(R.string.task_save_error);
	}

	private void showToast(int resId) {
		Toast.makeText(getContext(), resId, Toast.LENGTH_LONG).show();
	}
}
