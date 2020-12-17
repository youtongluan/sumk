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
package org.yx.log;

import org.slf4j.Logger;

/**
 * 这个类仅限于sumk框架内部使用
 */
public final class Logs {

	public static Logger http() {
		return Log.get("sumk.http");
	}

	public static Logger rpc() {
		return Log.get("sumk.rpc");
	}

	public static Logger system() {
		return Log.get("sumk.sys");
	}

	public static Logger db() {
		return Log.get("sumk.db");
	}

	public static Logger redis() {
		return Log.get("sumk.redis");
	}

	public static Logger ioc() {
		return Log.get("sumk.ioc");
	}

	public static Logger asm() {
		return Log.get("sumk.asm");
	}
}
