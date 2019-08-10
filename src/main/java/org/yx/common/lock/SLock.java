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
package org.yx.common.lock;

import org.slf4j.Logger;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.util.Assert;
import org.yx.util.UUIDSeed;

public final class SLock implements Lock {
	static final Logger logger = Log.get("sumk.lock");
	final static String sha = "fc8341f94e518c9868148c2b8fc7cef25ec6fa85";
	private final String id;
	private final String value;
	private final int maxLockTime;

	private final int intervalTime;

	public SLock(String keyId, String value, int maxLockTime, int intervalTime) {
		Assert.isTrue(keyId != null && (keyId = keyId.trim()).length() > 0, "lock name cannot be empty");
		Assert.isTrue(intervalTime > 0 && maxLockTime > 0 && value != null && value.length() > 0,
				"lock param is not valid");
		this.id = keyId;
		this.value = value;
		this.maxLockTime = maxLockTime;
		this.intervalTime = intervalTime;
	}

	public String getId() {
		return id;
	}

	public static SLock create(String name, int maxLockTime, int intervalTime) {
		return new SLock(name, UUIDSeed.seq(), maxLockTime, intervalTime);
	}

	public static SLock create(String name) {
		return create(name, AppInfo.getInt("sumk.lock.maxLockTime", 300));
	}

	public static SLock create(String name, int maxLockTime) {
		return create(name, maxLockTime, AppInfo.getInt("sumk.lock.intervalTime", 5));
	}

	boolean tryLock() {
		String ret = Locker.redis(id).set(id, value, "NX", "EX", maxLockTime);
		if (ret == null) {
			return false;
		}
		return ret.equalsIgnoreCase("OK") || ret.equals("1");
	}

	boolean lock(long maxWaitTime) {
		long begin = System.currentTimeMillis();
		do {
			if (tryLock()) {
				return true;
			}
			logger.debug("locked failed: {}={}", id, value);
			try {
				Thread.sleep(this.intervalTime);
			} catch (InterruptedException e) {
				return false;
			}
		} while (System.currentTimeMillis() - begin < maxWaitTime);
		return false;
	}

	void unlock0() {
		Locker.redis(id).evalsha(sha, 1, id, value);
		logger.debug("unlock: {}={}", id, value);
	}

	@Override
	public void unlock() {
		Locker.inst.unlock(this);
	}
}
