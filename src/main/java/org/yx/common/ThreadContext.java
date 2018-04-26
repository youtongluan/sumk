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
package org.yx.common;

import org.yx.conf.AppInfo;

public class ThreadContext {

	private static final String TEST = "sumk.test";

	public static enum ActionType {
		HTTP, RPC
	}

	private final ActionType type;

	private final String act;

	private String rootSn;

	private String contextSn;

	private boolean test;

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean isTest) {
		String t = AppInfo.get(TEST);
		if (t != null && t.length() > 0) {
			this.test = isTest;
		}
	}

	public Object info;

	private ThreadContext(ActionType type, String act, boolean isTest) {
		this.type = type;
		this.act = act;
		this.setTest(isTest);
	}

	public ActionType type() {
		return type;
	}

	public String act() {
		return act;
	}

	public String contextSn() {
		return this.contextSn;
	}

	public void contextSn(String contextSn) {
		this.contextSn = contextSn;
	}

	private static final ThreadLocal<ThreadContext> holder = new ThreadLocal<ThreadContext>() {

		@Override
		protected ThreadContext initialValue() {

			return new ThreadContext(null, null, false);
		}

	};

	public static ThreadContext httpContext(String act, String thisIsTest) {
		boolean test = false;
		if (thisIsTest != null && thisIsTest.equals(AppInfo.get(TEST))) {
			test = true;
		}
		ThreadContext c = new ThreadContext(ActionType.HTTP, act, test);
		holder.set(c);
		return c;
	}

	public static ThreadContext rpcContext(String act, String rootSn, String contextSn, boolean isTest) {
		ThreadContext c = new ThreadContext(ActionType.RPC, act, isTest);
		c.rootSn = rootSn;
		c.contextSn = contextSn;
		holder.set(c);
		return c;
	}

	public static ThreadContext get() {
		return holder.get();
	}

	public static void remove() {
		holder.remove();
	}

	public String rootSn() {
		return rootSn;
	}

}
