package org.yx.rpc.server;

import org.yx.exception.BizException;
import org.yx.exception.SoaException;

public class Response {
	/**
	 * request的ID
	 */
	private String sn;
	private String json;
	private SoaException exception;

	private Long ms;

	public String getSn() {
		return sn;
	}

	public String getJson() {
		return json;
	}

	public Long getMs() {
		return ms;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public void setMs(Long ms) {
		this.ms = ms;
	}

	public Response() {
		super();
	}

	public Response(String sn) {
		super();
		this.sn = sn;
	}

	RuntimeException getException() {
		return exception;
	}

	public void setException(SoaException exception) {
		this.exception = exception;
	}

	public void checkException() {
		if (this.exception == null) {
			return;
		}
		if (BizException.class.getName().equals(exception.getExceptionClz())) {
			BizException.throwException(this.exception.getCode(), this.getException().getMessage(), this.exception);
		}
		throw exception;
	}

}
