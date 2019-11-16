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

import java.util.Objects;

public class SimpleLoggerHolder {

	public static final SimpleLogger CONSOLE_LOG = new SimpleLogger() {
		@Override
		public void debug(String module, String msg) {

		}

		@Override
		public void info(String module, String msg) {
			System.out.println(msg);
		}

		@Override
		public void error(String module, String msg, Throwable e) {
			if (msg != null) {
				System.out.println(msg);
			}
			if (e != null) {
				e.printStackTrace();
			}
		}

		@Override
		public void error(String module, String msg) {
			System.out.println(msg);
		}
	};

	public static final SimpleLogger SLF4J_LOG = new SimpleLogger() {
		@Override
		public void debug(String module, String msg) {
			Log.get(module).debug(msg);
		}

		@Override
		public void info(String module, String msg) {
			Log.get(module).info(msg);
		}

		@Override
		public void error(String module, String msg, Throwable e) {
			if (e == null) {
				Log.get(module).error(msg);
				return;
			}
			if (msg == null || msg.isEmpty()) {
				msg = e.toString();
			}
			Log.get(module).error(msg, e);
		}

		@Override
		public void error(String module, String msg) {
			Log.get(module).error(msg);
		}
	};

	private static SimpleLogger inst = CONSOLE_LOG;

	public static SimpleLogger inst() {
		return inst;
	}

	public static void setLogger(SimpleLogger inst) {
		SimpleLoggerHolder.inst = Objects.requireNonNull(inst);
	}

	public static void error(String module, Throwable e) {
		inst.error(module, e.getMessage(), e);
	}

}
