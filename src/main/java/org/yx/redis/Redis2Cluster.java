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

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.yx.conf.AppInfo;
import org.yx.log.Logs;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisClusterCommand;
import redis.clients.jedis.JedisPool;

public class Redis2Cluster extends JedisCluster implements Redis, Redis2 {

	protected String hosts;
	protected RedisConfig config;

	public Redis2Cluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts,
			String password, String clientName, RedisConfig redisConfig) {
		super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, clientName, redisConfig);
		this.config = redisConfig;
	}

	public Redis2Cluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts,
			String password, RedisConfig redisConfig) {
		super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, redisConfig);
		this.config = redisConfig;
	}

	@Override
	public Collection<byte[]> hvals(byte[] key) {
		return super.hvals(key);
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
		try {
			super.close();
		} catch (IOException e) {
			Logs.redis().error("关闭jedisCluster连接失败", e);
		}
	}

	@Override
	public String toString() {
		return "Redis[hosts=" + hosts + ", db=" + config.getDb() + "]";
	}

	@Override
	public String scriptLoad(String script) {
		String ret = null;
		for (JedisPool pool : this.getClusterNodes().values()) {
			try (Jedis jedis = pool.getResource()) {
				ret = jedis.scriptLoad(script);
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

}
