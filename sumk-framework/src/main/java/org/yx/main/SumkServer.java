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

import java.util.ArrayList;
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
import org.yx.bean.aop.asm.AsmUtils;
import org.yx.conf.AppInfo;
import org.yx.conf.Const;
import org.yx.conf.SystemConfig;
import org.yx.conf.SystemConfigHolder;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.log.RawLog;
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
		run(null, args);
	}

	public static void run(Class<?> mainClazz, String[] args) {
		start(mainClazz, args == null ? Collections.emptyList() : Arrays.asList(args));
	}

	public static void startAsTool(Class<?> mainClazz) {
		start(mainClazz, Arrays.asList(StartConstants.NOHTTP, StartConstants.NOSOA, StartConstants.NOSOA_ClIENT));
	}

	public static synchronized void start(Class<?> mainClazz, SystemConfig config, Collection<String> args) {
		if (config != null) {
			SystemConfigHolder.setSystemConfig(config);
		}
		start(mainClazz, args);
	}

	/**
	 * 启动ioc框架
	 * 
	 * @param mainClazz 类似于springboot的启动类，会扫描该类底下的类。支持为null
	 * @param args      启动参数,可为null
	 */
	public static synchronized void start(Class<?> mainClazz, Collection<String> args) {
		if (AppContext.inst().isStarted()) {
			return;
		}
		startTime = System.currentTimeMillis();
		AppContext.inst().setStatusToStarted();
		if (mainClazz != null && mainClazz.getClassLoader() != null) {
			Loader.setClassLoader(mainClazz.getClassLoader());
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
			List<String> pcks = getIocScanPath(mainClazz);
			new Booter().start(pcks);
			SumkThreadPool.scheduleThreadPoolMonitor();
			RawLog.setLogger(RawLog.SLF4J_LOG);
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
		Logs.system().info("start finished in {} ms. sumk version {}", System.currentTimeMillis() - startTime,
				Const.sumkVersion());
	}

	private static List<String> getIocScanPath(Class<?> mainClazz) {
		String ioc = AppInfo.getLatin(StartConstants.IOC_PACKAGES);
		List<String> pcks = StringUtil.isEmpty(ioc) ? Collections.emptyList()
				: StringUtil.splitAndTrim(ioc, Const.COMMA, Const.SEMICOLON);
		pcks=new ArrayList<>(pcks);
		if (mainClazz != null && mainClazz.getPackage() != null
				&& StringUtil.isNotEmpty(mainClazz.getPackage().getName())) {
			String pkName = mainClazz.getPackage().getName();
			for (String defined : pcks) {
				if (defined.startsWith(pkName + ".")) {
					return pcks;
				}
			}
			pcks.add(pkName);
		}
		return pcks;
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

	public static boolean stop() {
		synchronized (SumkServer.class) {
			if (AppContext.inst().isDestoryed()) {
				return false;
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
		AsmUtils.clearProxyClassLoaders();
		InnerIOC.clear();
		Logs.system().info("sumk server stoped!!!");
		return true;
	}

	public static void destroy() {
		if (!stop()) {
			return;
		}
		SumkThreadPool.shutdown();
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

	public static String soaHost() {
		return AppInfo.get("sumk.rpc.host", AppInfo.getLocalIp());
	}

	public static int soaPort() {
		return AppInfo.getInt("sumk.rpc.port", -1);
	}

	public static String httpHost() {
		return AppInfo.get("sumk.http.host", null);
	}

	public static int httpPort() {
		return AppInfo.getInt("sumk.http.port", -1);
	}

	/**
	 * 重置状态，一般用于单元测试
	 */
	public static void resetStatus() {
		AppContext.inst().resetStatus();
	}
}
