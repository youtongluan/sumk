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

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 这个异常是框架使用，开发者只需去捕获这个异常，不要去抛出这个异常
 */
public class SoaException extends CodeException {

	private static final long serialVersionUID = 453453343454L;

	private String detailError;
	private String exceptionClz;

	public String getDetailError() {
		return detailError;
	}

	public String getExceptionClz() {
		return exceptionClz;
	}

	public SoaException(int code, String msg, Throwable e) {
		super(BizException.class.isInstance(e) ? e.getMessage() : msg);
		this.code = BizException.class.isInstance(e) ? ((BizException) e).getCode() : code;
		this.detailError = getException(e);
		this.exceptionClz = e == null ? "" : e.getClass().getName();
	}

	private static String getException(Throwable e) {
		if (e == null) {
			return "Exception is NULL";
		}
		StringWriter sw = new StringWriter();
		PrintWriter w = new PrintWriter(sw);
		e.printStackTrace(w);
		if (sw.toString().length() >= 4000) {
			return sw.toString().substring(0, 4000);
		}
		return sw.toString();
	}

}
