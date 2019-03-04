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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yx.common.lock.Key;
import org.yx.common.lock.Lock;
import org.yx.common.lock.SLock;
import org.yx.util.secury.EncryUtil;
import org.yx.util.secury.MD5Utils;

public abstract class S {

	public static class Collection {
		/**
		 * 生成一个java.util.ArrayList对象
		 * 
		 * @param a
		 *            列表中的元素
		 * @return java.util.ArrayList对象
		 */
		@SafeVarargs
		public static <T> List<T> list(T... a) {
			List<T> list = new ArrayList<>();
			CollectionUtil.addAll(list, a);
			return list;
		}

		/**
		 * 生成一个java.util.HashSet对象
		 * 
		 * @param a
		 *            set中的元素
		 * @return java.util.HashSet对象
		 */
		@SafeVarargs
		public static <T> Set<T> set(T... a) {
			Set<T> set = new HashSet<>();
			CollectionUtil.addAll(set, a);
			return set;
		}
	}

	/**
	 * AES加解密
	 */
	public static class Aes {

		public static byte[] encrypt(byte[] contentBytes, byte[] key) throws Exception {
			return EncryUtil.encrypt(contentBytes, key);
		}

		public static byte[] decrypt(byte[] contentBytes, byte[] key) throws Exception {
			return EncryUtil.decrypt(contentBytes, key);

		}

	}

	public static class MD5 {

		public static String encrypt(byte[] data) throws Exception {
			return MD5Utils.encrypt(data);
		}

		public static byte[] encryptByte(byte[] data) throws Exception {
			return MD5Utils.encryptByte(data);
		}

	}

	/**
	 * 分布式锁
	 */
	public static final class UnionLock {
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
