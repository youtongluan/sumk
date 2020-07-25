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

import java.util.Objects;
import java.util.function.Function;

import org.yx.log.Logs;

public class RedisFactory {

	private static Function<RedisConfig, Redis> creator;

	static {
		try {
			creator = conf -> {
				String type = conf.getType();
				if ("2".equals(conf.getType()) || "sentinel".equalsIgnoreCase(type)) {
					Logs.redis().warn("sentinel has not be tested.if any problem,send email to 3205207767@qq.com");
					return new RedisImpl(new SentinelJedis2Executor(conf));
				}
				if ("1".equals(type) || "cluster".equalsIgnoreCase(type)
						|| conf.hosts().replace('，', ',').contains(",")) {
					return Jedis2Factorys.getJedisClusterFactory().apply(conf);
				}
				return new RedisImpl(new SingleJedis2Executor(conf));
			};
		} catch (Throwable e) {
			Logs.redis().error("初始化redis的factory失败", e);
		}

	}

	public static Function<RedisConfig, Redis> getFactroy() {
		return creator;
	}

	public static void setFactroy(Function<RedisConfig, Redis> creator) {
		RedisFactory.creator = Objects.requireNonNull(creator);
	}

	public static Redis create(RedisConfig conf) {
		return creator.apply(conf);
	}

}
