package com.zenobase.cardio;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class Scoreboard {

	private final List<Integer> values = Lists.newArrayList();
	private boolean enabled;

	@Inject
	public Scoreboard(Bus bus) {
		bus.register(this);
	}

	@Subscribe
	public void add(RRValue value) {
		if (enabled) {
			values.add(value.getValue());
		}
	}

	public int getLast() {
		return Iterables.getLast(values, 0);
	}

	public List<Integer> getLast(int count) {
		List<Integer> last = Lists.newArrayListWithCapacity(count);
		int offset = values.size() - count;
		for (int i = 0; i < count; ++i) {
			last.add(i + offset >= 0 ? values.get(i + offset) : 0);
		}
		return last;
	}

	public int getCount() {
		return values.size();
	}

	public int getHR() {
		List<Integer> sorted = Lists.newArrayList(values);
		Collections.sort(sorted);
		return 60000 / sorted.get(sorted.size() / 2);
	}

	public int getSDNN() {
		final int n = values.size();
		if (n < 2) {
			return 0;
		}
		double avg = Iterables.getFirst(values, 0);
		double sum = 0;
		for (int i = 1; i < n; ++i) {
			double newavg = avg + (values.get(i) - avg) / (i + 1);
			sum += (values.get(i) - avg) * (values.get(i) - newavg);
			avg = newavg;
		}
		return Ints.saturatedCast(Math.round(Math.sqrt(sum / n)));
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void reset() {
		values.clear();
	}
}
