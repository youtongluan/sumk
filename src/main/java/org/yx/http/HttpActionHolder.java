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
package org.yx.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.yx.annotation.http.Web;
import org.yx.common.ActInfoUtil;
import org.yx.http.handler.HttpActionNode;
import org.yx.main.SumkServer;

/**
 * 仅供框架使用
 */
public class HttpActionHolder {

	private static Map<String, Class<?>> pojoMap = new ConcurrentHashMap<>();

	private static Map<String, HttpActionNode> actMap = new ConcurrentHashMap<>();

	public static Class<?> getArgType(String method) {
		String m = getArgClassName(method);
		return pojoMap.get(m);
	}

	private static String getArgClassName(String method) {
		int k = method.lastIndexOf(".");
		return method.substring(0, k) + "_" + method.substring(k + 1);
	}

	public static HttpActionNode getHttpInfo(String name) {
		return actMap.get(name);
	}

	public static void putActInfo(String name, HttpActionNode actInfo) {
		actMap.putIfAbsent(name, actInfo);
	}

	public static Set<String> actSet() {
		return actMap.keySet();
	}

	public static String[] acts() {
		return actMap.keySet().toArray(new String[0]);
	}

	public static List<Map<String, Object>> infos() {
		if (!SumkServer.isHttpEnable()) {
			return Collections.emptyList();
		}
		List<Map<String, Object>> ret = new ArrayList<>(HttpActionHolder.actMap.size());
		HttpActionHolder.actMap.forEach((name, http) -> {
			Map<String, Object> map = ActInfoUtil.infoMap(name, http);
			ret.add(map);
			if (http.action != null) {
				Web web = http.action;
				map.put("requireLogin", web.requireLogin());
				map.put("requestEncrypt", web.requestEncrypt());
				map.put("responseEncrypt", web.responseEncrypt());
				map.put("sign", web.sign());
				map.put("description", web.description());
			}
			map.put("upload", http.upload != null);
		});
		return ret;
	}
}
