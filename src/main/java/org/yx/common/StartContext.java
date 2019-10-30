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
package org.yx.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.yx.conf.AppInfo;
import org.yx.util.StringUtil;

public class StartContext {

	private static StartContext inst = new StartContext();

	public static StartContext inst() {
		return inst;
	}

	private Map<String, Object> map = new ConcurrentHashMap<>();

	public void put(String key, Object obj) {
		map.put(key, obj);
	}

	public void put(Class<?> clz, Object obj) {
		map.put(clz.getName(), obj);
	}

	public Object get(String key) {
		return map.get(key);
	}

	public Object get(Class<?> clz) {
		return map.get(clz.getName());
	}

	@SuppressWarnings("unchecked")
	public <T> T getOrCreate(String key, T t) {
		if (map.containsKey(key)) {
			return (T) map.get(key);
		}
		map.put(key, t);
		return t;
	}

	public <T> T getOrCreate(Class<?> clz, T t) {
		return this.getOrCreate(clz.getName(), t);
	}

	public static void clear() {
		inst.map = new ConcurrentHashMap<>();
	}

	public static void startFail() {
		if (AppInfo.getBoolean("sumk.exitIfFail", true)) {
			System.exit(-1);
		}
	}

	public static String soaHostInzk() {
		return get("sumk.rpc.zk.host", null);
	}

	public static String soaHost() {
		return get("sumk.rpc.host", AppInfo.getIp());
	}

	public static int soaPort() {
		return getInt("sumk.rpc.port", -1);
	}

	public static int soaPortInZk() {
		return getInt("sumk.rpc.zk.port", -1);
	}

	public static String httpHost() {
		return get("sumk.http.host", AppInfo.get("sumk.http.host"));
	}

	public static int httpPort() {
		return getInt("sumk.http.port", -1);
	}

	private static int getInt(String name, int defaultValue) {
		Integer p = Integer.getInteger(name);
		if (p != null) {
			return p.intValue();
		}
		return AppInfo.getInt(name, defaultValue);
	}

	private static String get(String name, String defaultValue) {
		String v = System.getProperty(name);
		if (StringUtil.isNotEmpty(v)) {
			return v;
		}
		return AppInfo.get(name, defaultValue);
	}

}
