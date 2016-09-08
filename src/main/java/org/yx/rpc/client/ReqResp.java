package org.yx.rpc.client;

import org.yx.rpc.server.Response;

public class ReqResp {
	private Req req;
	private Response resp;
	public Req getReq() {
		return req;
	}
	public Response getResp() {
		return resp;
	}
	public ReqResp(Req req, Response resp) {
		super();
		this.req = req;
		this.resp = resp;
	}
	
}
