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
import java.io.StringReader;
import java.util.Map;
import java.util.Set;

import org.yx.conf.AppInfo;
import org.yx.conf.NamePairs;
import org.yx.log.Log;
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

	private static final String REDIS_FILE = "redis.properties";

	public static void init() throws Exception {
		try {
			loadRedisByConfig();
		} catch (Exception e) {
			Log.get(Redis.LOG_NAME).error("can not load redis config,normal is in {}", REDIS_FILE);
			throw e;
		}
	}

	private static byte[] loadConfig() throws Exception {
		Map<String, String> map = AppInfo.subMap("s.redis.");
		return new NamePairs(map).toBytes();
	}

	private static void loadRedisByConfig() throws IOException, Exception {
		byte[] bs = loadConfig();
		if (bs == null || bs.length == 0) {
			return;
		}
		Map<String, String> p = CollectionUtil.loadMap(new StringReader(new String(bs, AppInfo.systemCharset())),
				false);
		Log.get(Redis.LOG_NAME).debug("config:{}", p);
		Set<String> keys = p.keySet();
		for (String kk : keys) {
			if (StringUtil.isEmpty(kk)) {
				continue;
			}
			String v = p.get(kk);
			kk = kk.toLowerCase();
			Redis redis = create(kk, v);
			String[] moduleKeys = kk.replace('，', ',').split(",");
			for (String key : moduleKeys) {
				if (StringUtil.isEmpty(key)) {
					continue;
				}
				if (StringUtil.isEmpty(v)) {
					continue;
				}
				if (RedisConfig.DEFAULT.equals(key)) {
					RedisPool._defaultRedis = redis;
					Log.get(Redis.LOG_NAME).debug("set default redis to {}", redis);
				} else {
					RedisPool.put(key, redis);
				}
			}
		}
	}

	private static RedisParamter createParam(String v) throws Exception {
		String[] params = v.split("#");
		RedisParamter param = RedisParamter.create(params[0]);
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

	private static Redis create(String name, String v) throws Exception {
		Log.get(Redis.LOG_NAME).trace("create redis {} with {}", name, v);
		JedisPoolConfig config = JedisPoolConfigHolder.getConfig(name);
		if (config == null) {
			config = defaultConfig;
		}
		return RedisFactory.get(config, createParam(v));
	}
}
