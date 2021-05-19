/**
 * Copyright (C) 2016 - 2030 youtongluan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yx.rpc.log;

import static org.yx.conf.AppInfo.LN;

import org.slf4j.Logger;
import org.yx.exception.SoaException;
import org.yx.log.Log;
import org.yx.log.LogKits;
import org.yx.rpc.RpcSettings;
import org.yx.rpc.client.Req;
import org.yx.rpc.client.RpcResult;
import org.yx.rpc.codec.ReqParamType;
import org.yx.rpc.codec.Request;
import org.yx.rpc.server.Response;
import org.yx.util.S;

public class PlainRpcLogHandler implements RpcLogHandler {

	protected boolean isLogEnable(Logger logger, long totalTime, Exception e) {
		if (e != null) {

			return logger.isErrorEnabled();
		}

		if (logger.isDebugEnabled()) {
			return true;
		}

		return (logger.isInfoEnabled() && totalTime >= RpcSettings.infoTime())
				|| (logger.isWarnEnabled() && totalTime >= RpcSettings.warnTime());
	}

	protected void appendParam(StringBuilder sb, Req req) {
		if (req.hasFeature(ReqParamType.REQ_PARAM_JSON)) {
			sb.append("   param(json): ").append(shortParam(req.getJsonedParam()));
		} else {
			sb.append("   param(array): ").append(shortParam(S.json().toJson(req.getParamArray())));
		}
	}

	protected String shortParam(String data) {
		return LogKits.shorterSubfix(data, RpcSettings.maxReqLogSize());
	}

	protected String getResult(String json) {
		return LogKits.shorterSubfix(json, RpcSettings.maxRespLogSize());
	}

	@Override
	public void clientLog(RpcLog rpcLog) {
		if (RpcSettings.isClientLogDisable() || rpcLog == null) {
			return;
		}
		Req req = rpcLog.getReq();
		if (req == null) {
			return;
		}
		String api = req.getApi();
		if (api == null || api.isEmpty() || api.startsWith("$")) {
			return;
		}
		Logger logger = Log.get("sumk.rpc.log.client");
		long totalTime = rpcLog.getReceiveTime() - req.getStart();
		RpcResult result = rpcLog.getResult();
		Exception e = result != null ? result.exception() : null;
		if (!this.isLogEnable(logger, totalTime, e)) {
			return;
		}
		StringBuilder sb = new StringBuilder(64);
		if (req.getTraceId() != null) {
			sb.append('{').append(req.getTraceId());
			if (req.getSpanId() != null) {
				sb.append('-').append(req.getSpanId());
			}
			sb.append("}  ");
		}
		sb.append(api).append("  server:").append(rpcLog.getServer()).append("  totalTime:").append(totalTime)
				.append(LN);
		this.appendParam(sb, req);
		if (result != null) {
			if (e == null) {
				sb.append(LN).append("   result: ").append(getResult(result.json()));
			}
		}

		if (e != null) {
			logger.error(sb.toString(), e);
		} else if (totalTime >= RpcSettings.warnTime()) {
			logger.warn(sb.toString());
		} else if (totalTime >= RpcSettings.infoTime()) {
			logger.info(sb.toString());
		} else {
			logger.debug(sb.toString());
		}
	}

	@Override
	public void serverLog(Request req, Response resp) {
		if (RpcSettings.isServerLogDisable()) {
			return;
		}
		Logger logger = Log.get("sumk.rpc.log.server");
		long totalTime = -1;
		SoaException e = null;
		if (resp != null) {
			totalTime = resp.serviceInvokeMilTime();
			e = resp.exception();
		}
		if (!this.isLogEnable(logger, totalTime, e)) {
			return;
		}
		StringBuilder sb = new StringBuilder(64);
		if (req != null) {
			sb.append(req.getApi()).append("   serverTime:").append(totalTime).append(LN);
			this.appendParam(sb, req);
		}
		String json = resp != null ? resp.json() : null;
		if (e == null) {
			sb.append(LN).append("   result: ").append(getResult(json));
		}

		if (resp != null && resp.isSuccess()) {
			if (totalTime >= RpcSettings.warnTime()) {
				logger.warn(sb.toString());
			} else if (totalTime > RpcSettings.infoTime()) {
				logger.info(sb.toString());
			} else {
				logger.debug(sb.toString());
			}
			return;
		}
		if (e != null && e.isBizException()) {
			logger.warn(sb.append(LN).append(e.toString()).toString());
		} else {
			logger.error(sb.toString(), e);
		}
	}

}
