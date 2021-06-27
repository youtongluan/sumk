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
package org.yx.db.conn;

/**
 * 所有字段都有可能为null
 */
public class HookContext {
	private final SumkDataSource write;
	private final SumkDataSource read;
	private final Throwable exception;

	public HookContext(SumkDataSource write, SumkDataSource read, Throwable exception) {
		this.write = write;
		this.read = read;
		this.exception = exception;
	}

	public SumkDataSource getWrite() {
		return write;
	}

	public SumkDataSource getRead() {
		return read;
	}

	public Throwable getException() {
		return exception;
	}

}
