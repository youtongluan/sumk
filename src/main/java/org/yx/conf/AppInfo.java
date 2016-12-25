package org.yx.conf;

import java.io.InputStream;

import org.yx.db.sql.ColumnType;
import org.yx.log.ConsoleLog;
import org.yx.log.Log;
import org.yx.log.LogType;
import org.yx.rpc.LocalhostUtil;

public class AppInfo {
	public static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
	public static final String CLASSPATH_URL_PREFIX = "classpath:";
	private static String appId = "UNKOWN";

	public static int httpSessionTimeout = 3600;

	public static int redisTTL = 7200;
	/**
	 * 默认情况下，DB操作是根据数据库中的主键，还是redis中的主键。
	 */
	public static ColumnType modifyByColumnType = ColumnType.ID_DB;

	public static final PropertiesInfo info = new PropertiesInfo("app.properties") {

		private Integer intValue(String key) {
			String temp = get(key);
			if (temp != null) {
				try {
					return Integer.valueOf(temp);
				} catch (Exception e) {
					Log.get(AppInfo.class).error(key + "=" + temp + "，不是有效的数字格式");
				}
			}
			return null;
		}

		@Override
		public void deal(InputStream in) throws Exception {
			super.deal(in);
			String id = get("sumk.appId");
			if (id != null) {
				AppInfo.appId = id;
			}

			Integer temp = intValue("http.session.timeout");
			if (temp != null) {
				AppInfo.httpSessionTimeout = temp;
			}

			temp = intValue("sumk.redis.ttl");
			if (temp != null) {
				AppInfo.redisTTL = temp;
			}
			String modify = get("sumk.db.default_modify");
			if (modify != null && modify.length() > 0
					&& ("redis".equalsIgnoreCase(modify) || "cache".equalsIgnoreCase(modify))) {
				AppInfo.modifyByColumnType = ColumnType.ID_CACHE;
			}
			try {
				String logType = get("sumk.logtype", LogType.console.name());
				Log.setLogType(LogType.valueOf(logType));
				temp = intValue("sumk.loglevel.console");
				if (temp != null && temp.byteValue() > -1) {
					ConsoleLog.setDefaultLevel(temp.byteValue());
				}
			} catch (Exception e) {
				Log.get("sumk.appInfo").info(e.getMessage(), e);
			}
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

	public static String getAppId() {
		return appId;
	}

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
			return Integer.parseInt(name);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static String systemCharset() {
		return System.getProperty("sumk.app.charset", "UTF-8");
	}

}
