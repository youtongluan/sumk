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
package org.yx.conf;

import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.yx.log.RawLog;
import org.yx.util.StringUtil;

public final class AppInfo {
	public static final String CLASSPATH_URL_PREFIX = "classpath:";
	public static final String LN = "\n";

	private static final List<Consumer<SystemConfig>> observers = new ArrayList<>();
	public static final Charset UTF8 = StandardCharsets.UTF_8;

	private static SystemConfig info;
	private static String pid;

	static {
		try {
			String temp = ManagementFactory.getRuntimeMXBean().getName();
			pid = temp.substring(0, temp.indexOf("@"));
		} catch (Exception e) {
			RawLog.error("sumk.conf", e);
			pid = "UNKNOW";
		}
		init();
	}

	private synchronized static void init() {
		if (info != null) {
			return;
		}
		try {
			if (refreshConfig()) {
				return;
			}
		} catch (Exception e) {
			RawLog.error("sumk.conf", e);
			System.exit(1);
		}
		if (info == null) {
			try {
				setConfig(new AppConfig());
			} catch (Exception e) {
				RawLog.error("sumk.conf", e);
				System.exit(1);
			}
		}
	}

	public static String pid() {
		return pid;
	}

	private static synchronized void setConfig(SystemConfig config) {
		if (info == null) {
			info = config;
		}
		config.start();
		LocalhostUtil.setLocalIp(config.get("sumk.ip"));
		info = config;
	}

	static synchronized boolean refreshConfig() {
		SystemConfig config = SystemConfigHolder.config;
		if (info == config || config == null) {
			return false;
		}
		SystemConfig old = info;
		setConfig(config);
		notifyUpdate();
		if (old != null) {
			old.stop();
		}
		return true;
	}

	public static String getServerZKUrl() {
		String url = info.get("sumk.rpc.zk.server");
		if (url != null && url.length() > 0) {
			return url;
		}
		return info.get("sumk.zkurl");
	}

	public static String getClinetZKUrl() {
		String url = info.get("sumk.rpc.zk.client");
		if (url != null && url.length() > 0) {
			return url;
		}
		return info.get("sumk.zkurl");
	}

	public static String getLocalIp() {
		return LocalhostUtil.getLocalIP();
	}

	/**
	 * @param defaultValue
	 *            如果没有设置的话，就返回这个默认值
	 * @return 当前应用的id
	 */
	public static String appId(String defaultValue) {
		return get("sumk.appId", defaultValue);
	}

	/**
	 * @param defaultValue
	 *            如果没有设置的话，就返回这个默认值
	 * @return 大系统的id
	 */
	public static String groupId(String defaultValue) {
		return get("sumk.groupId", defaultValue);
	}

	/**
	 * 
	 * 
	 * @param name
	 *            name
	 * @return name不存在的话，返回null
	 */
	public static String get(String name) {
		return info.get(name);
	}

	public static String getLatin(String name) {
		String v = info.get(name);
		return StringUtil.toLatin(v);
	}

	public static String getLatin(String name, String defaultValue) {
		String v = getLatin(name);
		return StringUtil.isEmpty(v) ? defaultValue : v;
	}

	public static String get(String name, String defaultValue) {
		String v = info.get(name);
		if (v == null || v.isEmpty()) {
			return defaultValue;
		}
		return v;
	}

	public static String get(String first, String second, String defaultValue) {
		String value = info.get(first);
		if (value != null && value.length() > 0) {
			return value;
		}
		return get(second, defaultValue);
	}

	public static int getInt(String name, int defaultValue) {
		String value = info.get(name);
		if (value == null || value.length() == 0) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static long getLong(String name, long defaultValue) {
		String value = info.get(name);
		if (value == null || value.length() == 0) {
			return defaultValue;
		}
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static Charset systemCharset() {

		String charsetName = info == null ? null : info.get("sumk.charset");
		if (StringUtil.isEmpty(charsetName) || charsetName.equalsIgnoreCase(UTF8.name())) {
			return UTF8;
		}
		if (!Charset.isSupported(charsetName)) {
			RawLog.error("sumk.conf", "charset '" + charsetName + "' is not supported");
			return UTF8;
		}
		return Charset.forName(charsetName);
	}

	public static boolean getBoolean(String name, boolean defaultValue) {
		String value = info.get(name);
		if (value == null || value.length() == 0) {
			return defaultValue;
		}
		value = value.toLowerCase();
		return "1".equals(value) || "true".equals(value);
	}

	public static Map<String, String> subMap(String prefix) {
		if (prefix == null) {
			prefix = "";
		}
		int len = prefix.length();
		Map<String, String> map = new HashMap<>();
		for (String name : info.keys()) {
			if (name.startsWith(prefix)) {
				String v = info.get(name);
				if (v != null) {
					map.put(name.substring(len), v);
				}
			}
		}
		return map;
	}

	public static void addObserver(Consumer<SystemConfig> ob) {
		synchronized (observers) {
			if (observers.contains(ob)) {
				return;
			}
			observers.add(ob);
		}
		ob.accept(info);
	}

	public static void removeObserver(Consumer<SystemConfig> ob) {
		synchronized (observers) {
			observers.remove(ob);
		}
	}

	public static synchronized void notifyUpdate() {
		List<Consumer<SystemConfig>> consumers;
		synchronized (observers) {
			consumers = new ArrayList<>(observers);
		}
		for (Consumer<SystemConfig> consumer : consumers) {
			try {
				consumer.accept(info);
			} catch (Exception e) {
				RawLog.error("sumk.conf", e);
			}
		}
	}

	public static SystemConfig config() {
		return info;
	}

}
