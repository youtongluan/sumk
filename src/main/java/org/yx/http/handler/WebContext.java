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
package org.yx.http.handler;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.http.HttpSettings;

public class WebContext {
	private HttpActionNode node;
	private HttpServletRequest httpRequest;
	private HttpServletResponse httpResponse;
	private Charset charset = HttpSettings.DEFAULT_CHARSET;
	private String sign;
	private Object data;

	private Object result;
	private byte[] key;
	private String act;
	private transient String str_data;
	private transient String str_resp;
	private final long beginTime;
	private transient Object attach;

	public Object getAttach() {
		return attach;
	}

	public void setAttach(Object attach) {
		this.attach = attach;
	}

	public long beginTime() {
		return this.beginTime;
	}

	public String dataInString() {
		return str_data;
	}

	public String respInString() {
		return str_resp;
	}

	public Object result() {
		return result;
	}

	void result(Object result) {
		this.result = result;
		if (String.class.isInstance(result)) {
			this.str_resp = (String) result;
		}
	}

	public WebContext(String act, HttpActionNode node, HttpServletRequest req, HttpServletResponse resp,
			long beginTime) {
		this.act = act;
		this.node = node;
		this.httpRequest = req;
		this.httpResponse = resp;
		this.beginTime = beginTime;
	}

	public Charset charset() {
		return charset;
	}

	public Object data() {
		return data;
	}

	public byte[] getDataInByteArray() {
		if (String.class.isInstance(data)) {
			return ((String) data).getBytes(charset());
		}
		return (byte[]) data;
	}

	public HttpServletRequest httpRequest() {
		return httpRequest;
	}

	public HttpServletResponse httpResponse() {
		return httpResponse;
	}

	public HttpActionNode httpNode() {
		return node;
	}

	public String sign() {
		return sign;
	}

	void charset(Charset charset) {
		if (charset != null) {
			this.charset = charset;
		}

	}

	void data(Object data) {
		this.data = data;
		if (String.class.isInstance(data)) {
			this.str_data = (String) data;
		}
	}

	void sign(String sign) {
		this.sign = sign;
	}

	public byte[] key() {
		return key;
	}

	void key(byte[] key) {
		this.key = key;
	}

	public String act() {
		return act;
	}

}
