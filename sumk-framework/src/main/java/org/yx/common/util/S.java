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
package org.yx.common.util;

import org.yx.base.thread.SumkExecutorService;
import org.yx.common.json.GsonHelper;
import org.yx.common.lock.Locker;
import org.yx.common.util.kit.BeanConverter;
import org.yx.common.util.secury.AESEncryptor;
import org.yx.common.util.secury.Base64;
import org.yx.common.util.secury.Base64Impl;
import org.yx.common.util.secury.CommonDigest;
import org.yx.common.util.secury.Encryptor;
import org.yx.common.util.secury.Hasher;
import org.yx.util.SumkThreadPool;

import com.google.gson.Gson;

public final class S {

	private static Gson json = GsonHelper.gson("sumk");
	private static SumkExecutorService executor = SumkThreadPool.executor();
	private static Base64 base64 = Base64Impl.inst;
	private static Encryptor cipher = new AESEncryptor();
	private static Hasher hash = new CommonDigest("SHA-256");
	private static Locker lock = Locker.inst;
	private static BeanConverter bean = new BeanConverter();

	/**
	 * @return json工具
	 */
	public static Gson json() {
		return json;
	}

	/**
	 * @return 系统共用的线程池
	 */
	public static SumkExecutorService executor() {
		return executor;
	}

	public static Base64 base64() {
		return base64;
	}

	/**
	 * @return 加密器，默认是AES对称加密
	 */
	public static Encryptor cipher() {
		return cipher;
	}

	/**
	 * @return hash工具，就是大家常说的md5
	 */
	public static Hasher hash() {
		return hash;
	}

	/**
	 * @return 分布式锁
	 */
	public static Locker lock() {
		return lock;
	}

	/**
	 * @return bean和map的转换，以及属性复制。只支持第一级field
	 */
	public static BeanConverter bean() {
		return bean;
	}

	static void setJson(Gson json) {
		S.json = json;
	}

	static void setExecutor(SumkExecutorService executor) {
		S.executor = executor;
	}

	static void setBase64(Base64 base64) {
		S.base64 = base64;
	}

	static void setCipher(Encryptor cipher) {
		S.cipher = cipher;
	}

	static void setHash(Hasher hash) {
		S.hash = hash;
	}

	static void setLock(Locker lock) {
		S.lock = lock;
	}

	static void setBean(BeanConverter bean) {
		S.bean = bean;
	}

}
