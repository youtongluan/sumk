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

import org.yx.log.Logs;
import org.yx.redis.Checkable;
import org.yx.redis.RedisConfig;
import org.yx.redis.RedisType;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.Pool;

public class SingleJedisExecutor extends AbstractJedisExecutor implements Checkable {

	public SingleJedisExecutor(RedisConfig config, Pool<Jedis> pool) {
		super(config, pool);
	}

	@Override
	public boolean aliveCheck() {
		Logs.redis().trace("{}-{} begin alive check", this, this.hashCode());
		final String PONE = "PONG";
		for (int i = 0; i < 3; i++) {
			try (Jedis jedis = jedis()) {
				String ret = jedis.ping();

				if (PONE.equalsIgnoreCase(ret)) {
					this.disConnected = false;
					return true;
				}
				Logs.redis().warn("{} answer {}", this, ret);
			} catch (Exception e) {
				if (!isConnectException(e)) {
					Logs.redis().error(e.getMessage(), e);

					if (!this.disConnected) {
						return false;
					}
				}
			}
		}
		this.disConnected = true;
		return false;
	}

	@Override
	public RedisType redisType() {
		return RedisType.SIMPLE;
	}
}
