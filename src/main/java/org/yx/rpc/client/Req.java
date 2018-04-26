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

import java.util.StringJoiner;

import org.yx.rpc.ReqProtocol;

public class Req {

	public Req() {

	}

	private String n;

	private String sn;

	private String rootSn;

	private String fatherSn;

	private String j;

	private String[] p;

	private String a;

	private String secret;

	private String sign;

	private String src;

	private long s;

	private int z;

	private void parseSn() {
		if (n == null) {
			this.sn = "";
			return;
		}
		String[] ss = n.split(",", 3);
		this.sn = ss[0];
		this.rootSn = ss.length < 2 ? this.sn : ss[1];
		this.fatherSn = ss.length < 3 ? this.rootSn : ss[2];
		;
	}

	public void setFullSn(String sn, String rootSn, String fatherSn) {
		StringJoiner joiner = new StringJoiner(",");
		joiner.add(sn);
		if (rootSn == null) {
			this.n = joiner.toString();
			return;
		}
		joiner.add(rootSn);
		if (fatherSn == null) {
			this.n = joiner.toString();
			return;
		}
		joiner.add(fatherSn);
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

	public String getRootSn() {
		if (sn == null) {
			parseSn();
		}
		return rootSn;
	}

	public String getFatherSn() {
		if (sn == null) {
			parseSn();
		}
		return fatherSn;
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
		this.z = z & ReqProtocol.TEST;
	}

	public boolean isTest(boolean b) {
		return (z & ReqProtocol.TEST) != 0;
	}

}
