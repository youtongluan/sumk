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
package org.yx.rpc.client;

public class Req {

	protected String sn;

	protected String sn0;

	protected Integer ver;

	protected String jsonedParam;

	protected String[] paramArray;

	/**
	 * 将所有的参数序列化为一个json
	 * 
	 * @return
	 */
	public String getJsonedParam() {
		return jsonedParam;
	}

	public void setJsonedParam(String jsonedParam) {
		this.jsonedParam = jsonedParam;
	}

	public String[] getParamArray() {
		return paramArray;
	}

	public void setParamArray(String[] paramArray) {
		this.paramArray = paramArray;
	}

	protected String method;

	protected String secret;

	protected String sign;

	protected String src;

	protected long start;

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSn0() {
		return sn0;
	}

	public void setSn0(String sn0) {
		this.sn0 = sn0;
	}

	public Integer getVer() {
		return ver;
	}

	public void setVer(Integer ver) {
		this.ver = ver;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
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
		this.jsonedParam = null;
		this.paramArray = null;
	}

}
