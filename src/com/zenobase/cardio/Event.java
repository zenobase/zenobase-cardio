package com.zenobase.cardio;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

public class Event {

	private static final SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	private final Date timestamp;
	private final long duration;
	private final String tag;
	private final int frequency;
	private final int rating;

	public Event(Date timestamp, String tag, long duration, int frequency, int rating) {
		this.timestamp = timestamp;
		this.tag = tag;
		this.duration = duration;
		this.frequency = frequency;
		this.rating = rating;
	}

	public int getFrequency() {
		return frequency;
	}

	public int getRating() {
		return rating;
	}

	public ObjectNode toJson() {
		ObjectNode node = new ObjectMapper().createObjectNode();
		node.put("timestamp", ISO8601.format(timestamp));
		node.putArray("tag").add(tag);
		node.put("duration", duration);
		ObjectNode frequencyNode = node.putObject("frequency");
		frequencyNode.put("@value", frequency);
		frequencyNode.put("unit", "bpm");
		node.put("rating", rating);
		return node;
	}
}
