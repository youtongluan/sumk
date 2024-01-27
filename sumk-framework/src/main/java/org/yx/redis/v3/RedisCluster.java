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
package org.yx.redis.v3;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.yx.conf.AppInfo;
import org.yx.log.Logs;
import org.yx.redis.Redis;
import org.yx.redis.RedisConfig;
import org.yx.redis.RedisType;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisClusterCommand;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

public class RedisCluster extends JedisCluster implements Redis, Redis3x {

	protected final String hosts;
	protected final RedisConfig config;

	@SuppressWarnings("unchecked")
	public RedisCluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts,
			String user, String password, String clientName, RedisConfig redisConfig) {
		super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, user, password, clientName, redisConfig);
		this.config = redisConfig;
		this.hosts = jedisClusterNode.toString();
	}

	@SuppressWarnings("unchecked")
	public RedisCluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts,
			String password, String clientName, RedisConfig redisConfig) {
		super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, clientName, redisConfig);
		this.config = redisConfig;
		this.hosts = jedisClusterNode.toString();
	}

	@SuppressWarnings("unchecked")

	public RedisCluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts,
			String password, RedisConfig redisConfig) {
		super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, redisConfig);
		this.config = redisConfig;
		this.hosts = jedisClusterNode.toString();
	}

	@Override
	public List<String> blpop(String... args) {

		return super.blpop(AppInfo.getInt("sumk.redis.blpop.timeout", 1000), args);
	}

	@Override
	public List<String> brpop(String... args) {
		return super.brpop(AppInfo.getInt("sumk.redis.brpop.timeout", 1000), args);
	}

	@Override
	public String watch(String... keys) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String unwatch() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String randomKey() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String hosts() {
		return this.hosts;
	}

	@Override
	public RedisConfig getRedisConfig() {
		return this.config;
	}

	@Override
	public void shutdownPool() {
		super.close();
	}

	@Override
	public String toString() {
		return "RedisCluster " + hosts;
	}

	@Override
	public String scriptLoad(String script) {
		String ret = null;
		for (Map.Entry<String, JedisPool> en : this.getClusterNodes().entrySet()) {
			try (Jedis jedis = en.getValue().getResource()) {
				ret = jedis.scriptLoad(script);
			} catch (RuntimeException e) {
				Logs.redis().error("分布式锁在{}初始化脚本失败,原因是：{}", en.getKey(), e);
				throw e;
			}

		}
		return ret;
	}

	@Override
	public <T> T execute(String key, Function<Jedis, T> callback) {
		return new JedisClusterCommand<T>(connectionHandler, maxAttempts) {
			@Override
			public T execute(Jedis connection) {
				return callback.apply(connection);
			}
		}.run(key);
	}

	@Override
	public Redis mute() {
		return this;
	}

	@Override
	public boolean isMuted() {
		return false;
	}

	@Override
	public RedisType redisType() {
		return RedisType.CLUSTER;
	}

	@Override
	public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time) {
		String NXXX = new String(nxxx, StandardCharsets.UTF_8);
		String EXPX = new String(expx, StandardCharsets.UTF_8);
		SetParams params = Redis3Kit.createSetParams(NXXX, EXPX, time);
		return super.set(key, value, params);
	}

	@Override
	public String set(String key, String value, String nxxx, String expx, long time) {
		SetParams params = Redis3Kit.createSetParams(nxxx, expx, time);
		return super.set(key, value, params);
	}

	@Override
	public String set(String key, String value, String expx, long time) {
		SetParams params = Redis3Kit.createSetParams(null, expx, time);
		return super.set(key, value, params);
	}

	@Override
	public String set(String key, String value, String nxxx) {
		SetParams params = Redis3Kit.createSetParams(nxxx, null, -1);
		return super.set(key, value, params);
	}

	@Override
	public String restore(String key, int ttl, byte[] serializedValue) {
		return super.restore(key, (long) ttl, serializedValue);
	}

	@Override
	public Long expire(String key, int seconds) {
		return super.expire(key, (long) seconds);
	}

	@Override
	public String setex(String key, int seconds, String value) {
		return super.setex(key, (long) seconds, value);
	}

	@Override
	public Long move(String key, int dbIndex) {
		return -1L;
	}

}
