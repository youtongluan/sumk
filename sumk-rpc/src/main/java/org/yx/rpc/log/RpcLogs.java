package org.yx.rpc.log;

import java.util.Objects;

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

	public static void clientLog(RpcLog log) {
		handler.clientLog(log);
	}

	public static void serverLog(Request req, Response resp) {
		handler.serverLog(req, resp);
	}
}
