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

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.yx.common.Host;
import org.yx.conf.AppInfo;
import org.yx.log.Logs;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

public final class Jedis2Factorys {

	private static Function<RedisConfig, JedisPool> jedisPoolFactory;

	private static Function<RedisConfig, JedisSentinelPool> sentinelPoolFactory;

	private static Function<RedisConfig, Redis2Cluster> jedisClusterFactory;

	public static Function<RedisConfig, JedisPool> getJedisPoolFactory() {
		return jedisPoolFactory != null ? jedisPoolFactory : conf -> {
			List<Host> hosts = RedisSettings.parseHosts(conf.hosts());
			Host h = hosts.get(0);
			try {

				return new JedisPool(conf, h.ip(), h.port(), conf.getConnectionTimeout(), conf.getTimeout(),
						conf.getPassword(), conf.getDb(), AppInfo.appId("sumk"));
			} catch (Throwable e) {
			}
			Logs.redis().debug("can not support property connectionTimeout");
			return new JedisPool(conf, h.ip(), h.port(), conf.getTimeout(), conf.getPassword(), conf.getDb(),
					AppInfo.appId("sumk"));
		};
	}

	public static void setJedisPoolFactory(Function<RedisConfig, JedisPool> jedisPoolFactory) {
		Jedis2Factorys.jedisPoolFactory = Objects.requireNonNull(jedisPoolFactory);
	}

	public static Function<RedisConfig, JedisSentinelPool> getSentinelPoolFactory() {
		return sentinelPoolFactory != null ? sentinelPoolFactory : config -> {
			List<Host> hosts = RedisSettings.parseHosts(config.hosts());
			Set<String> sentinels = new HashSet<>();
			for (Host h : hosts) {
				sentinels.add(h.toString());
			}
			Logs.redis().info("create sentinel redis pool,sentinels={},db={}", sentinels, config.getDb());

			return new JedisSentinelPool(config.getMaster(), sentinels, config, config.getConnectionTimeout(),
					config.getTimeout(), config.getPassword(), config.getDb(), AppInfo.appId("sumk"));
		};
	}

	public static void setSentinelPoolFactory(Function<RedisConfig, JedisSentinelPool> sentinelPoolFactory) {
		Jedis2Factorys.sentinelPoolFactory = Objects.requireNonNull(sentinelPoolFactory);
	}

	public static Function<RedisConfig, Redis2Cluster> getJedisClusterFactory() {
		return jedisClusterFactory != null ? jedisClusterFactory : config -> {
			List<Host> hosts = RedisSettings.parseHosts(config.hosts());
			Set<HostAndPort> hps = new HashSet<>();
			for (Host h : hosts) {
				hps.add(new HostAndPort(h.ip(), h.port()));
			}
			Logs.redis().info("create JedisCluster redis pool,hosts={}", hps);
			try {
				return new Redis2Cluster(hps, config.getConnectionTimeout(), config.getTimeout(),
						config.getMaxAttempts(), config.getPassword(), AppInfo.appId("sumk"), config);
			} catch (Throwable e) {
				Logs.redis().debug("can not support property clientName");

				return new Redis2Cluster(hps, config.getConnectionTimeout(), config.getTimeout(),
						config.getMaxAttempts(), config.getPassword(), config);
			}

		};
	}

	public static void setJedisCluster(Function<RedisConfig, Redis2Cluster> jedisClusterFactory) {
		Jedis2Factorys.jedisClusterFactory = Objects.requireNonNull(jedisClusterFactory);
	}

}
