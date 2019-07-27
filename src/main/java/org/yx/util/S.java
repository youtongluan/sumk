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

import org.yx.common.GsonHelper;
import org.yx.common.lock.SLock;
import org.yx.util.kit.BeanConverter;
import org.yx.util.secury.AESEncryptor;
import org.yx.util.secury.Encryptor;
import org.yx.util.secury.Hasher;
import org.yx.util.secury.MD5;

import com.google.gson.Gson;

public abstract class S {

	/**
	 * 加密器，一般是AES对称加密
	 */
	public static final Encryptor encryptor = new AESEncryptor();

	/**
	 * hash工具，就是大家常说的md5
	 */
	public static final Hasher hasher = new MD5();

	/**
	 * 分布式锁
	 */
	public static final SLock lock = SLock.inst;

	public static final BeanConverter beans = new BeanConverter();

	public static final Gson json = GsonHelper.gson("sumk");
}
