package org.yx.redis;

import redis.clients.jedis.Jedis;

@FunctionalInterface
public interface RedisCallBack<T> {
	public T invoke(Jedis jedis);
}
