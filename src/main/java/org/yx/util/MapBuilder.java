package org.yx.util;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder {
	private Map<String, Object> map = new HashMap<>();

	public static MapBuilder create() {
		return new MapBuilder();
	}

	public MapBuilder put(String key, Object value) {
		map.put(key, value);
		return this;
	}

	public Map<String, Object> toMap() {
		return this.map;
	}

	public static MapBuilder create(String key, Object value) {
		return create().put(key, value);
	}
}
