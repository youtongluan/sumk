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

import org.yx.conf.AppInfo;
import org.yx.util.ExceptionUtil;

public final class SoaException extends CodeException {

	private static final long serialVersionUID = 453453343454L;

	private String detailError;
	private String exceptionClz;
	private boolean bizException;

	public String getDetailError() {
		return this.detailError;
	}

	public String getExceptionClz() {
		return exceptionClz;
	}

	public SoaException(Throwable e, int code, String msg) {
		super(e instanceof BizException ? ((BizException) e).getCode() : code,
				e instanceof BizException ? e.getMessage() : msg);
		this.exceptionClz = e == null ? null : e.getClass().getName();
		this.detailError = getException(e);
		this.bizException = e instanceof BizException;
	}

	public SoaException(int code, String msg, String detail) {
		super(code, msg);
		this.exceptionClz = null;
		this.detailError = detail;
	}

	private static String getException(Throwable e) {
		if (e == null) {
			return null;
		}
		if (AppInfo.getBoolean("sumk.rpc.detailError", false)) {
			StringBuilder sb = new StringBuilder(64);
			ExceptionUtil.printStackTrace(sb, e);
			if (sb.length() >= 4000) {
				return sb.substring(0, 4000);
			}
			return sb.toString();
		}
		return e.getMessage();
	}

	private boolean printRawStackTrace() {
		return AppInfo.getBoolean("sumk.rpc.printRawStackTrace", false);
	}

	@Override
	public Throwable fillInStackTrace() {
		if (printRawStackTrace()) {
			super.fillInStackTrace();
		}
		return this;
	}

	private String buildStackTraceMessage() {
		StringBuilder sb = new StringBuilder().append(this.toString());
		if (this.exceptionClz != null && this.exceptionClz.length() > 0) {
			sb.append("\n\tcause by " + this.exceptionClz);
			if (this.detailError != null && this.detailError.length() > 0) {
				sb.append(":").append(this.detailError);
			}
		}
		return sb.toString();
	}

	@Override
	public void printStackTrace(PrintStream s) {
		if (printRawStackTrace()) {
			super.printStackTrace(s);
			return;
		}

		s.println(this.buildStackTraceMessage());
	}

	@Override
	public void printStackTrace(PrintWriter s) {
		if (printRawStackTrace()) {
			super.printStackTrace(s);
			return;
		}
		s.println(this.buildStackTraceMessage());
	}

	public boolean isBizException() {
		return bizException;
	}
}
