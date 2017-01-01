package org.yx.util;

import java.util.HashMap;
import java.util.Map;

public class SBuilder {
	public static MapBuilder map() {
		return new MapBuilder();
	}

	public static MapBuilder map(String key, Object value) {
		return map().put(key, value);
	}

	public static class MapBuilder {
		private Map<String, Object> map = new HashMap<>();

		public MapBuilder put(String key, Object value) {
			map.put(key, value);
			return this;
		}

		public Map<String, Object> toMap() {
			return this.map;
		}

	}
}
