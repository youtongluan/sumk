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
package org.yx.conf;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import org.yx.db.annotation.ColumnType;
import org.yx.log.Log;
import org.yx.rpc.LocalhostUtil;
import org.yx.util.StringUtils;

public class AppInfo {
	public static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
	public static final String CLASSPATH_URL_PREFIX = "classpath:";

	private static String groupId = "sumk";
	private static String appId = "demo";

	public static int httpSessionTimeout = 3600;

	/**
	 * 默认情况下，DB操作是根据数据库中的主键，还是redis中的主键。
	 */
	public static ColumnType modifyByColumnType = ColumnType.ID_DB;

	private static List<Observer> observers = new ArrayList<>(4);
	private static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	static NamePairs zkInfo = null;

	public static synchronized void addObserver(Observer ob) {
		if (observers.contains(ob)) {
			return;
		}
		observers.add(ob);
		ob.update(null, null);
	}

	static final PropertiesInfo info = new PropertiesInfo("app.properties") {

		private Integer intValue(String key) {
			String temp = get(key);
			if (temp != null) {
				try {
					return Integer.valueOf(temp);
				} catch (Exception e) {
					Log.get(AppInfo.class).error(key + "=" + temp + ", is not valid Integer,ignore it");
				}
			}
			return null;
		}

		@Override
		public void deal(InputStream in) throws Exception {
			super.deal(in);
			String id = get("sumk.appId");
			if (id != null && id.length() > 0) {
				AppInfo.appId = id;
			}
			id = get("sumk.groupId");
			if (id != null && id.length() > 0) {
				AppInfo.groupId = id;
			}

			Integer temp = intValue("http.session.timeout");
			if (temp != null) {
				AppInfo.httpSessionTimeout = temp;
			}

			observers.forEach(ob -> {
				ob.update(null, null);
			});
		}

		@Override
		public String get(String key) {
			String ret = super.get(key);
			if (ret == null && zkInfo != null) {
				return zkInfo.getValue(key);
			}
			return ret;
		}

		public String get(String key, String defaultValue) {
			String value = pro.get(key);
			if (value != null && value.length() > 0) {
				return value;
			}
			if (zkInfo != null) {
				value = zkInfo.getValue(key);
			}
			if (value != null && value.length() > 0) {
				return value;
			}
			return defaultValue;
		}

	};

	public static String getZKUrl() {
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
	 * 当前应用的id
	 * 
	 * @return
	 */
	public static String appId() {
		return appId;
	}

	/**
	 * 大系统的id
	 * 
	 * @return
	 */
	public static String groupId() {
		return groupId;
	}

	/**
	 * key不存在的话，返回null
	 * 
	 * @param name
	 * @return
	 */
	public static String get(String name) {
		return info.get(name);
	}

	public static String get(String name, String defaultValue) {
		return info.get(name, defaultValue);
	}

	/**
	 * 这个方法不会抛出异常
	 * 
	 * @param name
	 * @param defaultValue
	 * @return
	 */
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
		if (StringUtils.isEmpty(charsetName) || charsetName.equalsIgnoreCase(DEFAULT_CHARSET.name())) {
			return DEFAULT_CHARSET;
		}
		if (!Charset.isSupported(charsetName)) {
			Log.get("sumk.SYS").error("charset '{}' is not supported", charsetName);
			return DEFAULT_CHARSET;
		}
		return Charset.forName(charsetName);
	}

	/**
	 * name不存在或空字节的时候，返回defaultValue<br>
	 * 否则，值为1或true的时候（大小写不敏感），返回true
	 * 
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public static boolean getBoolean(String name, boolean defaultValue) {
		String value = info.get(name);
		if (value == null || value.length() == 0) {
			return defaultValue;
		}
		value = value.toLowerCase();
		return "1".equals(value) || "true".equals(value);
	}

}
