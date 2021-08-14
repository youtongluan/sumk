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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.yx.common.Host;
import org.yx.log.Logs;
import org.yx.util.StringUtil;

public final class RedisSettings {
	private static byte[] PASSWORD_KEY = new byte[] { 121, 111, 117, 116, 111, 110, 103, 108, 117, 97, 110, 64, 115,
			117, 109, 107 };
	private static Function<RedisConfig, Redis> creator;
	static {
		try {
			creator = conf -> {
				String type = conf.getType();
				if (RedisType.SENTINEL.accept(type)) {
					Logs.redis().warn("sentinel has not be tested.if any problem,send email to 3205207767@qq.com");
					return new Redis2Impl(new SentinelJedis2Executor(conf));
				}
				if (RedisType.CLUSTER.accept(type) || conf.hosts().contains(",")) {
					return Jedis2Factorys.getJedisClusterFactory().apply(conf);
				}
				return new Redis2Impl(new SingleJedis2Executor(conf));
			};
		} catch (Throwable e) {
			Logs.redis().error("初始化redis的factory失败", e);
		}

	}

	public static byte[] getPasswordKey() {
		return PASSWORD_KEY;
	}

	public static void setPasswordKey(byte[] passwordKey) {
		PASSWORD_KEY = Objects.requireNonNull(passwordKey);
	}

	public static Function<RedisConfig, Redis> getFactroy() {
		return creator;
	}

	public static void setFactroy(Function<RedisConfig, Redis> creator) {
		RedisSettings.creator = Objects.requireNonNull(creator);
	}

	public static List<Host> parseHosts(String host) {
		String h = StringUtil.toLatin(host).replaceAll("\\s", "");
		String[] hs = h.split(",");
		List<Host> hosts = new ArrayList<>(hs.length);
		for (String addr : hs) {
			if (addr.isEmpty()) {
				continue;
			}
			if (!addr.contains(":")) {
				hosts.add(Host.create(addr, 6379));
				continue;
			}
			hosts.add(Host.create(addr));
		}
		return hosts;
	}
}
