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
package org.yx.base.context;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.yx.conf.AppInfo;

/**
 * 这个只在系统启动的时候才能使用
 */
public final class AppContext {

	private static AppContext inst = new AppContext();
	private volatile boolean started;
	private volatile boolean destoryed;
	private boolean test;

	public static AppContext inst() {
		return inst;
	}

	private AppContext() {

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
		inst = new AppContext();
	}

	public static void startFailed() {
		if (AppInfo.getBoolean("sumk.exitIfStartFail", true)) {
			System.exit(1);
		}
	}

	public String getAppInfo(String key, String defaultV) {
		Object v = map.get(key);
		if (v instanceof String) {
			return (String) v;
		}
		return AppInfo.get(key, defaultV);
	}

	public boolean isStarted() {
		return started;
	}

	public boolean isDestoryed() {
		return destoryed;
	}

	public void resetStatus() {
		started = false;
		destoryed = false;
	}

	public void setStatusToStarted() {
		started = true;
		destoryed = false;
	}

	public void setStatusToDestoryed() {
		started = false;
		destoryed = true;
	}

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

}
