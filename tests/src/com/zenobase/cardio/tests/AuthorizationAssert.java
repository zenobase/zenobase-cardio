package com.zenobase.cardio.tests;

import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.Assertions;

import com.zenobase.cardio.Authorization;

public class AuthorizationAssert extends AbstractAssert<AuthorizationAssert, Authorization> {

	public static AuthorizationAssert assertThat(Authorization actual) {
		return new AuthorizationAssert(actual);
	}

	private AuthorizationAssert(Authorization actual) {
		super(actual, AuthorizationAssert.class);
	}

	public AuthorizationAssert hasToken(String token) {
		Assertions.assertThat(actual).isNotNull();
		Assertions.assertThat(actual.getToken()).as("token").isEqualTo(token);
		return this;
	}

	public AuthorizationAssert hasScope(String scope) {
		Assertions.assertThat(actual).isNotNull();
		Assertions.assertThat(actual.getScope()).as("scope").isEqualTo(scope);
		return this;
	}

	public AuthorizationAssert hasClientId(String clientId) {
		Assertions.assertThat(actual).isNotNull();
		Assertions.assertThat(actual.getClientId()).as("client ID").isEqualTo(clientId);
		return this;
	}
}
