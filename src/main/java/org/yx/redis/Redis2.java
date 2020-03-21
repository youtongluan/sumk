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

import static org.yx.redis.RedisLoader.LOG_NAME;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.yx.common.Host;
import org.yx.conf.AppInfo;
import org.yx.exception.SimpleSumkException;
import org.yx.exception.SumkException;
import org.yx.log.Log;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.Pool;

public abstract class Redis2 implements SeniorRedis {

	private static final SumkException DIS_CONNECTION_EXCEPTION = new SimpleSumkException(400, "redis is disConnected");
	protected final String hosts;
	protected final int tryCount;
	protected final Pool<Jedis> pool;
	protected boolean disConnected;
	protected final RedisConfig config;

	private static Function<RedisConfig, JedisPool> jedisPoolFactory = conf -> {
		List<Host> hosts = ConfigKit.parseHosts(conf.hosts());
		Host h = hosts.get(0);
		try {
			return new JedisPool(conf, h.ip(), h.port(), conf.getConnectionTimeout(), conf.getTimeout(),
					conf.getPassword(), conf.getDb(), AppInfo.appId("sumk"));
		} catch (Throwable e) {
		}
		Log.get(LOG_NAME).debug("can not support property connectionTimeout");
		return new JedisPool(conf, h.ip(), h.port(), conf.getTimeout(), conf.getPassword(), conf.getDb(),
				AppInfo.appId("sumk"));
	};

	private static Function<RedisConfig, JedisSentinelPool> sentinelPoolFactory = config -> {
		List<Host> hosts = ConfigKit.parseHosts(config.hosts());
		Set<String> sentinels = new HashSet<>();
		for (Host h : hosts) {
			sentinels.add(h.toString());
		}
		Log.get(LOG_NAME).info("create sentinel redis pool,sentinels={},db={}", sentinels, config.getDb());
		return new JedisSentinelPool(config.getMaster(), sentinels, config, config.getConnectionTimeout(),
				config.getTimeout(), config.getPassword(), config.getDb(), AppInfo.appId("sumk"));
	};

	public static Function<RedisConfig, JedisPool> getJedisPoolFactory() {
		return jedisPoolFactory;
	}

	public static void setJedisPoolFactory(Function<RedisConfig, JedisPool> jedisPoolFactory) {
		Redis2.jedisPoolFactory = Objects.requireNonNull(jedisPoolFactory);
	}

	public static Function<RedisConfig, JedisSentinelPool> getSentinelPoolFactory() {
		return sentinelPoolFactory;
	}

	public static void setSentinelPoolFactory(Function<RedisConfig, JedisSentinelPool> sentinelPoolFactory) {
		Redis2.sentinelPoolFactory = Objects.requireNonNull(sentinelPoolFactory);
	}

	public Redis2(RedisConfig config) {
		this.config = Objects.requireNonNull(config);
		this.tryCount = config.getTryCount();
		this.hosts = config.hosts();

		String masterName = config.getMaster();
		this.pool = masterName == null ? jedisPoolFactory.apply(config) : sentinelPoolFactory.apply(config);
		RedisChecker.get().addRedis(this);
	}

	@Override
	public RedisConfig getRedisConfig() {
		return this.config;
	}

	@Override
	public String hosts() {
		return hosts;
	}

	public Jedis jedis() {
		return pool.getResource();
	}

	@Override
	public int tryCount() {
		return tryCount;
	}

	@Override
	public <T> T exec(Function<Jedis, T> callback, int totalCount) {
		if (totalCount < 1) {
			totalCount = 1;
		}
		Jedis jedis = null;
		Exception e1 = null;
		for (int i = 0; i < totalCount; i++) {
			if (this.disConnected) {
				if (this.returnNullForConnectionException()) {
					return null;
				}
				throw DIS_CONNECTION_EXCEPTION;
			}
			try {
				e1 = null;
				jedis = this.jedis();
				return callback.apply(jedis);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).warn("redis连接错误！({})" + e.getMessage(), hosts);
					if (jedis != null) {
						jedis.close();
						jedis = null;
					}
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("redis执行错误！({})" + e.getMessage(), hosts);
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

			if (returnNullForConnectionException() && isConnectException(e1)) {
				return null;
			}
			throw new SumkException(12342422, e1.getMessage(), e1);
		}
		throw new SumkException(12342423, "未知redis异常");
	}

	public <T> T execAndRetry(Function<Jedis, T> callback) {
		return this.exec(callback, this.tryCount);
	}

	protected boolean returnNullForConnectionException() {
		return AppInfo.getBoolean("sumk.redis.disconnect.null", false);
	}

	public void shutdownPool() {
		this.pool.close();
	}

	@Override
	public boolean aliveCheck() {
		final String HELLO = "hello";
		for (int i = 0; i < 3; i++) {
			try (Jedis jedis = jedis()) {
				String ret = jedis.ping(HELLO);
				if (HELLO.equals(ret)) {
					this.disConnected = false;
					return true;
				}
				Log.get(LOG_NAME).warn("redis answer {} for {}", ret, HELLO);
			} catch (Exception e) {
				if (!isConnectException(e)) {
					Log.get(LOG_NAME).error(e.getMessage(), e);
				}
			}
		}
		this.disConnected = true;
		return false;
	}

	private static boolean isConnectException(Exception e) {
		return JedisConnectionException.class.isInstance(e)
				|| (e.getCause() != null && JedisConnectionException.class.isInstance(e.getCause()));
	}

	@Override
	public String toString() {
		return "[hosts=" + hosts + ", db=" + config.getDb() + ", tryCount=" + tryCount + "]";
	}
}
