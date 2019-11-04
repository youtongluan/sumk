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

import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import org.yx.rpc.ReqProtocol;
import org.yx.rpc.codec.Protocols;

public class Req {

	public Req() {

	}

	private String u;

	private String n;

	private transient String sn;

	private transient String spanId;

	private transient String traceId;

	private String j;

	private String[] p;

	private String a;

	private String secret;

	private String sign;

	private String src;

	private long s;

	private int z;

	private Map<String, String> attachments;

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
		return j;
	}

	public void setJsonedParam(String jsonedParam) {
		this.j = jsonedParam;
	}

	public String[] getParamArray() {
		return p;
	}

	public void setParamArray(String[] paramArray) {
		this.p = paramArray;
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

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public void clearParams() {
		this.j = null;
		this.p = null;
	}

	public void setTest(boolean b) {
		this.z = z | ReqProtocol.TEST;
	}

	public boolean isTest() {
		return (z & ReqProtocol.TEST) != 0;
	}

	public String getUserId() {
		return this.u;
	}

	public void setUserId(String userId) {
		this.u = userId;
	}

	public Map<String, String> getAttachments() {
		return attachments;
	}

	public void setAttachments(Map<String, String> attachments) {
		this.attachments = attachments;
	}

	public int paramProtocol() {
		if (this.j != null) {
			return Protocols.REQ_PARAM_JSON;
		}
		return Protocols.REQ_PARAM_ORDER;
	}

}
