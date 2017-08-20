package org.yx.conf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.yx.log.Log;

public class NamePairs {

	public static final char SPLIT_CHAR = 0x3;

	public static final String SPLIT = new String(new char[] { SPLIT_CHAR });

	private static final Logger LOG = Log.get("sumk.conf");

	private final Map<String, String> map;

	private final String data;

	public NamePairs(final String data) {
		map = new HashMap<>();
		this.data = data;
		if (data == null) {
			return;
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("data:\n" + data.replace(SPLIT_CHAR, '\n'));
		}
		String[] vs = data.split(SPLIT);
		for (String v : vs) {
			v = v.trim();
			if (v.isEmpty()) {
				continue;
			}
			String[] namePair = v.contains("=") ? v.split("=", 2) : v.split(":", 2);
			if (namePair.length < 2) {
				continue;
			}
			this.map.put(namePair[0].trim(), namePair[1].trim());
		}
	}

	private NamePairs(Map<String, String> map, String data) {
		this.map = map;
		this.data = data;
	}

	public String getRawData() {
		return data;
	}

	public Map<String, String> values() {
		return Collections.unmodifiableMap(this.map);
	}

	public NamePairs unmodify() {
		return new NamePairs(Collections.unmodifiableMap(this.map), this.data);
	}

	public String getValue(String key) {
		if (key == null) {
			return null;
		}
		return map.get(key);
	}

	@Override
	public String toString() {
		return map.toString();
	}
}