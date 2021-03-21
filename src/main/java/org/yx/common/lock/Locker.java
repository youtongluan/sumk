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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;

import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.main.SumkServer;
import org.yx.redis.Redis;
import org.yx.redis.RedisPool;
import org.yx.util.IOUtil;
import org.yx.util.Task;

public final class Locker {
	private static final int REDIS_LEN = 16;
	private static final String[] nodeKey = new String[REDIS_LEN];

	public static synchronized void init() {
		if (nodeKey[0] != null) {
			return;
		}
		for (int i = 0; i < REDIS_LEN; i++) {
			nodeKey[i] = "lock_" + i;
		}
		ensureScriptRunner.run();
		Task.scheduleAtFixedRate(ensureScriptRunner, AppInfo.getLong("sumk.lock.schedule.delay", 1000L * 600),
				AppInfo.getLong("sumk.lock.schedule.delay", 1000L * 600));
	}

	private Locker() {
	}

	public static final Locker inst = new Locker();

	static Redis redis(String id) {
		int index = id.hashCode() & (REDIS_LEN - 1);
		Redis redis = RedisPool.get(nodeKey[index]);
		if (redis == null) {
			throw new SumkException(8295462, "SLock must use in redis environment");
		}
		return redis;
	}

	private static final ThreadLocal<List<SLock>> locks = new ThreadLocal<List<SLock>>() {
		@Override
		protected List<SLock> initialValue() {
			return new ArrayList<>(2);
		}
	};

	private static SLock getLock(final Lock key) {
		List<SLock> list = locks.get();
		if (list == null || list.isEmpty() || key == null) {
			return null;
		}
		list = new ArrayList<>(list);
		for (SLock lock : list) {
			if (!lock.isEnable()) {
				Log.get("sumk.lock").warn("remove unable lock: {}", lock);
				lock.unlock();
				continue;
			}
			if (lock.getId().equals(key.getId())) {
				return lock;
			}
		}
		return null;
	}

	public int releaseLocalLocks() {
		List<SLock> list = locks.get();
		list = new ArrayList<>(list);
		for (SLock lock : list) {
			lock.unlock();
		}
		locks.remove();
		return list.size();
	}

	void remove(final Lock lock) {
		if (lock == null || lock instanceof Locked) {
			return;
		}
		List<SLock> list = locks.get();
		if (list == null || list.isEmpty()) {
			return;
		}
		ListIterator<SLock> it = list.listIterator(list.size());
		while (it.hasPrevious()) {
			SLock lock2 = it.previous();
			if (lock2.getId().equals(lock.getId())) {
				it.remove();
			}
		}
	}

	/**
	 * 
	 * @param name
	 *            要被锁的对象
	 * @param maxWaitTime
	 *            获取锁的最大时间，单位ms
	 * @param maxLockTime
	 *            最大的锁住时间，单位ms
	 * @return Key对象,或者null
	 */
	public Lock tryLock(String name, int maxWaitTime, int maxLockTime) {
		SLock lock = SLock.create(name, maxLockTime);
		return tryLock(lock, maxWaitTime);
	}

	/**
	 * 
	 * @param name
	 *            要被锁的对象
	 * @param maxWaitTime
	 *            获取锁的最大时间，单位ms。如果只想尝试一次，可以传0
	 * @return 锁的钥匙，获取不到锁就返回null
	 */
	public Lock tryLock(String name, int maxWaitTime) {
		SLock lock = SLock.create(name);
		return tryLock(lock, maxWaitTime);
	}

	public Lock lock(String name) {
		return tryLock(name, Integer.MAX_VALUE);
	}

	/**
	 * 尝试加锁，如果锁定失败，就返回null
	 * 
	 * @param lock
	 *            锁对象
	 * @param maxWaitTime
	 *            获取锁的最大时间，单位ms。无论值为多少，都至少会尝试一次
	 * @return Key对象,或者null
	 */
	public Lock tryLock(SLock lock, int maxWaitTime) {
		Lock old = getLock(lock);
		if (old != null) {
			return new Locked(old.getId(), old.getValue());
		}
		if (lock.lock(maxWaitTime)) {
			locks.get().add(lock);
			return lock;
		}
		return null;
	}

	public boolean isLockedNow(Lock lock) {
		return Objects.equals(lock.getValue(), Locker.redis(lock.getId()).get(lock.getId()));
	}

	/**
	 * 重置锁超时时间，对重进入的锁也适用
	 * 
	 * @param lock
	 *            锁
	 * @param mils
	 *            重置锁的过期时间为当前值
	 * @return 如果锁已经被释放，或其它未知原因，就返回false
	 */
	public boolean resetExpiredTime(Lock lock, int mils) {
		SLock slock = getLock(lock);
		if (slock == null || !isLockedNow(slock)) {
			return false;
		}

		long endTime = System.currentTimeMillis() + mils;
		if (Locker.redis(slock.getId()).pexpireAt(slock.getId(), endTime) > 0) {
			slock.resetEndTime(endTime);
			return true;
		}
		return false;
	}

	private static final Runnable ensureScriptRunner = () -> {
		try {
			InputStream in = SumkServer.class.getClassLoader().getResourceAsStream("META-INF/lua_del");
			String script = new String(IOUtil.readAllBytes(in, true), AppInfo.UTF8);
			Set<Redis> set = new HashSet<>();
			for (String key : nodeKey) {
				Redis redis = RedisPool.get(key);
				if (redis == null || !set.add(redis)) {
					continue;
				}
				Log.get("sumk.lock").debug("init lock script", redis);

				redis.scriptLoad(script);
			}
		} catch (Exception e) {
			Log.get("sumk.lock").error("Lock init failed. Maybe you need restart!!!");
			Log.printStack("sumk.lock", e);
			if (AppInfo.getBoolean("sumk.shutdown.if.lock.failed", false)) {
				System.exit(1);
			}
		}
	};
}
