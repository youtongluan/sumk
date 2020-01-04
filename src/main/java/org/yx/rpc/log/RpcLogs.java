package org.yx.rpc.log;

import static org.yx.conf.AppInfo.LN;

import java.util.Objects;

import org.slf4j.Logger;
import org.yx.common.Host;
import org.yx.exception.BizException;
import org.yx.log.Log;
import org.yx.rpc.client.Req;
import org.yx.rpc.client.RpcResult;
import org.yx.rpc.codec.Request;
import org.yx.rpc.server.Response;
import org.yx.util.S;

public class RpcLogs {

	private static RpcLogHandler handler = new RpcLogHandler() {

		@Override
		public void clientLog(RpcLog rpcLog) {
			Logger logger = Log.get("sumk.rpc.log.client");
			if (logger.isDebugEnabled()) {
				StringBuilder sb = new StringBuilder();
				Req req = rpcLog.getReq();
				sb.append(req.getApi()).append("   server:").append(rpcLog.getServer()).append("   totalTime:")
						.append(rpcLog.getReceiveTime() - req.getStart()).append(LN);
				if (req.getJsonedParam() != null) {
					sb.append("   param(json): ").append(req.getJsonedParam());
				} else {
					sb.append("   param(array): ").append(S.json.toJson(req.getParamArray()));
				}
				RpcResult result = rpcLog.getResult();
				Exception e = null;
				if (result != null) {
					if ((e = result.exception()) == null) {
						sb.append(LN).append("   result: ").append(result.json());
					}
				}

				if (e == null) {
					logger.debug(sb.toString());
				} else {
					logger.debug(sb.toString(), e);
				}
			}
		}

		@Override
		public void serverLog(Request req, Response resp) {
			Logger logger = Log.get("sumk.rpc.log.server");
			if (logger.isDebugEnabled()) {
				StringBuilder sb = new StringBuilder();
				if (req != null) {
					sb.append("-- ").append(req.getApi()).append("   serverTime:").append(resp.serviceInvokeMilTime())
							.append(LN);
					if (req.getJsonedParam() != null) {
						sb.append("   param(json): ").append(req.getJsonedParam());
					} else {
						sb.append("   param(array): ").append(S.json.toJson(req.getParamArray()));
					}
				}
				String json = resp.json();
				Exception e = resp.exception();
				if (e == null) {
					sb.append(LN).append("   result: ").append(json);
				}

				if (resp.isSuccess()) {
					logger.debug(sb.toString());
					return;
				}
				if (BizException.class.isInstance(e)) {
					logger.warn(sb.toString(), e.toString());
				} else {
					logger.error(sb.toString(), e);
				}
			}
		}

	};

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
