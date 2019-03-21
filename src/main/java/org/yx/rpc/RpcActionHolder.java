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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.yx.annotation.rpc.Soa;
import org.yx.common.ActInfoUtil;
import org.yx.main.SumkServer;

public class RpcActionHolder {

	private static Map<String, Class<?>> pojoMap = new ConcurrentHashMap<>();

	private static Map<String, RpcActionNode> actMap = new ConcurrentHashMap<>();

	public static Class<?> getArgType(String method) {
		String m = getArgClassName(method);
		return pojoMap.get(m);
	}

	private static String getArgClassName(String method) {
		int k = method.lastIndexOf(".");
		return method.substring(0, k) + "_" + method.substring(k + 1);
	}

	public static RpcActionNode getActionNode(String soaName) {
		return actMap.get(soaName);
	}

	public static void putActNode(String soaName, RpcActionNode actInfo) {
		actMap.putIfAbsent(soaName, actInfo);
	}

	public static Set<String> soaSet() {
		return actMap.keySet();
	}

	public static Collection<String> publishSoaSet() {
		List<String> list = new ArrayList<>(actMap.size());
		actMap.forEach((method, v) -> {
			if (v.action.publish()) {
				list.add(method);
			}
		});
		return list;
	}

	public static List<Map<String, Object>> infos() {
		if (!SumkServer.isRpcEnable()) {
			return Collections.emptyList();
		}
		List<Map<String, Object>> ret = new ArrayList<>(actMap.size());
		actMap.forEach((name, rpc) -> {
			Map<String, Object> map = ActInfoUtil.infoMap(name, rpc);
			;
			ret.add(map);
			if (rpc.action != null) {
				Soa soa = rpc.action;
				map.put("description", soa.description());
			}
		});
		return ret;
	}

}
