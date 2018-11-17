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

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.util.Assert;

import redis.clients.jedis.JedisPoolConfig;

public class RedisFactory {

	private static final Map<String, Redis> map = new ConcurrentHashMap<>();
	private static Constructor<?> constructor;

	private static String toMapKey(RedisParamter p) {
		StringBuilder sb = new StringBuilder();
		return sb.append(p.hosts()).append('#').append(p.getDb()).append('#').append(p.getTimeout()).append('#')
				.append(p.getTryCount()).toString();
	}

	public static Redis get(String ip) {
		RedisParamter p = RedisParamter.create(ip);
		return get(null, p);
	}

	public static Redis get(String ip, int port) {
		RedisParamter p = RedisParamter.create(ip + ":" + port);
		return get(null, p);
	}

	public static Redis get(String ip, int port, int timeout) {
		RedisParamter p = RedisParamter.create(ip + ":" + port);
		p.setTimeout(timeout);
		return get(null, p);
	}

	private static Constructor<?> getConstructor()
			throws NoSuchMethodException, SecurityException, ClassNotFoundException {
		String clzName = AppInfo.get("sumk.redis.impl", RedisImpl.class.getName());
		Class<?> clz = Class.forName(clzName);
		Constructor<?> c = clz.getConstructor(JedisPoolConfig.class, RedisParamter.class);
		c.setAccessible(true);
		return c;
	}

	public static Redis get(GenericObjectPoolConfig<?> config, RedisParamter p) {
		Assert.notNull(p, "redis paramter cannot be null");
		if (p.getTryCount() < 1 || p.getTryCount() > 100) {
			throw new SumkException(54354354, "tryCount必须介于0和100之间");
		}
		final String key = toMapKey(p);
		Redis redis = map.get(key);
		if (redis == null) {
			synchronized (RedisFactory.class) {
				if (map.containsKey(key)) {
					return map.get(key);
				}
				try {
					if (constructor == null) {
						constructor = getConstructor();
					}
					redis = (Redis) constructor.newInstance(config, p);
					map.put(key, redis);
				} catch (Exception e) {
					Log.printStack(Redis.LOG_NAME, e);
					throw new SumkException(2345345, "create redis [" + key + "] failed!!");
				}

			}
		}
		return redis;
	}

	public static String status() {
		return map.keySet().toString();
	}

}
