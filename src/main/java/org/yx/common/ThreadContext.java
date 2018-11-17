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

import java.util.HashMap;
import java.util.Map;

import org.yx.conf.AppInfo;
import org.yx.rpc.Attachable;
import org.yx.util.StringUtil;

public class ThreadContext implements Attachable {

	private static final String TEST = "sumk.test";

	public static enum ActionType {
		HTTP, RPC, OTHER
	}

	private final ActionType type;

	private final String act;

	private String rootSn;

	private String contextSn;

	private String userId;

	private boolean test;

	private Map<String, String> attachments;

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean isTest) {
		String t = AppInfo.get(TEST);
		if (t != null && t.length() > 0) {
			this.test = isTest;
		}
	}

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

	private static final ThreadLocal<ThreadContext> holder = new ThreadLocal<ThreadContext>() {

		@Override
		protected ThreadContext initialValue() {

			return new ThreadContext(ActionType.OTHER, null, false);
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

	public void setRootSnIfAbsent(String sn) {
		if (this.rootSn == null) {
			this.rootSn = sn;
		}
	}

	public String userId() {
		return userId;
	}

	public void userId(String userId) {
		this.userId = userId;
	}

	public String userIdOrContextSN() {
		String sn = userId;
		if (StringUtil.isEmpty(sn)) {
			sn = contextSn;
		}
		return sn;
	}

	@Override
	public Map<String, String> getAttachments() {
		return attachments;
	}

	@Override
	public void setAttachments(Map<String, String> attachments) {
		this.attachments = attachments;
	}

	@Override
	public void setAttachment(String key, String value) {
		if (attachments == null) {
			attachments = new HashMap<>();
		}
		attachments.put(key, value);
	}

	@Override
	public void setAttachmentIfAbsent(String key, String value) {
		if (attachments == null) {
			attachments = new HashMap<>();
		}
		attachments.putIfAbsent(key, value);
	}

	@Override
	public void addAttachments(Map<String, String> attachments) {
		if (attachments == null) {
			return;
		}
		if (this.attachments == null) {
			this.attachments = new HashMap<>();
		}
		this.attachments.putAll(attachments);
	}

	@Override
	public void addAttachmentsIfAbsent(Map<String, String> attachments) {
		if (attachments == null) {
			return;
		}
		for (Map.Entry<String, String> entry : attachments.entrySet()) {
			setAttachmentIfAbsent(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public String getAttachment(String key) {
		if (attachments == null) {
			return null;
		}
		return attachments.get(key);
	}

	@Override
	public String getAttachment(String key, String defaultValue) {
		if (attachments == null) {
			return defaultValue;
		}
		return attachments.getOrDefault(key, defaultValue);
	}

}
