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

import java.util.Objects;

public abstract class CodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	protected final String code;

	public CodeException(String code, String msg) {
		super(msg);
		this.code = Objects.requireNonNull(code);
	}

	public CodeException(String code, String msg, Throwable exception) {
		super(msg, exception);
		this.code = Objects.requireNonNull(code);
	}

	public String getCode() {
		return code;
	}

	public boolean isSameCode(String expect) {
		return this.code.equals(expect);
	}

	@Override
	public String getLocalizedMessage() {
		return new StringBuilder().append(this.getMessage()).append(" (").append(code).append(")").toString();
	}

	@Override
	public final String toString() {
		return new StringBuilder(this.getClass().getSimpleName()).append(": ").append(this.getLocalizedMessage())
				.toString();
	}

}
