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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.yx.bean.Booter;
import org.yx.bean.IOC;
import org.yx.bean.InnerIOC;
import org.yx.bean.Loader;
import org.yx.bean.Plugin;
import org.yx.conf.AppInfo;
import org.yx.conf.Const;
import org.yx.conf.SystemConfig;
import org.yx.conf.SystemConfigHolder;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.redis.RedisPool;
import org.yx.util.StringUtil;

public final class SumkServer {
	private static volatile boolean started;
	private static volatile boolean destoryed;
	private static boolean httpEnable;
	private static boolean rpcEnable;

	private static boolean test;
	private static long startTime;

	public static long startTime() {
		return startTime;
	}

	public static void resetStatus() {
		started = false;
		destoryed = false;
	}

	/**
	 * 如果使用BootWatcher来加载远程配置，需要在BootWatcher里调用本方法
	 */
	public static void reloadConfig() {
		StartContext sc = StartContext.inst();
		SumkServer.test = AppInfo.getBoolean("sumk.test", false);
		if (sc.get(StartConstants.NOSOA) == null && StartContext.soaPort() >= 0) {
			rpcEnable = true;
		}
		if (sc.get(StartConstants.NOHTTP) == null && StartContext.httpPort() > 0) {
			httpEnable = true;
		}
	}

	public static boolean isHttpEnable() {
		return httpEnable;
	}

	public static boolean isRpcEnable() {
		return rpcEnable;
	}

	public static void main(String[] args) {
		start(args == null ? Collections.emptyList() : Arrays.asList(args));
		Logs.system().info("启动完成,耗时：{}毫秒", System.currentTimeMillis() - startTime);
	}

	public static void main(Class<?> startClass, String[] args) {
		Loader.setClassLoader(startClass.getClassLoader());
		main(args);
	}

	public static void start(String... args) {
		start(args == null ? Collections.emptyList() : Arrays.asList(args));
	}

	public static void start(Class<?> startClass, Collection<String> args) {
		Loader.setClassLoader(startClass.getClassLoader());
		start(args);
	}

	public static void startAsTool() {
		start(Arrays.asList(StartConstants.NOHTTP, StartConstants.NOSOA, StartConstants.NOSOA_ClIENT));
	}

	public static void start(SystemConfig config, Collection<String> args) {
		SystemConfigHolder.setSystemConfig(config);
		start(args == null ? Collections.emptyList() : args);
	}

	public static void start(Collection<String> args) {
		synchronized (SumkServer.class) {
			if (started) {
				return;
			}
			startTime = System.currentTimeMillis();
			started = true;
			destoryed = false;
		}
		if (args == null) {
			args = Collections.emptyList();
		}
		try {
			handleArgs(args);
			beforeStart();
			StartContext sc = StartContext.inst();
			if (Logs.system().isDebugEnabled()) {
				Logs.system().debug("start contexts:{}", sc);
			}

			if (sc.get(StartConstants.THREAD_ON_DEAMON) != null) {
				SumkThreadPool.setDaemon(true);
			}
			reloadConfig();
			String ioc = AppInfo.getLatin(StartConstants.IOC_PACKAGES);
			List<String> pcks = StringUtil.isEmpty(ioc) ? Collections.emptyList()
					: StringUtil.splitAndTrim(ioc, Const.COMMA, Const.SEMICOLON);
			new Booter().start(pcks);
			SumkThreadPool.scheduleThreadPoolMonitor();
			StartContext.clear();
		} catch (Throwable e) {
			Log.printStack("sumk.error", e);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			}
			StartContext.startFailed();
		}
	}

	/**
	 * 有时候希望在start之前做一些装配工作，这个方法就是用于完成这个目的。<BR>
	 * 通过sumk.start.before.class.xx=类名（必须实现Runnable接口）来配置
	 */
	private static void beforeStart() {
		String prefix = "sumk.start.before.class.";
		Map<String, String> map = AppInfo.subMap(prefix);
		if (map == null || map.isEmpty()) {
			return;
		}
		map = new TreeMap<>(map);
		for (String key : map.keySet()) {
			String clzName = map.get(key);
			if (StringUtil.isEmpty(clzName)) {
				continue;
			}
			try {
				clzName = StringUtil.toLatin(clzName).trim();
				Object obj = Loader.newInstance(clzName);
				if (!(obj instanceof Runnable)) {
					Logs.ioc().warn("{}的处理类{}不是Runnable类型，被自动过滤掉", prefix + key, clzName);
					continue;
				}
				((Runnable) obj).run();
			} catch (Exception e) {
				Logs.ioc().error(e.getLocalizedMessage(), e);
				StartContext.startFailed();
			}
		}
	}

	private static void handleArgs(Collection<String> args) {
		if (args == null) {
			return;
		}
		for (String arg : args) {
			if (arg.contains("=")) {
				String[] kv = arg.split("=", 2);
				StartContext.inst().put(kv[0], kv[1]);
				continue;
			}
			StartContext.inst().put(arg, Boolean.TRUE);
		}

	}

	public static boolean isTest() {
		return test;
	}

	public static boolean isStarted() {
		return started;
	}

	public static boolean isDestoryed() {
		return destoryed;
	}

	public static void stop() {
		synchronized (SumkServer.class) {
			if (destoryed) {
				return;
			}
			destoryed = true;
			started = false;
		}
		Logs.system().warn("sumk server stoping...");
		List<Plugin> lifes = IOC.getBeans(Plugin.class);
		if (lifes != null && lifes.size() > 0) {
			Collections.reverse(lifes);
			for (Plugin b : lifes) {
				try {
					b.stop();
				} catch (Exception e) {
					Log.printStack("sumk.error", e);
				}
			}
		}
		try {
			RedisPool.shutdown();
		} catch (Throwable e2) {
		}
		InnerIOC.clear();
		SumkThreadPool.shutdown();
		Logs.system().info("sumk server stoped!!!");
	}
}
