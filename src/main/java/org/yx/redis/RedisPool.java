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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.yx.log.Logs;

public final class RedisPool {

	private static Map<String, Redis> cache = Collections.emptyMap();

	private static Redis _defaultRedis;

	static void setDefaultRedis(Redis r) {
		_defaultRedis = r;
	}

	static List<String> keys() {
		return new ArrayList<String>(cache.keySet());
	}

	public static Redis get(String alias) {
		if (alias == null) {
			return _defaultRedis;
		}
		return cache.getOrDefault(alias, _defaultRedis);
	}

	public static Redis getRedisExactly(String alias) {
		return cache.get(alias);
	}

	public static Redis defaultRedis() {
		return _defaultRedis;
	}

	public static synchronized void put(String alias, Redis redis) {
		Map<String, Redis> map = new HashMap<>(cache);
		Redis old = map.put(alias, Objects.requireNonNull(redis));
		cache = map;
		Logs.redis().trace("redis name {} : {}", alias, redis);
		if (old != null) {
			old.shutdownPool();
			Logs.redis().info("shutdown old redis {} : {}", alias, redis);
		}
	}

	public static void shutdown() {
		Set<Redis> redises = new HashSet<>(cache.values());
		if (_defaultRedis != null) {
			redises.add(_defaultRedis);
		}

		for (Redis r : redises) {
			r.shutdownPool();
		}
		cache = Collections.emptyMap();
	}
}
