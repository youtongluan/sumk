package org.yx.rpc.log;

import org.yx.common.Host;
import org.yx.common.context.LogContext;
import org.yx.rpc.client.Req;
import org.yx.rpc.client.RpcResult;

/**
 * 这里的属性，以及属性的子属性，都要只读，不要去修改它<BR>
 * Req里面有开始时间。<BR>
 * 全部时间都不为null
 * 
 */
public class RpcLog {
	private final Host server;
	private final Req req;
	private final RpcResult result;
	private final LogContext originLogContext;
	private long receiveTime;

	public RpcLog(Host server, Req req, LogContext logContext, RpcResult result, long receiveTime) {
		this.server = server;
		this.req = req;
		this.result = result;
		this.receiveTime = receiveTime;
		this.originLogContext = logContext;
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

	public LogContext getOriginLogContext() {
		return originLogContext;
	}

	@Override
	public String toString() {
		return "RpcLog [server=" + server + ", req=" + req + ", result=" + result + "]";
	}

}
