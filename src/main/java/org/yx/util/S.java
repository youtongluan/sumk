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

import java.util.concurrent.ExecutorService;

import org.yx.common.GsonHelper;
import org.yx.common.lock.Locker;
import org.yx.main.SumkThreadPool;
import org.yx.util.helper.ArrayHelper;
import org.yx.util.kit.BeanConverter;
import org.yx.util.secury.AESEncryptor;
import org.yx.util.secury.Base64;
import org.yx.util.secury.Encryptor;
import org.yx.util.secury.Hasher;
import org.yx.util.secury.MD5;

import com.google.gson.Gson;

public final class S {
	public static final ArrayHelper array = new ArrayHelper();

	/**
	 * json工具
	 */
	public static final Gson json = GsonHelper.gson("sumk");

	/**
	 * 系统共用的线程池
	 */
	public static final ExecutorService executor = SumkThreadPool.executor();

	public static final Base64 base64 = Base64.inst;

	/**
	 * 加密器，默认是AES对称加密
	 */
	public static final Encryptor cipher = new AESEncryptor();

	/**
	 * hash工具，就是大家常说的md5
	 */
	public static final Hasher hash = new MD5();

	/**
	 * 分布式锁
	 */
	public static final Locker lock = Locker.inst;
	/**
	 * bean和map的转换，以及属性复制。 只支持第一级field
	 */
	public static final BeanConverter bean = new BeanConverter();

}
