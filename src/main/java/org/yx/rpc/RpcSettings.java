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

import org.yx.conf.AppInfo;

public final class RpcSettings {
	private static boolean started;
	private static boolean serverLogDisable;
	private static boolean clientLogDisable;
	private static long infoTime;
	private static long warnTime;
	private static int clientDefaultTimeout;
	private static boolean disableLocalRoute;
	private static int clientTryCount;
	private static boolean showServerExceptionLog;
	private static int maxReqLogSize;
	private static int maxRespLogSize;

	public static int maxReqLogSize() {
		return maxReqLogSize;
	}

	public static int maxRespLogSize() {
		return maxRespLogSize;
	}

	public static boolean showServerExceptionLog() {
		return showServerExceptionLog;
	}

	public static boolean disableLocalRoute() {
		return disableLocalRoute;
	}

	public static int clientDefaultTimeout() {
		return clientDefaultTimeout;
	}

	public static long infoTime() {
		return infoTime;
	}

	public static long warnTime() {
		return warnTime;
	}

	public static boolean isServerLogDisable() {
		return serverLogDisable;
	}

	public static int clientTryCount() {
		return clientTryCount;
	}

	public static boolean isClientLogDisable() {
		return clientLogDisable;
	}

	public static long maxServerIdleTime() {
		return AppInfo.getLong("sumk.rpc.server.idle", 1000L * 60 * 10);
	}

	public static long maxClientIdleTime() {
		return AppInfo.getLong("sumk.rpc.client.idle", 1000L * 60 * 5);
	}

	public synchronized static void init() {
		if (started) {
			return;
		}
		started = true;
		AppInfo.addObserver(info -> {
			RpcSettings.warnTime = AppInfo.getInt("sumk.rpc.log.warn.time", 3000);
			RpcSettings.infoTime = AppInfo.getInt("sumk.rpc.log.info.time", 1000);
			RpcSettings.serverLogDisable = AppInfo.getBoolean("sumk.rpc.log.server.disable", false);
			RpcSettings.clientLogDisable = AppInfo.getBoolean("sumk.rpc.log.client.disable", false);
			RpcSettings.clientDefaultTimeout = AppInfo.getInt("sumk.rpc.call.timeout", 30000);
			RpcSettings.clientTryCount = AppInfo.getInt("sumk.rpc.client.trycount", 3);
			RpcSettings.disableLocalRoute = AppInfo.getBoolean("sumk.rpc.localroute.disable", false);
			RpcSettings.showServerExceptionLog = AppInfo.getBoolean("sumk.rpc.server.exceptionlog", false);
			RpcSettings.maxReqLogSize = AppInfo.getInt("sumk.rpc.log.reqsize", 1000);
			RpcSettings.maxRespLogSize = AppInfo.getInt("sumk.rpc.log.respsize", 5000);
		});
	}
}
