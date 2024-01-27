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
package org.yx.exception;

import org.yx.conf.AppInfo;

/**
 * 它的code最好要大于0，并且避开3位数，因为内置的错误码都是3位数
 */
public class BizException extends CodeException {

	private static final long serialVersionUID = 453453454L;

	public BizException(int code, String msg) {
		super(String.valueOf(code), msg);
	}

	public BizException(String code, String msg) {
		super(code, msg);
	}

	public static BizException create(int code, String msg) {
		return new BizException(code, msg);
	}

	public static void throwException(int code, String msg) throws BizException {
		throw new BizException(code, msg);
	}

	public static BizException create(String code, String msg) {
		return new BizException(code, msg);
	}

	public static void throwException(String code, String msg) throws BizException {
		throw new BizException(code, msg);
	}

	@Override
	public Throwable fillInStackTrace() {
		if (AppInfo.getBoolean("sumk.bizexception.fullstack", true)) {
			return super.fillInStackTrace();
		}
		return this;
	}
}
