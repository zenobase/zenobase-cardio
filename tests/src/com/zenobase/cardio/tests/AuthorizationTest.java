package com.zenobase.cardio.tests;

import static com.zenobase.cardio.tests.AuthorizationAssert.assertThat;

import junit.framework.TestCase;
import android.net.Uri;

import com.zenobase.cardio.Authorization;

public class AuthorizationTest extends TestCase {

	public void test() {
		assertThat(parse("?error=foo")).isNull();
		assertThat(parse("?access_token=foo&scope=bar&client_id=baz")).hasToken("foo").hasScope("bar").hasClientId("baz");
	}

	private static Authorization parse(String uri) {
		return Authorization.parse(Uri.parse(uri));
	}
}
