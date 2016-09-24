package org.yx.http.filter;

import java.util.Map;

public class Session {

	public static String SESSIONID = "sid";

	private String sessionId;
	/**
	 * 用于加密使用，如果不需要加密，也不能为null。null表示登陆失败
	 */
	private byte[] key;

	private String errorMsg;

	private Map<String, Object> valueMap;

	public Map<String, Object> getValueMap() {
		return valueMap;
	}

	public void setValueMap(Map<String, Object> valueMap) {
		this.valueMap = valueMap;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Session(String sessionId, byte[] key, String errorMsg) {
		super();
		this.sessionId = sessionId;
		this.key = key;
		this.errorMsg = errorMsg;
	}

}
