package org.yx.http.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yx.http.HttpInfo;

public class WebContext {
	private HttpInfo info;
	private HttpServletRequest httpRequest;
	private HttpServletResponse httpResponse;
	private String charset = "UTF-8";
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

	public String getCharset() {
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

	void setCharset(String charset) {
		if (charset != null && charset.length() > 0) {
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
