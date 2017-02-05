/**
 * Copyright (C) 2016 - 2017 youtongluan.
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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.yx.http.handler.HttpInfo;

public class HttpHolder {

	private static Map<String, Class<?>> pojoMap = new ConcurrentHashMap<String, Class<?>>();

	private static Map<String, HttpInfo> actMap = new ConcurrentHashMap<String, HttpInfo>();

	/**
	 * 根据方法的全名，获取参数列表
	 * 
	 * @param method
	 *            classFullName.method
	 * @return null表示方法不存在,或者参数为空
	 */
	public static Class<?> getArgType(String method) {
		String m = getArgClassName(method);
		return pojoMap.get(m);
	}

	private static String getArgClassName(String method) {
		int k = method.lastIndexOf(".");
		return method.substring(0, k) + "_" + method.substring(k + 1);
	}

	/**
	 * 根据soaName获取MethodInfo
	 * 
	 * @param method
	 * @return
	 */
	public static HttpInfo getHttpInfo(String name) {
		return actMap.get(name);
	}

	public static void putActInfo(String name, HttpInfo actInfo) {
		actMap.putIfAbsent(name, actInfo);
	}

	public static Set<String> actSet() {
		return actMap.keySet();
	}

	/**
	 * 获取所有的http接口
	 * 
	 * @return
	 */
	public static String[] acts() {
		return actMap.keySet().toArray(new String[0]);
	}
}
