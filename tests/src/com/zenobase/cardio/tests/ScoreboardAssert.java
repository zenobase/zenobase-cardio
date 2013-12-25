package com.zenobase.cardio.tests;

import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.Assertions;

import com.zenobase.cardio.Scoreboard;

public class ScoreboardAssert extends AbstractAssert<ScoreboardAssert, Scoreboard> {

	public static ScoreboardAssert assertThat(Scoreboard actual) {
		return new ScoreboardAssert(actual);
	}

	private ScoreboardAssert(Scoreboard actual) {
		super(actual, ScoreboardAssert.class);
	}

	public ScoreboardAssert hasCount(int count) {
		Assertions.assertThat(actual.getCount()).as("count").isEqualTo(count);
		return this;
	}

	public ScoreboardAssert hasSDNN(int value) {
		Assertions.assertThat(actual.getSDNN()).as("SDNN").isEqualTo(value);
		return this;
	}

	public ScoreboardAssert hasScores(Integer... scores) {
		Assertions.assertThat(actual.getLast(10)).as("number of scores").hasSize(10);
		Assertions.assertThat(actual.getLast(10)).as("unique scores").containsOnly(scores);
		return this;
	}

	public ScoreboardAssert isEmpty() {
		return hasCount(0).hasSDNN(0).hasScores(0);
	}
}
