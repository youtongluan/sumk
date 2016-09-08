package org.yx.rpc.server;

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
	public SoaException getException() {
		return exception;
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
	public void setException(SoaException exception) {
		this.exception = exception;
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
	
}
