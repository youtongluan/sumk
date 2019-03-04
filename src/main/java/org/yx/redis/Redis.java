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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.rpc.Host;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.BinaryScriptingCommands;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.MultiKeyBinaryCommands;
import redis.clients.jedis.MultiKeyCommands;
import redis.clients.jedis.ScriptingCommands;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.Pool;
import redis.clients.util.SafeEncoder;

public abstract class Redis implements BinaryJedisCommands, MultiKeyBinaryCommands, BinaryScriptingCommands,
		JedisCommands, MultiKeyCommands, ScriptingCommands {

	static final String LOG_NAME = "sumk.redis";
	protected final List<Host> hosts;
	protected final int db;
	protected final int tryCount;
	protected final Pool<Jedis> pool;
	private static final Charset UTF8 = StandardCharsets.UTF_8;

	protected JedisPoolConfig defaultPoolConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMinIdle(AppInfo.getInt("sumk.redis.minidle", 1));
		config.setMaxIdle(AppInfo.getInt("sumk.redis.maxidle", 20));
		config.setMaxTotal(AppInfo.getInt("sumk.redis.maxtotal", 100));
		config.setTestWhileIdle(true);
		config.setTimeBetweenEvictionRunsMillis(AppInfo.getInt("sumk.redis.timebetweenevictionrunsmillis", 5 * 60000));
		config.setNumTestsPerEvictionRun(AppInfo.getInt("sumk.redis.numtestsperevictionrun", 3));
		return config;
	}

	public Redis(JedisPoolConfig config, RedisParamter p) {
		this.tryCount = p.getTryCount();
		this.hosts = p.hosts();

		this.db = p.getDb();
		if (config == null) {
			config = defaultPoolConfig();
		}
		String masterName = p.masterName();
		if (masterName == null) {
			Host host = p.hosts().get(0);

			this.pool = new JedisPool(config, host.ip(), host.port(), p.getTimeout(), p.getPassword(), p.getDb(),
					AppInfo.appId("sumk"));
			return;
		}

		List<Host> hosts = p.hosts();
		Set<String> sentinels = new HashSet<>();
		for (Host h : hosts) {
			sentinels.add(h.toString());
		}
		Log.get(LOG_NAME).info("create sentinel redis pool,sentinels={},db={}", sentinels, p.getDb());
		this.pool = new JedisSentinelPool(masterName, sentinels, config, p.getTimeout(), p.getTimeout(),
				p.getPassword(), p.getDb(), AppInfo.appId("sumk"));
	}

	/**
	 * @return redis的主机地址，如果存在多个，就用逗号分隔
	 */
	public List<Host> getHosts() {
		return hosts;
	}

	public int getDb() {
		return db;
	}

	protected Jedis jedis() {
		return pool.getResource();
	}

	public int getTryCount() {
		return tryCount;
	}

	public <T> T exec(RedisCallBack<T> callback) {
		return new RedisTemplate(this).execute(callback);
	}

	protected void handleRedisException(Exception e1) {
		if (e1 != null) {
			throw new SumkException(12342422, e1.getMessage(), e1);
		}
	}

	protected boolean isConnectException(Exception e) {
		return JedisConnectionException.class.isInstance(e)
				|| (e.getCause() != null && JedisConnectionException.class.isInstance(e.getCause()));
	}

	protected void close(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	public Long hset(String key, String field, byte[] value) {
		Jedis jedis = null;
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			try {
				jedis = pool.getResource();
				return jedis.hset(SafeEncoder.encode(key), SafeEncoder.encode(field), value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error("redis connection failed！" + e.getMessage(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	public String setex(String key, int seconds, byte[] value) {
		Jedis jedis = null;
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			try {
				jedis = pool.getResource();
				return jedis.setex(key.getBytes(UTF8), seconds, value);
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error("redis connection failed！" + e.getMessage(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	public byte[] getBytes(String key) {
		Jedis jedis = null;
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			try {
				jedis = pool.getResource();
				return jedis.get(key.getBytes(UTF8));
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error("redis connection failed！" + e.getMessage(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	public byte[] hgetBinarry(String key, String field) {
		Jedis jedis = null;
		Exception e1 = null;
		for (int i = 0; i < tryCount; i++) {
			try {
				jedis = pool.getResource();
				return jedis.hget(SafeEncoder.encode(key), SafeEncoder.encode(field));
			} catch (Exception e) {
				if (isConnectException(e)) {
					Log.get(LOG_NAME).error("redis connection failed！" + e.getMessage(), e);
					e1 = e;
					continue;
				}
				Log.get(LOG_NAME).error("redis execute error！" + e.getMessage(), e);
				SumkException.throwException(12342411, e.getMessage(), e);
			} finally {
				close(jedis);
			}
		}
		handleRedisException(e1);
		throw new SumkException(12342423, "未知redis异常");
	}

	@Override
	public String toString() {
		return "[hosts=" + hosts + ", db=" + db + ", tryCount=" + tryCount + "]";
	}

	public void shutdown() {
		this.pool.close();
	}
}
