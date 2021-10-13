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
package org.yx.log;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

import org.yx.conf.AppInfo;
import org.yx.conf.Const;
import org.yx.conf.SystemConfig;
import org.yx.util.StringUtil;

public final class Loggers {
	private static final String ROOT = "";
	private static LogLevel DEFAULT_LEVEL = LogLevel.INFO;

	private static ConcurrentMap<String, LogLevel> _levelMap = new ConcurrentHashMap<>();

	private ConcurrentMap<String, SumkLogger> map = new ConcurrentHashMap<>();
	private final String name;

	private Loggers(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Loggers [name=" + name + "]";
	}

	private static final Consumer<SystemConfig> observer = info -> {
		try {
			Map<String, LogLevel> newLevels = new HashMap<>();
			String temp = AppInfo.getLatin("sumk.log.level", null);
			if (temp == null) {
				Loggers.resetLevel(newLevels);
				return;
			}
			List<String> levelStrs = StringUtil.splitAndTrim(temp, Const.COMMA, Const.SEMICOLON, "\r", Const.LN);
			for (String levelStr : levelStrs) {
				String[] levelNamePair = levelStr.split(":");
				switch (levelNamePair.length) {
				case 1:
					newLevels.put(ROOT, LogLevel.valueOf(levelNamePair[0].trim().toUpperCase()));
					break;
				case 2:
					newLevels.put(levelNamePair[0].trim(), LogLevel.valueOf(levelNamePair[1].trim().toUpperCase()));
					break;
				default:
					System.err.println(levelStr + " is not valid name:level format");
				}
			}
			Loggers.resetLevel(newLevels);
		} catch (Exception e) {
			e.printStackTrace();
		}
	};
	static {
		AppInfo.addObserver(observer);
	}

	public static synchronized Loggers create(String name) {
		System.out.println("create loggers " + name);
		return new Loggers(name);
	}

	private static LogLevel getLevel(final String fullName) {
		ConcurrentMap<String, LogLevel> cache = _levelMap;
		if (cache.isEmpty()) {
			return DEFAULT_LEVEL;
		}
		LogLevel level = cache.get(fullName);
		if (level != null) {
			return level;
		}
		int index = 0;
		String logName = fullName;
		do {
			index = logName.lastIndexOf('.');
			if (index <= 0) {
				break;
			}
			logName = logName.substring(0, index);
			level = cache.get(logName);
			if (level != null) {
				cache.put(fullName, level);
				return level;
			}
		} while (logName.length() > 0);
		cache.put(fullName, DEFAULT_LEVEL);
		return DEFAULT_LEVEL;
	}

	public static LogLevel getLevel(SumkLogger log) {
		String name = log.getName();
		if (name == null || name.isEmpty()) {
			return DEFAULT_LEVEL;
		}
		return getLevel(name);
	}

	public synchronized static void resetLevel(Map<String, LogLevel> newLevelMap) {
		ConcurrentMap<String, LogLevel> newLevels = new ConcurrentHashMap<>(newLevelMap);
		LogLevel defaultLevel = newLevels.remove(ROOT);
		DEFAULT_LEVEL = defaultLevel == null ? LogLevel.INFO : defaultLevel;
		_levelMap = newLevels;
	}

	public static void setDefaultLevel(LogLevel level) {
		if (level != null) {
			DEFAULT_LEVEL = level;
		}
	}

	public static Map<String, LogLevel> currentLevels() {
		return Collections.unmodifiableMap(_levelMap);
	}

	public SumkLogger get(String name) {
		return map.get(name);
	}

	public SumkLogger putIfAbsent(String name, SumkLogger log) {
		return map.putIfAbsent(name, log);
	}

}
