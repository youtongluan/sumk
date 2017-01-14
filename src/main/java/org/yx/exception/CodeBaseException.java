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

public abstract class CodeBaseException extends RuntimeException {

	private static final long serialVersionUID = 5037279306320394281L;
	/**
	 * 等于0的话，就相当于没有code。有的异常code有实际意义，有的异常，code仅仅是为了查询方便
	 */
	protected int code;

	public int getCode() {
		return code;
	}

	public CodeBaseException(String msg) {
		super(msg);
	}

	public CodeBaseException(int code, String msg) {
		super(msg);
		this.code = code;
	}

	public CodeBaseException(int code, String msg, Throwable exception) {
		super(msg, exception);
		this.code = code;
	}

	@Override
	public final String toString() {
		return code == 0 ? super.toString() : super.toString() + " (" + code + ")";
	}
}
