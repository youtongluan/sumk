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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.main.SumkServer;
import org.yx.redis.Redis;
import org.yx.redis.RedisPool;
import org.yx.util.StringUtil;

public final class Locker {
	private static final int REDIS_LEN = 16;
	private static final String[] nodeKey = new String[REDIS_LEN];

	public static void init() {
		try {
			for (int i = 0; i < REDIS_LEN; i++) {
				nodeKey[i] = "lock_" + i;
			}
			String script = StringUtil.load(SumkServer.class.getClassLoader().getResourceAsStream("META-INF/lua_del"));
			Set<Redis> set = new HashSet<>();
			for (String key : nodeKey) {
				Redis redis = RedisPool.get(key);
				if (redis == null || !set.add(redis)) {
					continue;
				}
				redis.scriptLoad(script);
			}
		} catch (Exception e) {
			Log.get("sumk.lock").error("Lock init failed. Maybe you need restart!!!");
			Log.printStack("sumk.lock", e);
			if (AppInfo.getBoolean("sumk.shutdown.if.lock", false)) {
				System.exit(-1);
			}
		}
	}

	private Locker() {

	}

	public static final Locker inst = new Locker();

	static Redis redis(String id) {
		int index = id.hashCode() & (REDIS_LEN - 1);
		Redis redis = RedisPool.get(nodeKey[index]);
		if (redis == null) {
			SumkException.throwException(8295462, "SLock must use in redis environment");
		}
		return redis;
	}

	private static final ThreadLocal<List<SLock>> locks = new ThreadLocal<List<SLock>>() {
		@Override
		protected List<SLock> initialValue() {
			return new ArrayList<>(2);
		}
	};

	private static SLock getLock(Lock key) {
		List<SLock> list = locks.get();
		if (list == null || list.isEmpty() || key == null) {
			return null;
		}
		Iterator<SLock> it = list.iterator();
		while (it.hasNext()) {
			SLock lock = it.next();
			if (lock.getId().equals(key.getId())) {
				return lock;
			}
		}
		return null;
	}

	public void releaseLocalLocks() {
		List<SLock> list = locks.get();
		for (SLock lock : list) {
			lock.unlock();
		}
		locks.remove();
	}

	/**
	 * 
	 * @param key
	 *            为null的话，将不发生任何事情
	 */
	public void unlock(Lock key) {
		if (key == null || key == Locked.key) {
			return;
		}
		List<SLock> list = locks.get();
		if (list == null || list.isEmpty()) {
			return;
		}
		ListIterator<SLock> it = list.listIterator(list.size());
		while (it.hasPrevious()) {
			SLock lock = it.previous();
			if (lock.getId().equals(key.getId())) {
				lock.unlock0();
				it.remove();
				return;
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
	 *            最大的锁住时间，单位秒
	 * @return Key对象,或者null
	 */
	public Lock tryLock(String name, long maxWaitTime, int maxLockTime) {
		SLock lock = SLock.create(name, maxLockTime);
		return tryLock(lock, maxWaitTime);
	}

	/**
	 * 
	 * @param name
	 *            要被锁的对象
	 * @param maxWaitTime
	 *            获取锁的最大时间，单位ms
	 * @return 锁的钥匙
	 */
	public Lock tryLock(String name, long maxWaitTime) {
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
	public Lock tryLock(SLock lock, long maxWaitTime) {
		if (getLock(lock) != null) {
			return Locked.key;
		}
		if (lock.lock(maxWaitTime)) {
			locks.get().add(lock);
			return lock;
		}
		return null;
	}

}
