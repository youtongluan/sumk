package org.yx.rpc.log;

import java.util.Objects;

import org.yx.common.Host;
import org.yx.rpc.client.Req;
import org.yx.rpc.client.RpcResult;
import org.yx.rpc.codec.Request;
import org.yx.rpc.server.Response;

public class RpcLogs {

	private static RpcLogHandler handler = new PlainRpcLogHandler();

	public static RpcLogHandler getHandler() {
		return handler;
	}

	public static void setHandler(RpcLogHandler handler) {
		RpcLogs.handler = Objects.requireNonNull(handler);
	}

	public static void clientLog(Host url, Req req, RpcResult result, long receiveTime) {
		handler.clientLog(new RpcLog(url, req, result, receiveTime));
	}

	public static void serverLog(Request req, Response resp) {
		handler.serverLog(req, resp);
	}
}
