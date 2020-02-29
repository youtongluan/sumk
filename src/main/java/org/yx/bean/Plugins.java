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
package org.yx.bean;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class Plugins {

	private static CountDownLatch redisWatcher = new CountDownLatch(1);
	private static CountDownLatch dbWatcher = new CountDownLatch(1);

	private static boolean waitForPlugin(CountDownLatch c, long mils) {
		try {
			if (c == null) {
				return true;
			}
			return c.await(mils, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			return false;
		}
	}

	private static void finishPlugin(CountDownLatch c) {
		if (c == null) {
			return;
		}
		c.countDown();
	}

	public static boolean waitForRedisPlugin(long mils) {
		return waitForPlugin(redisWatcher, mils);
	}

	public static boolean waitForDBOnly(long mils) {
		return waitForPlugin(dbWatcher, mils);
	}

	public static boolean waitForDBAndCache(long mils) {
		waitForRedisPlugin(mils);
		return waitForDBOnly(mils);
	}

	public static void setRedisStarted() {
		finishPlugin(redisWatcher);
		Plugins.redisWatcher = null;
	}

	public static void setDbStarted() {
		finishPlugin(dbWatcher);
		Plugins.dbWatcher = null;
	}

	public static void setAllStarted() {
		setRedisStarted();
		setDbStarted();
	}
}
