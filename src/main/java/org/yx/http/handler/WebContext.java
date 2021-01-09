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
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.common.context.NodeContext;
import org.yx.http.act.HttpActionNode;

public class WebContext extends NodeContext<HttpActionNode> {
	private final String rawAct;
	private final HttpServletRequest httpRequest;
	private final HttpServletResponse httpResponse;
	private final Charset charset;
	private String sign;
	private Object data;

	private int lowestOrder;

	private Object result;
	private transient String str_data;
	private transient String str_resp;
	private final long beginTime;

	private boolean failed;

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

	void result(Object result, boolean cacheInStr) {
		this.result = result;
		if (cacheInStr && result != null && String.class == result.getClass()) {
			this.str_resp = (String) result;
		}
	}

	public WebContext(String rawAct, HttpActionNode node, HttpServletRequest req, HttpServletResponse resp,
			long beginTime, Charset charset) {
		super(node);
		this.rawAct = Objects.requireNonNull(rawAct);
		this.httpRequest = Objects.requireNonNull(req);
		this.charset = Objects.requireNonNull(charset);
		this.httpResponse = resp;
		this.beginTime = beginTime;
	}

	public Charset charset() {
		return this.charset;
	}

	public Object data() {
		return data;
	}

	public byte[] getDataInByteArray() {
		if (data instanceof String) {
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

	public String sign() {
		return sign;
	}

	void data(Object data) {
		this.data = data;
		if (data != null && String.class == data.getClass()) {
			this.str_data = (String) data;
		}
	}

	void sign(String sign) {
		this.sign = sign;
	}

	/**
	 * 开发者定义的原始act，这里的act不包含逗号。 如果有逗号分隔，这里就只是@Web中的一段
	 * 
	 * @return 就是@Web上定义的act
	 */
	public String rawAct() {
		return rawAct;
	}

	public int getLowestOrder() {
		return lowestOrder;
	}

	public void setLowestOrder(int lowestOrder) {
		this.lowestOrder = lowestOrder;
	}

	public List<String> tags() {
		return this.node.tags();
	}

	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}

}
