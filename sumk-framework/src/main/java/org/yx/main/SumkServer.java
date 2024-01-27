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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import org.yx.base.context.AppContext;
import org.yx.bean.Booter;
import org.yx.bean.IOC;
import org.yx.bean.InnerIOC;
import org.yx.bean.Plugin;
import org.yx.conf.AppInfo;
import org.yx.conf.Const;
import org.yx.conf.SystemConfig;
import org.yx.conf.SystemConfigHolder;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.redis.RedisPool;
import org.yx.util.Loader;
import org.yx.util.StringUtil;
import org.yx.util.SumkThreadPool;

public final class SumkServer {

	private static boolean httpEnable;
	private static boolean rpcEnable;

	private static long startTime;

	public static long startTime() {
		return startTime;
	}

	/**
	 * 如果使用BootWatcher来加载远程配置，需要在BootWatcher里调用本方法
	 */
	public static void reloadConfig() {
		AppContext sc = AppContext.inst();
		sc.setTest(AppInfo.getBoolean("sumk.test", false));
		SumkServer.rpcEnable = sc.get(StartConstants.NOSOA) == null && soaPort() >= 0;
		SumkServer.httpEnable = sc.get(StartConstants.NOHTTP) == null && httpPort() > 0;
	}

	public static boolean isHttpEnable() {
		return httpEnable;
	}

	public static boolean isRpcEnable() {
		return rpcEnable;
	}

	public static void main(String[] args) {
		start(args == null ? Collections.emptyList() : Arrays.asList(args));
		Logs.system().info("启动完成,框架版本号:{},耗时：{}毫秒", Const.sumkVersion(), System.currentTimeMillis() - startTime);
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
			if (AppContext.inst().isStarted()) {
				return;
			}
			startTime = System.currentTimeMillis();
			AppContext.inst().setStatusToStarted();
		}
		if (args == null) {
			args = Collections.emptyList();
		}
		try {
			handleArgs(args);
			beforeStart();
			AppContext sc = AppContext.inst();
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
			AppContext.clear();
		} catch (Throwable e) {
			Log.printStack("sumk.error", e);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			}
			AppContext.startFailed();
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
				AppContext.startFailed();
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
				AppContext.inst().put(kv[0], kv[1]);
				continue;
			}
			AppContext.inst().put(arg, Boolean.TRUE);
		}

	}

	public static void stop() {
		synchronized (SumkServer.class) {
			if (AppContext.inst().isDestoryed()) {
				return;
			}
			AppContext.inst().setStatusToDestoryed();
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

	public static Executor getExecutor(String name) {
		Object obj = AppContext.inst().get(name);
		if (obj == null || !ExecutorService.class.isInstance(obj)) {
			return SumkThreadPool.executor();
		}
		return (Executor) obj;
	}

	public static ThreadPoolExecutor getHttpExecutor() {
		Object obj = AppContext.inst().get("sumk.http.executor");
		if (obj == null || !ThreadPoolExecutor.class.isInstance(obj)) {
			return (ThreadPoolExecutor) SumkThreadPool.executor();
		}
		return (ThreadPoolExecutor) obj;
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

	public static void resetStatus() {
		AppContext.inst().resetStatus();
	}
}
