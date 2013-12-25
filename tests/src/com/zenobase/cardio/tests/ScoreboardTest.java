package com.zenobase.cardio.tests;

import static com.zenobase.cardio.tests.ScoreboardAssert.assertThat;
import static org.mockito.Mockito.mock;

import android.test.AndroidTestCase;
import com.squareup.otto.Bus;

import com.zenobase.cardio.RRValue;
import com.zenobase.cardio.Scoreboard;

public class ScoreboardTest extends AndroidTestCase {

	@Override
	public void setUp() {
		System.setProperty("dexmaker.dexcache", getContext().getCacheDir().toString());
	}

	public void test() {

		Bus bus = mock(Bus.class);
		Scoreboard scoreboard = new Scoreboard(bus);
		assertThat(scoreboard).isEmpty();

		scoreboard.add(new RRValue(1000));
		assertThat(scoreboard).isEmpty();

		scoreboard.setEnabled(true);
		assertThat(scoreboard).isEmpty();

		scoreboard.add(new RRValue(1000));
		assertThat(scoreboard).hasCount(1).hasSDNN(0).hasScores(0, 1000);

		scoreboard.add(new RRValue(900));
		assertThat(scoreboard).hasCount(2).hasSDNN(50).hasScores(0, 900, 1000);

		scoreboard.add(new RRValue(1100));
		assertThat(scoreboard).hasCount(3).hasSDNN(82).hasScores(0, 900, 1000, 1100);

		scoreboard.reset();
		assertThat(scoreboard).isEmpty();
	}
}
