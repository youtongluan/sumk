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

final class RedisChecker implements Runnable {

	private static final RedisChecker holder = new RedisChecker();

	private RedisChecker() {

	}

	public static RedisChecker get() {
		return holder;
	}

	private Redis[] allRedis = new Redis[0];

	public synchronized void addRedis(Redis redis) {
		Redis[] rss = new Redis[allRedis.length + 1];
		System.arraycopy(allRedis, 0, rss, 0, allRedis.length);
		rss[rss.length - 1] = redis;
		this.allRedis = rss;
	}

	@Override
	public void run() {
		Redis[] rediss = this.allRedis;
		for (Redis redis : rediss) {
			redis.aliveCheck();
		}

	}

}
