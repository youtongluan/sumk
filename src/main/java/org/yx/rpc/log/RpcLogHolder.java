package org.yx.rpc.log;

import java.util.Objects;

import org.yx.common.Host;
import org.yx.rpc.client.Req;
import org.yx.rpc.client.RpcResult;

public class RpcLogHolder {

	private static RpcLogHandler handler;

	public static RpcLogHandler getHandler() {
		return handler;
	}

	public static void setHandler(RpcLogHandler handler) {
		RpcLogHolder.handler = Objects.requireNonNull(handler);
	}

	public static void handle(Host url, Req req, RpcResult result, long receiveTime) {
		if (handler == null) {
			return;
		}
		handler.handle(new RpcLog(url, req, result, receiveTime));
	}
}
