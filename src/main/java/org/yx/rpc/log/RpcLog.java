package org.yx.rpc.log;

import org.yx.common.Host;
import org.yx.rpc.client.Req;
import org.yx.rpc.client.RpcResult;

/**
 * 这里的属性，以及属性的子属性，都要只读，不要去修改它<BR>
 * Req里面有开始时间。
 * 
 */
public class RpcLog {
	private Host server;
	private Req req;
	private RpcResult result;
	private long receiveTime;

	public RpcLog(Host server, Req req, RpcResult result, long receiveTime) {
		super();
		this.server = server;
		this.req = req;
		this.result = result;
		this.receiveTime = receiveTime;
	}

	public long getReceiveTime() {
		return receiveTime;
	}

	public Host getServer() {
		return server;
	}

	public Req getReq() {
		return req;
	}

	public RpcResult getResult() {
		return result;
	}

	@Override
	public String toString() {
		return "RpcLog [server=" + server + ", req=" + req + ", result=" + result + "]";
	}

}
