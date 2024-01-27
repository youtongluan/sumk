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

/**
 * 这个异常表示是执行sumk框架中的某个方法抛出的异常,它的code不能大于0<BR>
 * 框架内部使用，业务异常请用BizException
 *
 */
public class SumkException extends CodeException {

	private static final long serialVersionUID = 3453246435546L;

	public SumkException(int code, String msg) {
		super(negative(code), msg);
	}

	public SumkException(int code, String msg, Throwable exception) {
		super(negative(code), msg, exception);
	}

	private static String negative(int code) {
		return code > 0 ? String.valueOf(-code) : String.valueOf(code);
	}

	public static SumkException wrap(Throwable e) {
		if (e instanceof SumkException) {
			throw (SumkException) e;
		}
		return new SumkException(-34534565, e.getMessage(), e);
	}

}
