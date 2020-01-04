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
package org.yx.redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import redis.clients.jedis.JedisPoolConfig;

public final class JedisPoolConfigHolder {
	private static Map<String, JedisPoolConfig> map = new ConcurrentHashMap<>();

	public static void putConfig(String name, JedisPoolConfig config) {
		map.put(name, config);
	}

	public static JedisPoolConfig getConfig(String name) {
		return map.get(name);
	}

	public static void removeConfig(String name) {
		map.remove(name);
	}
}
