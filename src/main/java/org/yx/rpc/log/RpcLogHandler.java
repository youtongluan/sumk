package org.yx.rpc.log;

import org.yx.rpc.codec.Request;
import org.yx.rpc.server.Response;

public interface RpcLogHandler {

	void clientLog(RpcLog log);

	void serverLog(Request req, Response resp);
}
