package org.yx.redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.yx.log.Log;

public class RedisPool {
	private static final Map<String, Redis> map = new ConcurrentHashMap<>();

	private static final Map<String, RedisParamter[]> readParamsMap = new ConcurrentHashMap<>();

	static Redis _defaultRedis;
	static {
		try {
			RedisLoader.init();
		} catch (Exception e) {
			Log.printStack(e);
			System.exit(-1);
		}
	}

	/**
	 * 获取已经存在的redis，获取不到就返回默认的，如果连默认的都没有，就返回null
	 * 
	 * @param alias
	 * @return
	 */
	public static Redis get(String alias) {
		if (alias == null) {
			return _defaultRedis;
		}
		alias = alias.toLowerCase();
		Redis r = map.get(alias);
		if (r != null) {
			return r;
		}
		return _defaultRedis;
	}

	public static Redis getRedisExactly(String alias) {
		alias = alias.toLowerCase();
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
		map.putIfAbsent(alias.toLowerCase(), redis);
	}

	static void attachRead(String host, RedisParamter[] reads) {
		readParamsMap.put(host, reads);
	}
}
