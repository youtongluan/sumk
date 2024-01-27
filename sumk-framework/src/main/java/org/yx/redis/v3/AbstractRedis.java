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

import java.util.function.Function;

import org.yx.exception.SumkException;
import org.yx.log.Logs;
import org.yx.redis.Checkable;
import org.yx.redis.Redis;
import org.yx.redis.RedisChecker;
import org.yx.redis.RedisConfig;
import org.yx.redis.RedisType;

import redis.clients.jedis.Jedis;

public abstract class AbstractRedis implements Redis, Redis3x, Cloneable {

	protected final JedisExecutor jedis2Executor;
	private boolean mute;
	private transient AbstractRedis muteConnectionExceptionRedis;

	public AbstractRedis(JedisExecutor executor) {
		this.jedis2Executor = executor;
		if (this.jedis2Executor instanceof Checkable) {
			RedisChecker.get().addRedis((Checkable) this.jedis2Executor);
		}
	}

	@Override
	public RedisConfig getRedisConfig() {
		return this.jedis2Executor.getRedisConfig();
	}

	@Override
	public String hosts() {
		return jedis2Executor.hosts();
	}

	@Override
	public <T> T execute(String key, Function<Jedis, T> callback) {
		return this.execAndRetry(callback);
	}

	public <T> T execAndRetry(Function<Jedis, T> callback) {
		return this.jedis2Executor.execAndRetry(callback, mute);
	}

	public void shutdownPool() {
		this.jedis2Executor.shutdownPool();
		if (this.jedis2Executor instanceof Checkable) {
			RedisChecker.get().remove((Checkable) this.jedis2Executor);
		}
	}

	@Override
	public Redis mute() {
		AbstractRedis r = this.muteConnectionExceptionRedis;
		if (r != null) {
			return r;
		}
		if (this.mute) {
			return this;
		}
		try {
			r = (AbstractRedis) super.clone();
		} catch (Exception e) {
			Logs.redis().error(e.toString(), e);
			throw new SumkException(345345, this.getClass().getName() + "无法clone");
		}
		r.mute = true;
		this.muteConnectionExceptionRedis = r;
		return r;
	}

	@Override
	public boolean isMuted() {
		return mute;
	}

	@Override
	public RedisType redisType() {
		return this.jedis2Executor.redisType();
	}

	@Override
	public String toString() {
		return "Redis[" + this.jedis2Executor + "]";
	}
}
