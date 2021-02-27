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
package org.yx.rpc.client;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import org.yx.rpc.codec.Protocols;
import org.yx.rpc.codec.ReqParamType;

public class Req implements Serializable {

	private static final long serialVersionUID = 123L;

	private String u;

	private String n;

	private transient String sn;

	private transient String spanId;

	private transient String traceId;

	private transient Object params;

	private String a;

	private String f;

	private long s;

	protected int z;

	private transient int serverProtocol;

	private Map<String, String> t;

	private void parseSn() {
		if (n == null) {
			this.sn = "";
			return;
		}
		String[] ss = n.split(",", 3);
		this.sn = ss[0];
		this.traceId = ss.length < 2 ? this.sn : ss[1];
		this.spanId = ss.length < 3 ? "#" : ss[2];
		if (this.traceId == null || this.traceId.length() == 0) {
			this.traceId = sn;
		}
	}

	public void setFullSn(String sn, String traceId, String spanId) {
		StringJoiner joiner = new StringJoiner(",");
		joiner.add(Objects.requireNonNull(sn));
		joiner.add(traceId == null ? "" : traceId);
		joiner.add(Objects.requireNonNull(spanId));
		this.n = joiner.toString();
	}

	public String getJsonedParam() {
		return this.hasFeature(ReqParamType.REQ_PARAM_JSON) ? (String) params : null;
	}

	public void setParams(int type, Object params) {
		this.setParamType(type & Protocols.REQUEST_PARAM_TYPES);
		this.params = params;
	}

	private void setParamType(int type) {
		int p = this.z & (~Protocols.REQUEST_PARAM_TYPES);
		this.z = p | type;
	}

	public Object getParams() {
		return this.params;
	}

	public String[] getParamArray() {
		return this.hasFeature(ReqParamType.REQ_PARAM_ORDER) ? (String[]) params : null;
	}

	public long getStart() {
		return s;
	}

	public void setStart(long start) {
		this.s = start;
	}

	public String getSn() {
		if (sn == null) {
			parseSn();
		}
		return sn;
	}

	public String getTraceId() {
		if (sn == null) {
			parseSn();
		}
		return this.traceId;
	}

	public String getSpanId() {
		if (sn == null) {
			parseSn();
		}
		return this.spanId;
	}

	public String getApi() {
		return a;
	}

	public void setApi(String api) {
		this.a = api;
	}

	public String getFrom() {
		return f;
	}

	public void setFrom(String src) {
		this.f = src;
	}

	public void addFeature(int feature) {
		this.z |= feature;
	}

	public boolean hasFeature(int feature) {
		return Protocols.hasFeature(z, feature);
	}

	public int protocol() {
		return this.z;
	}

	public void setTest(boolean b) {
		addFeature(Protocols.TEST);
	}

	public boolean isTest() {
		return hasFeature(Protocols.TEST);
	}

	public String getUserId() {
		return this.u;
	}

	public void setUserId(String userId) {
		this.u = userId;
	}

	public Map<String, String> getAttachments() {
		return t;
	}

	public void setAttachments(Map<String, String> attachments) {
		this.t = attachments;
	}

	public void initAcceptResponseTypes(int protocol) {
		protocol &= Protocols.RESPONSE_FULL_TYPES;
		this.z |= protocol;
	}

	public int getAcceptResponseTypes() {
		return this.z & Protocols.RESPONSE_FULL_TYPES;
	}

	public int getServerProtocol() {
		return serverProtocol;
	}

	public void setServerProtocol(int serverProtocol) {
		this.serverProtocol = serverProtocol;
	}

}
