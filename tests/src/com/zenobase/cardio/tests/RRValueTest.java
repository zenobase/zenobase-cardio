package com.zenobase.cardio.tests;

import junit.framework.TestCase;
import com.google.common.testing.EqualsTester;

import com.zenobase.cardio.RRValue;

public class RRValueTest extends TestCase {

	public void test() {
		new EqualsTester()
			.addEqualityGroup(new RRValue(0), new RRValue(0))
			.addEqualityGroup(new RRValue(1))
			.testEquals();
	}
}
