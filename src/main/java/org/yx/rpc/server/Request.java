package org.yx.rpc.server;

import org.yx.rpc.client.Req;

public class Request extends Req {
	/**
	 * 服务器端接收到req请求的时间
	 */
	private long startInServer = System.currentTimeMillis();

	public long getStartInServer() {
		return startInServer;
	}

}
