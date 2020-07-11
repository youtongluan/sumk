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

import java.util.function.Function;

import org.yx.exception.SumkException;
import org.yx.log.Logs;

import redis.clients.jedis.Jedis;

public abstract class Redis2 implements Redis, Cloneable {

	protected final Jedis2Executor jedis2Executor;
	private boolean mute;
	private transient Redis2 muteConnectionExceptionRedis;

	public Redis2(Jedis2Executor executor) {
		this.jedis2Executor = executor;
		if (Checkable.class.isInstance(this.jedis2Executor)) {
			RedisChecker.get().addRedis(Checkable.class.cast(this.jedis2Executor));
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

	public <T> T execAndRetry(Function<Jedis, T> callback) {
		return this.jedis2Executor.execAndRetry(callback, mute);
	}

	public void shutdownPool() {
		this.jedis2Executor.shutdownPool();
		if (Checkable.class.isInstance(this.jedis2Executor)) {
			RedisChecker.get().remove(Checkable.class.cast(this.jedis2Executor));
		}
	}

	public boolean isCluster() {
		return false;
	}

	@Override
	public Redis mute() {
		Redis2 r = this.muteConnectionExceptionRedis;
		if (r != null) {
			return r;
		}
		if (this.mute) {
			return this;
		}
		try {
			r = (Redis2) super.clone();
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
	public String toString() {
		return "Redis[" + this.jedis2Executor + "]";
	}
}
