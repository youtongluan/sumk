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

import org.yx.log.Logs;
import org.yx.util.helper.ArrayHelper;

public final class RedisChecker implements Runnable {

	private static final RedisChecker holder = new RedisChecker();

	private RedisChecker() {

	}

	public static RedisChecker get() {
		return holder;
	}

	private Checkable[] allRedis = new Checkable[0];

	public synchronized void addRedis(Checkable c) {
		Logs.redis().debug("add alive check: {}", c);
		this.allRedis = ArrayHelper.add(this.allRedis, c, i -> new Checkable[i]);
	}

	@Override
	public void run() {
		Checkable[] rediss = this.allRedis;
		for (Checkable redis : rediss) {
			redis.aliveCheck();
		}
	}

	public void remove(Checkable c) {
		Logs.redis().debug("remove alive chech: {}", c);
		this.allRedis = ArrayHelper.remove(this.allRedis, c, i -> new Checkable[i]);
	}

}
