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
package org.yx.rpc;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.yx.common.context.ActionContext;
import org.yx.conf.AppInfo;
import org.yx.conf.SimpleBeanUtil;
import org.yx.log.Logs;
import org.yx.rpc.client.Req;
import org.yx.util.StringUtil;

public final class InnerRpcUtil {

	public static ActionContext rpcContext(Req req, boolean isTest) {
		String traceId = StringUtil.isEmpty(req.getTraceId()) ? null : req.getTraceId();
		return ActionContext.newContext(req.getApi(), traceId, req.getSpanId(), req.getUserId(), isTest,
				req.getAttachments());
	}

	public static String parseRpcIntfPrefix(Class<?> intfClz) {

		return parseClassName2Prefix(intfClz.getName(), AppInfo.getInt("sumk.rpc.intf.name.partcount", 3));
	}

	public static String parseClassName2Prefix(String name, int partCount) {
		String[] names = name.split("\\.");
		if (names.length <= partCount) {
			return name + ".";
		}
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = names.length - partCount; i < names.length; i++) {
			sb.append(StringUtil.uncapitalize(names[i])).append('.');
		}
		return sb.toString();
	}

	public static void config(SocketSessionConfig conf, boolean server) {

		conf.setIdleTime(IdleStatus.BOTH_IDLE, AppInfo.getInt("sumk.rpc.session.idle", 60));
		Map<String, String> map = new HashMap<>(AppInfo.subMap("sumk.rpc.conf."));
		String selfKey = server ? "sumk.rpc.server.conf." : "sumk.rpc.client.conf.";
		map.putAll(AppInfo.subMap(selfKey));
		if (map.isEmpty()) {
			return;
		}
		String flag = server ? "server" : "client";
		Logs.rpc().info(flag + " session config: {}", map);
		try {
			SimpleBeanUtil.copyProperties(conf, map);
		} catch (Exception e) {
			Logs.rpc().warn(flag + " rpc config error. " + e.getMessage(), e);
		}
	}
}
