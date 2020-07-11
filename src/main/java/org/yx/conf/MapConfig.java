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
package org.yx.conf;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MapConfig implements SystemConfig {

	private Map<String, String> map = new ConcurrentHashMap<>();

	public static MapConfig create() {
		return new MapConfig();
	}

	public Map<String, String> map() {
		return map;
	}

	public void replace(Map<String, String> config) {
		map = Objects.requireNonNull(config);
	}

	@Override
	public String get(String key) {
		return map.get(key);
	}

	@Override
	public Set<String> keys() {
		return new HashSet<>(map.keySet());
	}

	public MapConfig put(String k, String v) {
		map.put(k, v);
		return this;
	}

	public String remove(String key) {
		return this.map.remove(key);
	}

	public MapConfig putKV(String kv) {
		map.put(kv.split("=")[0].trim(), kv.split("=")[1].trim());
		return this;
	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}

	@Override
	public String toString() {
		return "MapConfig " + map;
	}

}
