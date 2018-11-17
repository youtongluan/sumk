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
package org.yx.util;

import org.yx.common.lock.Key;
import org.yx.common.lock.Lock;
import org.yx.common.lock.SLock;

public abstract class S {

	/**
	 * 分布式锁
	 */
	public static class UnionLock {
		/**
		 * 解除所有分布式锁
		 */
		public static void unlock() {
			SLock.unlock();
		}

		/**
		 * 解除key对应的分布式锁
		 * 
		 * @param key
		 *            为null的话，将不发生任何事情
		 */
		public static void unlock(Key key) {
			SLock.unlock(key);
		}

		/**
		 * 
		 * @param name
		 *            要被锁的对象
		 * @param maxWaitTime
		 *            获取锁的最大时间，单位ms
		 * @return 锁的钥匙
		 */
		public static Key tryLock(String name, long maxWaitTime) {
			return SLock.tryLock(name, maxWaitTime);
		}

		public static Key lock(String name) {
			return SLock.lock(name);
		}

		/**
		 * 尝试加锁，如果锁定失败，就返回null
		 * 
		 * @param lock
		 *            锁对象
		 * @param maxWaitTime
		 *            获取锁的最大时间，单位ms
		 * @return 锁
		 */
		public static Key tryLock(Lock lock, long maxWaitTime) {
			return SLock.tryLock(lock, maxWaitTime);
		}

	}
}
