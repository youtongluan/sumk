/**
 * Copyright (C) 2016 - 2030 youtongluan.
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

import org.yx.exception.SumkException;

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

	@SuppressWarnings("unchecked")
	public static Map<String, Object> flatToLevel(Map<String, Object> map) {
		Map<String, Object> ret = new HashMap<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String k = entry.getKey();
			Object v = entry.getValue();
			if (!k.contains(".")) {
				ret.put(k, v);
				continue;
			}
			String[] ks = k.split("\\.");
			int lastIndex = ks.length - 1;
			Map<String, Object> temp = ret;
			for (int i = 0; i < lastIndex; i++) {
				String k0 = ks[i];
				Object obj = temp.get(k0);
				if (obj == null) {
					Map<String, Object> temp2 = new HashMap<>();
					temp.put(k0, temp2);
					temp = temp2;
					continue;
				}
				if (Map.class.isInstance(obj)) {
					temp = (Map<String, Object>) obj;
					continue;
				}
				throw new SumkException(546456452, k + " conflit with index " + i);
			}
			temp.put(ks[lastIndex], v);
		}

		return ret;
	}
}
