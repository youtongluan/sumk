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
package org.yx.common;

import org.yx.util.UUIDSeed;

public class ThreadContext {
	public static enum ActionType {
		HTTP, RPC
	}

	private ActionType type;

	private String act;

	private String sn0;

	/**
	 * 附带的信息，有可能为null
	 */
	public Object info;

	private ThreadContext(ActionType type, String act) {
		super();
		this.type = type;
		this.act = act;
	}

	public ActionType getType() {
		return type;
	}

	public String getAct() {
		return act;
	}

	public String getSn0() {
		return sn0;
	}

	private static ThreadLocal<ThreadContext> holder = new ThreadLocal<ThreadContext>() {

		@Override
		protected ThreadContext initialValue() {
			return new ThreadContext(null, null);
		}

	};

	public static ThreadContext httpContext(String act) {
		ThreadContext c = new ThreadContext(ActionType.HTTP, act);
		c.sn0 = ActionType.HTTP + "_" + UUIDSeed.seq();
		holder.set(c);
		return c;
	}

	public static ThreadContext rpcContext(String act, String sn0) {
		ThreadContext c = new ThreadContext(ActionType.RPC, act);
		c.sn0 = sn0;
		holder.set(c);
		return c;
	}

	public static ThreadContext get() {
		return holder.get();
	}

	public static void remove() {
		holder.remove();
	}
}
