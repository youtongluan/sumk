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
package org.yx.common.context;

import java.util.HashMap;
import java.util.Map;

import org.yx.exception.SumkException;
import org.yx.main.SumkServer;

public final class ActionContext implements org.yx.common.context.Attachable, Cloneable {

	private LogContext logContext;

	/**
	 * 用来做自增长的
	 */
	private int spanSeed;

	public boolean isTest() {
		return logContext.test;
	}

	ActionContext(LogContext logContext) {
		this.logContext = logContext;
	}

	public String act() {
		return logContext.act;
	}

	public String traceId() {
		return logContext.traceId;
	}

	public String spanId() {
		return logContext.spanId;
	}

	private static final ThreadLocal<ActionContext> holder = new ThreadLocal<ActionContext>() {

		@Override
		protected ActionContext initialValue() {

			return new ActionContext(LogContext.EMPTY);
		}

	};

	public static ActionContext newContext(String act, String traceId, boolean test) {
		return newContext(act, traceId, null, null, test, null);
	}

	public static ActionContext newContext(String act, String traceId, String spanId, String userId, boolean isTest,
			Map<String, String> attachments) {
		LogContext logContext = LogContext.create(act, traceId, spanId, userId, isTest && SumkServer.isTest(),
				attachments);
		return newContext(logContext);
	}

	public static ActionContext newContext(LogContext logContext) {
		ActionContext c = new ActionContext(logContext);
		holder.set(c);
		return c;
	}

	public static void store(ActionContext c) {
		holder.set(c);
	}

	public static ActionContext current() {
		return holder.get();
	}

	public static void remove() {
		holder.remove();
	}

	public void setTraceIdIfAbsent(String traceId) {
		LogContext lc = this.logContext;
		if (lc.traceId != null) {
			return;
		}
		this.logContext = LogContext.create(lc.act, traceId, lc.spanId, lc.userId, lc.test, lc.attachments);
	}

	public String userId() {
		return logContext.userId;
	}

	public void userId(String userId) {
		LogContext lc = this.logContext;
		this.logContext = LogContext.create(lc.act, lc.traceId, lc.spanId, userId, lc.test, lc.attachments);
	}

	@Override
	public Map<String, String> attachmentView() {
		return logContext.attachments;
	}

	@Override
	public void setAttachment(String key, String value) {
		if (value == null) {
			if (this.logContext.attachments != null) {
				Map<String, String> attachments = new HashMap<>(this.logContext.attachments);
				attachments.remove(key);
				this.logContext = LogContext.create(this.logContext, attachments);
			}
			return;
		}
		Map<String, String> attachments = this.logContext.attachments;
		attachments = attachments == null ? new HashMap<>() : new HashMap<>(attachments);
		attachments.put(key, value);
		this.logContext = LogContext.create(this.logContext, attachments);
	}

	@Override
	public String getAttachment(String key) {
		Map<String, String> attachments = this.logContext.attachments;
		if (attachments == null) {
			return null;
		}
		return attachments.get(key);
	}

	public String nextSpanId() {
		LogContext lc = this.logContext;
		if (lc.traceId == null) {
			return "1";
		}
		int seed;
		synchronized (this) {
			seed = ++this.spanSeed;
		}
		String sp = lc.spanId;
		if (sp == null) {
			return String.valueOf(seed);
		}
		return new StringBuilder().append(lc.spanId).append('.').append(seed).toString();
	}

	public LogContext logContext() {
		return this.logContext;
	}

	public ActionContext clone() {
		try {
			return (ActionContext) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new SumkException(234235, "clone not supported");
		}
	}
}
