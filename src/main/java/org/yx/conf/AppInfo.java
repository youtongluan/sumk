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

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;

import org.yx.bean.Loader;
import org.yx.log.Log;
import org.yx.rpc.LocalhostUtil;
import org.yx.util.StringUtil;

public final class AppInfo {
	public static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
	public static final String CLASSPATH_URL_PREFIX = "classpath:";

	static final List<Observer> observers = new ArrayList<>(4);
	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	private static SystemConfig info;
	static {
		try {
			Class<?> clz = Loader.loadClass("org.yx.conf.SystemConfigImpl");
			Object obj = clz.newInstance();
			if (SystemConfig.class.isInstance(obj)) {
				Log.get("sumk.SYS").debug("use SystemConfigImpl for appInfo");
				info = (SystemConfig) obj;
			}
		} catch (Throwable e) {
		}
		try {
			if (info == null) {
				info = new AppPropertiesInfo();
			}
			info.initAppInfo();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static synchronized void addObserver(Observer ob) {
		if (observers.contains(ob)) {
			return;
		}
		observers.add(ob);
		ob.update(null, null);
	}

	public static String getServerZKUrl() {
		String url = info.get("soa.zk.server");
		if (url != null && url.length() > 0) {
			return url;
		}
		return info.get("sumk.zkurl");
	}

	public static String getClinetZKUrl() {
		String url = info.get("soa.zk.client");
		if (url != null && url.length() > 0) {
			return url;
		}
		return info.get("sumk.zkurl");
	}

	public static String getIp() {
		String ip = info.get("sumk.ip");
		if (ip != null) {
			return ip;
		}
		try {
			return LocalhostUtil.getLocalIP();
		} catch (Exception e) {
			Log.printStack(e);
		}
		return "0.0.0.0";
	}

	/**
	 * 
	 * @return 当前应用的id
	 */
	public static String appId() {
		return info.get("sumk.appId", "app");
	}

	/**
	 * 
	 * @return 大系统的id
	 */
	public static String groupId() {
		return info.get("sumk.groupId", "sumk");
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

	public static String get(String name, String defaultValue) {
		return info.get(name, defaultValue);
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

	public static Charset systemCharset() {

		String charsetName = info == null ? null : info.get("sumk.charset");
		if (StringUtil.isEmpty(charsetName) || charsetName.equalsIgnoreCase(DEFAULT_CHARSET.name())) {
			return DEFAULT_CHARSET;
		}
		if (!Charset.isSupported(charsetName)) {
			Log.get("sumk.SYS").error("charset '{}' is not supported", charsetName);
			return DEFAULT_CHARSET;
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
		int len = prefix.length();
		Map<String, String> map = new HashMap<>();
		for (String name : info.keys()) {
			if (name.startsWith(prefix)) {
				map.put(name.substring(len), info.get(name));
			}
		}
		return map;
	}

	private static class AppPropertiesInfo extends PropertiesInfo {

		AppPropertiesInfo() {
			super("app.properties");
		}

		@Override
		public void deal(InputStream in) throws Exception {
			super.deal(in);

			AppInfo.observers.forEach(ob -> {
				ob.update(null, null);
			});
		}

		@Override
		public void initAppInfo() {
			this.start();
		}

	}

}
