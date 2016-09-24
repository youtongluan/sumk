package org.yx.redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RedisRep {
	private static final Map<String, Redis> map = new ConcurrentHashMap<String, Redis>();
	static Redis _defaultRedis;

	/**
	 * 获取已经存在的redis，获取不到就返回默认的，如果连某人的都没有，就返回null
	 * 
	 * @param alias
	 * @return
	 */
	public static Redis get(String alias) {
		Redis r = map.get(alias);
		if (r != null) {
			return r;
		}
		return _defaultRedis;
	}

	public static Redis getRedisExactly(String alias) {
		return map.get(alias);
	}

	/**
	 * 默认的redis
	 * 
	 * @return
	 */
	public static Redis defaultRedis() {
		return _defaultRedis;
	}

	public static void put(String alias, Redis redis) {
		map.putIfAbsent(alias, redis);
	}
}
