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

import org.yx.annotation.Bean;
import org.yx.log.Logs;
import org.yx.redis.Redis;
import org.yx.redis.RedisConfig;
import org.yx.redis.RedisFactory;
import org.yx.redis.RedisType;

@Bean
public class Redis3Factory implements RedisFactory {

	@Override
	public Redis create(RedisConfig conf) {
		String type = conf.getType();
		if (RedisType.SENTINEL.accept(type)) {
			Logs.redis().warn("sentinel has not be tested.if any problem,send email to 3205207767@qq.com");
			return new RedisImpl(new SentinelJedisExecutor(conf, Redis3Kit.createSentinelPool(conf)));
		}
		if (RedisType.CLUSTER.accept(type) || conf.hosts().contains(",")) {
			return Redis3Kit.createJedisCluster(conf);
		}
		return new RedisImpl(new SingleJedisExecutor(conf, Redis3Kit.createJedisPool(conf)));
	}

	@Override
	public int order() {
		return RedisFactory.super.order() + 100;
	}

}
