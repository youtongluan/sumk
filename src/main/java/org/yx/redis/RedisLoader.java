/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yx.bean.Loader;
import org.yx.conf.AppInfo;
import org.yx.conf.SingleResourceLoader;
import org.yx.log.Log;
import org.yx.util.Assert;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

import redis.clients.jedis.JedisPoolConfig;

public class RedisLoader {
	private static JedisPoolConfig defaultConfig = null;

	public static JedisPoolConfig getDefaultConfig() {
		return defaultConfig;
	}

	public static void setDefaultConfig(JedisPoolConfig defaultConfig) {
		RedisLoader.defaultConfig = defaultConfig;
	}

	private static final String SLAVE_PRE = "slave.";
	private static final String REDIS_FILE = "redis.properties";

	public static void init() throws Exception {
		try {
			loadRedisByConfig();
		} catch (Exception e) {
			Log.get("sumk.redis").error("can not load redis config,normal is in {}", REDIS_FILE);
			throw e;
		}
	}

	private static InputStream loadConfig() throws Exception {
		String resourceFactory = AppInfo.get("sumk.redis.conf.loader", "redis.RedisPropertiesLoader");
		if (resourceFactory == null || resourceFactory.isEmpty()) {
			return null;
		}
		Class<?> factoryClz = Loader.loadClass(resourceFactory);
		Assert.isTrue(SingleResourceLoader.class.isAssignableFrom(factoryClz),
				resourceFactory + " should extend from " + SingleResourceLoader.class.getSimpleName());
		SingleResourceLoader factory = (SingleResourceLoader) factoryClz.newInstance();
		return factory.openInput(REDIS_FILE);
	}

	private static void loadRedisByConfig() throws IOException, Exception {
		InputStream in = loadConfig();
		if (in == null) {
			return;
		}
		Map<String, String> p = CollectionUtil.loadMap(in);
		Log.get("sumk.redis").debug("config:{}", p);
		Set<String> keys = p.keySet();
		for (String kk : keys) {
			if (StringUtil.isEmpty(kk)) {
				continue;
			}
			String v = p.get(kk);
			if (kk.startsWith(SLAVE_PRE)) {
				createReadRedis(kk.substring(SLAVE_PRE.length()), v.replace('，', ',').split(","));
				continue;
			}
			Redis redis = create(v);
			String[] moduleKeys = kk.replace('，', ',').split(",");
			for (String key : moduleKeys) {
				key = key.toLowerCase();
				if (StringUtil.isEmpty(key)) {
					continue;
				}
				if (StringUtil.isEmpty(v)) {
					continue;
				}
				if (RedisConstants.DEFAULT.equals(key)) {
					RedisPool._defaultRedis = redis;
				} else {
					RedisPool.put(key, redis);
				}
			}
		}
	}

	private static void createReadRedis(String host, String[] redisParams) throws Exception {
		if (StringUtil.isEmpty(host) || redisParams.length == 0 || !host.contains(":")) {
			return;
		}
		List<RedisParamter> list = new ArrayList<>();
		for (String param : redisParams) {
			param = param.trim();
			if (StringUtil.isEmpty(param)) {
				continue;
			}
			list.add(createParam(param));
		}
		if (list.isEmpty()) {
			return;
		}
		RedisPool.attachRead(host, list.toArray(new RedisParamter[list.size()]));
	}

	private static RedisParamter createParam(String v) throws Exception {
		String[] params = v.split("#");
		String ip = params[0];
		RedisParamter param;
		if (ip.contains(":")) {
			String[] addr = ip.split(":");
			ip = addr[0];
			param = RedisParamter.create(ip, Integer.parseInt(addr[1]));
		} else {
			param = RedisParamter.create(ip);
		}
		if (params.length > 1 && !StringUtil.isEmpty(params[1])) {
			param.setDb(Integer.parseInt(params[1]));
		}
		if (params.length > 2 && !StringUtil.isEmpty(params[2])) {
			param.setPassword(params[2]);
		}
		if (params.length > 3 && !StringUtil.isEmpty(params[3])) {
			param.setTimeout(Integer.parseInt(params[3]));
		}
		if (params.length > 4 && !StringUtil.isEmpty(params[4])) {
			param.setTryCount(Integer.parseInt(params[4]));
		}
		return param;
	}

	private static Redis create(String v) throws Exception {
		return RedisFactory.get(defaultConfig, createParam(v));
	}
}
