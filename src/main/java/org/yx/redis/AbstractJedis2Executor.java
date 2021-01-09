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

import org.yx.exception.SimpleSumkException;
import org.yx.exception.SumkException;
import org.yx.exception.SumkExceptionCode;
import org.yx.log.Logs;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.Pool;

public abstract class AbstractJedis2Executor implements Jedis2Executor {

	private static final SumkException DIS_CONNECTION_EXCEPTION = new SimpleSumkException(
			SumkExceptionCode.REDIS_DIS_CONNECTION, "redis is disConnected");

	protected boolean disConnected;
	protected final RedisConfig config;
	protected final Pool<Jedis> pool;

	public AbstractJedis2Executor(RedisConfig config, Pool<Jedis> pool) {
		this.config = Objects.requireNonNull(config);
		this.pool = Objects.requireNonNull(pool);
	}

	public Jedis jedis() {
		return pool.getResource();
	}

	@Override
	public RedisConfig getRedisConfig() {
		return this.config;
	}

	@Override
	public String hosts() {
		return config.hosts();
	}

	protected boolean isConnectException(Throwable e) {
		return e instanceof JedisConnectionException
				|| (e.getCause() != null && e.getCause() instanceof JedisConnectionException);
	}

	@Override
	public <T> T execAndRetry(Function<Jedis, T> callback, boolean mute) {
		return this.exec(callback, this.config.getMaxAttempts(), mute);
	}

	public <T> T exec(Function<Jedis, T> callback, int maxAttempts, boolean mute) {
		if (maxAttempts < 1) {
			maxAttempts = 1;
		}
		Jedis jedis = null;
		Throwable e1 = null;
		for (int i = 0; i < maxAttempts; i++) {
			if (this.disConnected) {
				if (mute) {
					return null;
				}
				throw DIS_CONNECTION_EXCEPTION;
			}
			try {
				jedis = this.jedis();
				return callback.apply(jedis);
			} catch (Throwable e) {
				e1 = e;
				if (isConnectException(e)) {
					Logs.redis().warn("redis连接异常！({}){}", hosts(), e.getMessage());
					continue;
				}
				Logs.redis().error("redis执行错误！({}){}", hosts(), e.getMessage());
				break;
			} finally {
				if (jedis != null) {
					try {
						jedis.close();
					} catch (Throwable e2) {
						Logs.redis().error(e2.toString(), e2);
					}
					jedis = null;
				}
			}
		}

		if (mute) {
			return null;
		}
		if (e1 != null) {
			throw new SumkException(12342422, e1.getMessage(), e1);
		}
		throw new SumkException(12342423, "未知redis异常,执行次数:" + maxAttempts);
	}

	public void shutdownPool() {
		this.pool.close();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(32);
		sb.append("[hosts=").append(hosts()).append(",db=").append(config.getDb()).append(",maxAttempts=")
				.append(config.getMaxAttempts());
		try {
			sb.append(",idle=").append(pool.getNumIdle()).append(",active=").append(pool.getNumActive());
		} catch (Throwable e) {

		}
		return sb.append("]").toString();
	}
}
