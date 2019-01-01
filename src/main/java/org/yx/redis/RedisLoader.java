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
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.yx.bean.Loader;
import org.yx.conf.AppInfo;
import org.yx.conf.SingleResourceLoader;
import org.yx.log.Log;
import org.yx.util.Assert;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

public class RedisLoader {
	private static GenericObjectPoolConfig<?> defaultConfig = null;

	private static SingleResourceLoader resourceLoader;

	public static GenericObjectPoolConfig<?> getDefaultConfig() {
		return defaultConfig;
	}

	public static void setDefaultConfig(GenericObjectPoolConfig<?> defaultConfig) {
		RedisLoader.defaultConfig = defaultConfig;
	}

	public static void setResourceLoader(SingleResourceLoader resourceLoader) {
		RedisLoader.resourceLoader = resourceLoader;
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

	private static InputStream loadConfig() throws Exception {
		if (resourceLoader == null) {
			String resourceFactory = AppInfo.get("sumk.redis.conf.loader", "redis.RedisPropertiesLoader");
			if (resourceFactory == null || resourceFactory.isEmpty()) {
				return null;
			}
			Class<?> factoryClz = Loader.loadClass(resourceFactory);
			Assert.isTrue(SingleResourceLoader.class.isAssignableFrom(factoryClz),
					resourceFactory + " should extend from " + SingleResourceLoader.class.getSimpleName());
			resourceLoader = (SingleResourceLoader) factoryClz.newInstance();
		}
		return resourceLoader.openInput(REDIS_FILE);
	}

	private static void loadRedisByConfig() throws IOException, Exception {
		InputStream in = loadConfig();
		if (in == null) {
			return;
		}
		Map<String, String> p = CollectionUtil.loadMap(in);
		Log.get(Redis.LOG_NAME).debug("config:{}", p);
		Set<String> keys = p.keySet();
		for (String kk : keys) {
			if (StringUtil.isEmpty(kk)) {
				continue;
			}
			String v = p.get(kk);
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

	private static Redis create(String v) throws Exception {
		return RedisFactory.get(defaultConfig, createParam(v));
	}
}
