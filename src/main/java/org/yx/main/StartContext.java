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
package org.yx.main;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import org.yx.conf.AppInfo;

/**
 * 这个只在系统启动的时候才能使用
 */
public final class StartContext {

	private static final StartContext inst = new StartContext();

	public static StartContext inst() {
		return inst;
	}

	private StartContext() {

	}

	private ConcurrentMap<String, Object> map = new ConcurrentHashMap<>();
	private static final String KEY_BEANS = "key_beans";

	public void setBeans(List<Object> beans) {
		map.put(KEY_BEANS, beans);
	}

	@SuppressWarnings("unchecked")
	public List<Object> getBeans() {
		return (List<Object>) map.get(KEY_BEANS);
	}

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

	public <T> T get(Class<T> clz, T defaultV) {
		String key = clz.getName();
		Object old = map.get(key);
		return clz.isInstance(old) ? clz.cast(old) : defaultV;
	}

	public static void clear() {
		inst.map = new ConcurrentHashMap<>();
	}

	public static void startFail() {
		if (AppInfo.getBoolean("sumk.exitIfFail", true)) {
			System.exit(1);
		}
	}

	public static String soaHostInzk() {
		return AppInfo.get("sumk.rpc.zk.host", null);
	}

	public static String soaHost() {
		return AppInfo.get("sumk.rpc.host", AppInfo.getLocalIp());
	}

	public static int soaPort() {
		return AppInfo.getInt("sumk.rpc.port", -1);
	}

	public static int soaPortInZk() {
		return AppInfo.getInt("sumk.rpc.zk.port", -1);
	}

	public static String httpHost() {
		return AppInfo.get("sumk.http.host", null);
	}

	public static int httpPort() {
		return AppInfo.getInt("sumk.http.port", -1);
	}

	public ThreadPoolExecutor getHttpExecutor() {
		Object obj = map.get("sumk.http.executor");
		if (obj == null || !ThreadPoolExecutor.class.isInstance(obj)) {
			return (ThreadPoolExecutor) SumkThreadPool.executor();
		}
		return (ThreadPoolExecutor) obj;
	}

	public ExecutorService getExecutorService(String name) {
		Object obj = map.get(name);
		if (obj == null || !ExecutorService.class.isInstance(obj)) {
			return SumkThreadPool.executor();
		}
		return (ExecutorService) obj;
	}
}
