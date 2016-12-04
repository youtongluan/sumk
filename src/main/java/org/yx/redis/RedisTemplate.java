package org.yx.redis;

import org.yx.exception.SumkException;
import org.yx.log.Log;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisTemplate {
	private Redis redis;

	public RedisTemplate(Redis redis) {
		this.redis = redis;
	}

	public <T> T execute(RedisCallBack<T> callback) {
		Jedis jedis = null;
		Exception e1 = null;
		for (int i = 0; i < redis.getTryCount(); i++) {
			try {
				e1 = null;
				jedis = redis.jedis();
				return callback.invoke(jedis);
			} catch (Exception e) {
				if (JedisConnectionException.class.isInstance(e)
						|| (e.getCause() != null && JedisConnectionException.class.isInstance(e.getCause()))) {
					Log.get("Redis.get#1").error("redis连接错误！" + e.getMessage(), e);
					if (jedis != null) {
						jedis.close();
						jedis = null;
					}
					e1 = e;
					continue;
				}
				Log.get("Redis.get#2").error("redis执行错误！" + e.getMessage(), e);
				if (jedis != null) {
					jedis.close();
					jedis = null;
				}
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
		}
		if (e1 != null) {
			throw new SumkException(12342422, e1.getMessage(), e1);
		}
		throw new SumkException(12342423, "未知redis异常");
	}
}
