/**
 * Copyright (C) 2016 - 2017 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yx.util;

import java.util.HashMap;
import java.util.Map;

public class SBuilder {
	public static boolean isMap(Object obj) {
		return Map.class.isInstance(obj);
	}

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
