/**
 * Copyright (C) 2016 - 2017 youtongluan.
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
 * 这个异常表示是执行sumk框架中的某个方法抛出的异常
 * 
 * @author 游夏
 *
 */
public class SumkException extends CodeException {

	private static final long serialVersionUID = 3453246435546L;

	public SumkException(int code, String msg) {
		super(code, msg);
	}

	public SumkException(int code, String msg, Throwable exception) {
		super(code, msg, exception);
	}

	public static void throwException(int code, String msg) throws SumkException {
		throw new SumkException(code, msg);
	}

	public static void throwException(int code, String msg, Throwable exception) throws SumkException {
		if (SumkException.class.isInstance(exception)) {
			throw (SumkException) exception;
		}
		throw new SumkException(code, msg, exception);
	}

	public static void throwException(String msg) throws SumkException {
		throw new SumkException(0, msg);
	}

	public static void throwException(String msg, Throwable exception) throws SumkException {
		if (SumkException.class.isInstance(exception)) {
			throw (SumkException) exception;
		}
		throw new SumkException(0, msg, exception);
	}

	public static SumkException create(Throwable e) {
		if (SumkException.class.isInstance(e)) {
			throw (SumkException) e;
		}
		return new SumkException(0, e.getMessage(), e);
	}

}
