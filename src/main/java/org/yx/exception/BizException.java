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

import org.yx.common.CodedMessage;

public class BizException extends CodeException {

	private static final long serialVersionUID = 453453454L;

	public BizException(int code, String msg) {
		super(msg);
		this.code = code;
	}

	public BizException(int code, String msg, Throwable exception) {
		super(code, msg, exception);
	}

	public static void throwException(int code, String msg) throws BizException {
		throw new BizException(code, msg);
	}

	public static void throwException(int code, String msg, Throwable exception) throws BizException {
		throw new BizException(code, msg, exception);
	}

	/**
	 * 
	 * @param message
	 *            不能为null
	 */
	public static void throwException(CodedMessage message) throws BizException {
		throw new BizException(message.code(), message.message());
	}

}
