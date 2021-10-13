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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yx.common.Host;
import org.yx.conf.AppInfo;

import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.redis.RedisConfig;
import org.yx.redis.RedisSettings;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.params.SetParams;

public class Redis3Kit {

	public static SetParams createSetParams(String nxxx, String expx, long time) {
		SetParams params = SetParams.setParams();
		if (nxxx != null) {
			if ("NX".equalsIgnoreCase(nxxx)) {
				params.nx();
			} else if ("XX".equalsIgnoreCase(nxxx)) {
				params.xx();
			} else {
				throw new SumkException(-234325, "nxxx参数错误：" + nxxx);
			}
		}
		if (expx != null) {
			if ("EX".equalsIgnoreCase(expx)) {
				params.ex(time);
			} else if ("PX".equalsIgnoreCase(expx)) {
				params.px(time);
			} else {
				throw new SumkException(-234325, "expx参数错误：" + expx);
			}
		}
		return params;
	}

	@SuppressWarnings("unchecked")
	public static JedisPool createJedisPool(RedisConfig conf) {
		List<Host> hosts = RedisSettings.parseHosts(conf.hosts());
		Host h = hosts.get(0);
		try {

			return new JedisPool(conf, h.ip(), h.port(), conf.getConnectionTimeout(), conf.getTimeout(), conf.getUser(),
					conf.getPassword(), conf.getDb(), AppInfo.appId("sumk"));
		} catch (Throwable e) {
			Logs.redis().info("JedisPool ignore property user");
		}
		try {

			return new JedisPool(conf, h.ip(), h.port(), conf.getConnectionTimeout(), conf.getTimeout(),
					conf.getPassword(), conf.getDb(), AppInfo.appId("sumk"));
		} catch (Throwable e) {
			Logs.redis().info("JedisPool ignore property connectionTimeout");
		}
		return new JedisPool(conf, h.ip(), h.port(), conf.getTimeout(), conf.getPassword(), conf.getDb(),
				AppInfo.appId("sumk"));
	}

	@SuppressWarnings("unchecked")
	public static JedisSentinelPool createSentinelPool(RedisConfig config) {
		List<Host> hosts = RedisSettings.parseHosts(config.hosts());
		Set<String> sentinels = new HashSet<>();
		for (Host h : hosts) {
			sentinels.add(h.toString());
		}
		Logs.redis().info("create sentinel redis pool,sentinels={},db={}", sentinels, config.getDb());
		try {
			return new JedisSentinelPool(config.getMaster(), sentinels, config, config.getConnectionTimeout(),
					config.getTimeout(), config.getUser(), config.getPassword(), config.getDb(), AppInfo.appId("sumk"));
		} catch (Throwable e) {
			Logs.redis().info("JedisCluster ignore property user");
		}

		return new JedisSentinelPool(config.getMaster(), sentinels, config, config.getConnectionTimeout(),
				config.getTimeout(), config.getPassword(), config.getDb(), AppInfo.appId("sumk"));
	}

	public static RedisCluster createJedisCluster(RedisConfig config) {
		List<Host> hosts = RedisSettings.parseHosts(config.hosts());
		Set<HostAndPort> hps = new HashSet<>();
		for (Host h : hosts) {
			hps.add(new HostAndPort(h.ip(), h.port()));
		}
		Logs.redis().info("create JedisCluster redis pool,hosts={}", hps);
		try {
			return new RedisCluster(hps, config.getConnectionTimeout(), config.getTimeout(), config.getMaxAttempts(),
					config.getUser(), config.getPassword(), AppInfo.appId("sumk"), config);
		} catch (Throwable e) {
			Logs.redis().info("JedisCluster ignore property user");
		}

		try {
			return new RedisCluster(hps, config.getConnectionTimeout(), config.getTimeout(), config.getMaxAttempts(),
					config.getPassword(), AppInfo.appId("sumk"), config);
		} catch (Throwable e) {
			Logs.redis().info("JedisCluster ignore property clientName");
		}

		return new RedisCluster(hps, config.getConnectionTimeout(), config.getTimeout(), config.getMaxAttempts(),
				config.getPassword(), config);
	}
}
