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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.yx.common.Host;

import org.yx.base.context.ActionContext;
import org.yx.common.route.Router;
import org.yx.rpc.client.route.RpcRoutes;
import org.yx.rpc.server.LocalRpcContext;
import org.yx.rpc.server.RpcContext;

public final class RpcUtil {

	public static String userId() {
		return ActionContext.current().userId();
	}

	public static void setUserId(String userId) {
		ActionContext.current().userId(userId);
	}

	public static boolean setUserIdIfEmpty(String userId) {
		ActionContext tc = ActionContext.current();
		if (tc.userId() == null) {
			tc.userId(userId);
			return true;
		}
		return false;
	}

	public static Map<String, String> attachmentView() {
		return ActionContext.current().attachmentView();
	}

	public static void setAttachment(String key, String value) {
		ActionContext.current().setAttachment(key, value);
	}

	public static String getAttachment(String key) {
		return ActionContext.current().getAttachment(key);
	}

	public static void removeContext() {
		ActionContext.remove();
	}

	public static List<Host> allServers(String api) {
		Router<Host> route = RpcRoutes.getRoute(api);
		if (route == null) {
			return Collections.emptyList();
		}
		return route.allSources();
	}

	public static List<Host> aliveServers(String api) {
		Router<Host> route = RpcRoutes.getRoute(api);
		if (route == null) {
			return Collections.emptyList();
		}
		return route.aliveSources();
	}

	public static RpcContext context() {
		return LocalRpcContext.getCtx();
	}
}
