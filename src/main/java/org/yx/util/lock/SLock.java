package org.yx.util.lock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.main.SumkServer;
import org.yx.redis.Redis;
import org.yx.redis.RedisPool;
import org.yx.util.StringUtils;

/**
 * 分布式锁<BR>
 * <B>本功能依赖于redis，必须在拥有redis 2.6或以上版本的环境下使用，而且要在拥有良好网络的内网使用</B>
 * 
 */
public final class SLock {
	private static final int REDIS_LEN = 16;
	private static final String[] nodeKey = new String[REDIS_LEN];

	static {
		try {
			for (int i = 0; i < REDIS_LEN; i++) {
				nodeKey[i] = "lock_" + i;
			}
			String script = StringUtils.load(SumkServer.class.getClassLoader().getResourceAsStream("META-INF/lua_del"));
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
			Log.printStack(e);
		}
	}

	static Redis redis(String id) {
		int index = id.hashCode() & (REDIS_LEN - 1);
		Redis redis = RedisPool.get(nodeKey[index]);
		if (redis == null) {
			SumkException.throwException(8295462, "SLock must use in redis environment");
		}
		return redis;
	}

	private static final ThreadLocal<List<Lock>> locks = new ThreadLocal<List<Lock>>() {
		@Override
		protected List<Lock> initialValue() {
			return new ArrayList<>(2);
		}
	};

	private static Lock getLock(Key key) {
		List<Lock> list = locks.get();
		if (list == null || list.isEmpty() || key == null) {
			return null;
		}
		Iterator<Lock> it = list.iterator();
		while (it.hasNext()) {
			Lock lock = it.next();
			if (lock.getId().equals(key.getId())) {
				return lock;
			}
		}
		return null;
	}

	public static void unlock() {
		List<Lock> list = locks.get();
		for (Lock lock : list) {
			lock.unlock();
		}
		locks.remove();
	}

	/**
	 * 
	 * @param key
	 *            为null的话，将不发生任何事情
	 */
	public static void unlock(Key key) {
		List<Lock> list = locks.get();
		if (list == null || list.isEmpty() || key == null || key == LockedKey.key) {
			return;
		}
		Iterator<Lock> it = list.iterator();
		while (it.hasNext()) {
			Lock lock = it.next();
			if (lock.getId().equals(key.getId())) {
				lock.unlock();
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
	 * @return 锁的钥匙
	 */
	public static Key lock(String name, long maxWaitTime) {
		Lock lock = Lock.create(name);
		return lock(lock, maxWaitTime);
	}

	public static Key lock(String name) {
		return lock(name, Integer.MAX_VALUE);
	}

	public static Key lock(Lock lock, long maxWaitTime) {
		if (getLock(lock) != null) {
			return LockedKey.key;
		}
		lock = lock.lock(maxWaitTime);
		if (lock != null) {
			locks.get().add(lock);
		}
		return lock;
	}

}
