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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import org.yx.conf.AppInfo;

/**
 * 这个只在系统启动的时候才能使用
 */
public final class StartContext {

	private static StartContext inst = new StartContext();

	public static StartContext inst() {
		return inst;
	}

	private StartContext() {

	}

	private final ConcurrentMap<String, Object> map = new ConcurrentHashMap<>();

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

	public <T> T get(Class<T> clz, T defaultObject) {
		String key = clz.getName();
		Object old = map.get(key);
		return clz.isInstance(old) ? clz.cast(old) : defaultObject;
	}

	public static void clear() {
		inst = new StartContext();
	}

	public static void startFailed() {
		if (AppInfo.getBoolean("sumk.exitIfStartFail", true)) {
			System.exit(1);
		}
	}

	/**
	 * 
	 * @return 剩余的启动超时时间，以ms为单位
	 */
	public static long startTimeout() {
		return SumkServer.startTime() + AppInfo.getLong("sumk.start.timeout", 1000L * 60 * 10)
				- System.currentTimeMillis();
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

	public Executor getExecutor(String name) {
		Object obj = map.get(name);
		if (obj == null || !ExecutorService.class.isInstance(obj)) {
			return SumkThreadPool.executor();
		}
		return (Executor) obj;
	}

	public String getAppInfo(String key, String defaultV) {
		Object v = map.get(key);
		if (v instanceof String) {
			return (String) v;
		}
		return AppInfo.get(key, defaultV);
	}
}
