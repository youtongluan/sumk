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

import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.util.UUIDSeed;
import org.yx.util.kit.Asserts;

public final class SLock implements Lock {
	private static final String SHA = "fc8341f94e518c9868148c2b8fc7cef25ec6fa85";
	private static final long CLOSED = -1;
	private final String id;
	private final String value;
	private final int maxLockTime;
	private final int intervalTime;

	private long endTime;
	private long startNS;

	public SLock(String keyId, String value, int maxLockTime, int intervalTime) {
		Asserts.requireTrue(keyId != null && (keyId = keyId.trim()).length() > 0, "lock name cannot be empty");
		Asserts.requireTrue(intervalTime > 0 && maxLockTime > 0 && value != null && value.length() > 0,
				"lock param is not valid");
		this.id = keyId;
		this.value = value;
		this.maxLockTime = maxLockTime;
		this.intervalTime = intervalTime;
	}

	public String getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public static SLock create(String name, int maxLockTime, int intervalTime) {
		return new SLock(name, UUIDSeed.seq(), maxLockTime, intervalTime);
	}

	public static SLock create(String name) {
		return create(name, AppInfo.getInt("sumk.lock.maxLockTime", 60000));
	}

	public static SLock create(String name, int maxLockTime) {
		return create(name, maxLockTime, AppInfo.getInt("sumk.lock.intervalTime", 50));
	}

	boolean tryLock() {
		this.startNS = System.nanoTime();
		String ret = Locker.redis(id).set(id, value, "NX", "PX", maxLockTime);
		if (ret == null) {
			return false;
		}
		return ret.equalsIgnoreCase("OK") || ret.equals("1");
	}

	boolean lock(final int maxWaitTime) {
		long now = System.currentTimeMillis();
		this.endTime = now + this.maxLockTime;
		if (this.endTime < 1) {
			this.endTime = 1;
		}
		long waitEndTime = now + maxWaitTime;
		while (true) {
			if (tryLock()) {
				return true;
			}
			long left = waitEndTime - System.currentTimeMillis();
			if (left <= 0) {
				return false;
			}

			long sleepTime = Math.min(left, (long) this.intervalTime);
			try {
				Log.get("sumk.lock").debug("locked failed: {}={},sleep {}ms", id, value, sleepTime);
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return false;
			}
		}
	}

	void resetEndTime(long endTime) {
		this.endTime = endTime;
	}

	boolean isEnable() {
		long end = this.endTime;
		return end > 0 && System.currentTimeMillis() < end;
	}

	@Override
	public void unlock() {
		if (this.endTime == CLOSED) {
			return;
		}
		Locker.redis(id).evalsha(SHA, 1, id, value);
		this.endTime = CLOSED;
		Locker.inst.remove(this);
		Log.get("sumk.lock").info("unlock {}:{},spent {}ns", id, value, System.nanoTime() - this.startNS);
	}

	@Override
	public String toString() {
		return id + "=" + value + " : " + this.endTime;
	}

}
