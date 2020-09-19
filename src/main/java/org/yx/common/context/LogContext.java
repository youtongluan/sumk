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

import java.util.Map;

import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

public final class LogContext {
	public static final LogContext EMPTY = new LogContext(null, null, null, null, false, null);

	public final String act;

	public final String traceId;

	public final String spanId;

	public final String userId;

	public final boolean test;

	final Map<String, String> attachments;

	public static LogContext create(String act, String traceId, String spanId, String userId, boolean test,
			Map<String, String> attachments) {
		if (attachments != null && attachments.size() > 0) {
			return new LogContext(act, traceId, spanId, userId, test, attachments);
		}

		if (act == null && traceId == null && spanId == null && userId == null && !test) {
			return EMPTY;
		}
		return new LogContext(act, traceId, spanId, userId, test, null);
	}

	public static LogContext create(LogContext lc, Map<String, String> attachments) {
		return create(lc.act, lc.traceId, lc.spanId, lc.userId, lc.test, attachments);
	}

	private LogContext(String act, String traceId, String spanId, String userId, boolean test,
			Map<String, String> attachments) {
		this.act = act;
		this.traceId = StringUtil.isEmpty(traceId) ? null : traceId;
		this.spanId = spanId;
		this.userId = userId;
		this.test = test;
		this.attachments = CollectionUtil.unmodifyMap(attachments);
	}

	public static LogContext empty() {
		return EMPTY;
	}

	public Map<String, String> unmodifiedAttachs() {
		return this.attachments;
	}

	public String getAct() {
		return act;
	}

	public String getTraceId() {
		return traceId;
	}

	public String getSpanId() {
		return spanId;
	}

	public String getUserId() {
		return userId;
	}

	public boolean isTest() {
		return test;
	}
}