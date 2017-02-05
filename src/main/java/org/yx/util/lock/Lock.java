/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
package org.yx.util.lock;

import java.util.concurrent.locks.LockSupport;

import org.slf4j.Logger;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.util.Assert;
import org.yx.util.UUIDSeed;

public final class Lock implements Key {
	static Logger logger = Log.get("sumk.lock");
	final static String sha = "fc8341f94e518c9868148c2b8fc7cef25ec6fa85";
	private String id;
	String value;
	int maxLockTime;
	int intervalTime;

	public Lock(String keyId, String value, int maxLockTime, int intervalTime) {
		Assert.isTrue(keyId != null && (keyId = keyId.trim()).length() > 0, "lock name cannot be empty");
		Assert.isTrue(intervalTime > 1000 && maxLockTime > 0 && value != null && value.length() > 0,
				"lock param is not valid");
		this.id = keyId;
		this.value = value;
		this.maxLockTime = maxLockTime;
		this.intervalTime = intervalTime;
	}

	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param name
	 *            锁的名称
	 * @param maxLockTime
	 *            最大的锁住时间，单位秒
	 * @param intervalTime
	 *            两次重试之间的间隔时间，单位纳秒
	 * @return
	 */
	public static Lock create(String name, int maxLockTime, int intervalTime) {
		return new Lock(name, UUIDSeed.seq(), maxLockTime, intervalTime);
	}

	public static Lock create(String name) {
		return create(name, AppInfo.getInt("sumk.lock.maxLockTime", 300));
	}

	/**
	 * 
	 * @param name
	 *            锁的名称
	 * @param maxLockTime
	 *            最大的锁住时间，单位秒
	 * @return
	 */
	public static Lock create(String name, int maxLockTime) {
		return create(name, maxLockTime, AppInfo.getInt("sumk.lock.intervalTime", 1000 * 1000 * 10));
	}

	boolean tryLock() {
		String ret = SLock.redis(id).set(id, value, "NX", "EX", maxLockTime);
		if (ret == null) {
			return false;
		}
		ret = ret.toUpperCase();
		return ret.equals("OK") || ret.equals("1");
	}

	Lock lock(long maxWaitTime) {
		long begin = System.currentTimeMillis();
		while (System.currentTimeMillis() - begin < maxWaitTime) {
			if (tryLock()) {
				logger.debug("locked success: {}={}", id, value);
				return this;
			}
			LockSupport.parkNanos(this.intervalTime);
		}
		return null;
	}

	void unlock() {
		SLock.redis(id).evalsha(sha, 1, id, value);
		logger.debug("unlock: {}={}", id, value);
	}
}
