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
package org.yx.http.handler;

import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.http.HttpUtil;

public class WebContext {
	private HttpInfo info;
	private HttpServletRequest httpRequest;
	private HttpServletResponse httpResponse;
	private Charset charset = HttpUtil.DEFAULT_CHARSET;
	private String sign;
	private Map<String, String> headers;
	private Object data;
	private Integer respCode = null;
	private Object result = null;
	private byte[] key;
	private String act;

	public Integer getRespCode() {
		return respCode;
	}

	public void setRespCode(Integer respCode) {
		this.respCode = respCode;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public WebContext(String act, HttpInfo info, HttpServletRequest req, HttpServletResponse resp) {
		this.act = act;
		this.info = info;
		this.httpRequest = req;
		this.httpResponse = resp;
	}

	public Charset getCharset() {
		return charset;
	}

	public Object getData() {
		return data;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public HttpServletRequest getHttpRequest() {
		return httpRequest;
	}

	public HttpServletResponse getHttpResponse() {
		return httpResponse;
	}

	public HttpInfo getInfo() {
		return info;
	}

	public String getSign() {
		return sign;
	}

	void setCharset(Charset charset) {
		if (charset != null) {
			this.charset = charset;
		}

	}

	void setData(Object data) {
		this.data = data;
	}

	void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	void setSign(String sign) {
		this.sign = sign;
	}

	public byte[] getKey() {
		return key;
	}

	void setKey(byte[] key) {
		this.key = key;
	}

	String getAct() {
		return act;
	}

}
