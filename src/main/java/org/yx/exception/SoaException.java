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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.yx.conf.AppInfo;

public class SoaException extends CodeException {

	private static final long serialVersionUID = 453453343454L;

	private String detailError;
	private String exceptionClz;

	public String getDetailError() {
		return this.detailError;
	}

	public String getExceptionClz() {
		return exceptionClz;
	}

	public SoaException(int code, String msg, Throwable e) {
		super(BizException.class.isInstance(e) ? ((BizException) e).getCode() : code,
				BizException.class.isInstance(e) ? e.getMessage() : msg);
		this.exceptionClz = e == null ? null : e.getClass().getName();
		this.detailError = getException(e);
	}

	public SoaException(int code, String msg, String detail) {
		super(msg);
		this.code = code;
		this.exceptionClz = null;
		this.detailError = detail;
	}

	private static String getException(Throwable e) {
		if (e == null) {
			return null;
		}
		if (AppInfo.getBoolean("sumk.rpc.detailError", false)) {
			StringWriter sw = new StringWriter();
			PrintWriter w = new PrintWriter(sw);
			e.printStackTrace(w);
			if (sw.toString().length() >= 4000) {
				return sw.toString().substring(0, 4000);
			}
			return sw.toString();
		}
		return e.getMessage();
	}

	@Override
	public void printStackTrace(PrintStream s) {
		if (AppInfo.getBoolean("sumk.rpc.printRawStackTrace", false)) {
			super.printStackTrace(s);
			return;
		}
		StringBuilder sb = new StringBuilder(this.toString());
		if (this.exceptionClz != null && this.exceptionClz.length() > 0) {
			sb.append("\n\tcause by " + this.exceptionClz);
			if (this.detailError != null && this.detailError.length() > 0) {
				sb.append(":").append(this.detailError);
			}
		}
		s.println(sb.toString());
	}
}
