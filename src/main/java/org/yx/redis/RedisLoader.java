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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.yx.conf.AppInfo;
import org.yx.conf.SimpleBeanUtil;
import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

public class RedisLoader {

	public static final String SEQ = "seq";
	public static final String SESSION = "session";

	private static final String CONFIG_PREFIX = "s.redis.";

	public static synchronized void init() throws Exception {
		Map<String, String> map = AppInfo.subMap(CONFIG_PREFIX);
		if (map == null || map.isEmpty()) {
			return;
		}
		Map<String, String> commonConfig = Collections.unmodifiableMap(AppInfo.subMap("s.common.redis."));
		for (String key : map.keySet()) {
			if (key.contains(".")) {
				continue;
			}
			Map<String, String> configMap = new HashMap<>(commonConfig);
			configMap.putAll(CollectionUtil.subMap(map, key + "."));
			initRedis(key, StringUtil.toLatin(map.get(key).trim()), configMap);
		}
		if (Logs.redis().isDebugEnabled()) {
			Logs.redis().debug("redis的主键列表{},默认redis: {}", RedisPool.keys(), RedisPool.defaultRedis());
		}
	}

	private static void initRedis(final String name, String host, Map<String, String> configMap) {
		Logs.redis().info("开始初始化redis：{}={}", name, host);
		try {
			RedisConfig config = createConfig(host, configMap);
			Logs.redis().debug("{} : {}", name, config);

			Redis redis = RedisFactory.create(config);
			if ("*".equals(name) || "default".equals(name)) {
				RedisPool.setDefaultRedis(redis);
			} else {
				RedisPool.put(name, redis);
			}
			String aliases = config.getAlias();
			if (StringUtil.isNotEmpty(aliases)) {
				String[] aas = StringUtil.toLatin(aliases).split(",");
				for (String s : aas) {
					s = s.trim();
					if (s.isEmpty()) {
						continue;
					}
					if (RedisPool.getRedisExactly(s) != null) {
						Logs.redis().warn("{}的redis配置已经存在了", s);
						continue;
					}
					Logs.redis().debug("设置别名{} -> {}", s, name);
					RedisPool.put(s, redis);
				}
			}
		} catch (Exception e) {
			throw new SumkException(35345, "redis [" + name + "] 初始化失败", e);
		}

	}

	public static RedisConfig createConfig(String host, Map<String, String> configMap) {
		RedisConfig config = new RedisConfig(host);
		if (configMap != null && configMap.size() > 0) {
			try {
				SimpleBeanUtil.copyProperties(config, configMap);
			} catch (Exception e) {
				Logs.redis().error(e.getLocalizedMessage(), e);
			}
		}
		return config;
	}
}
