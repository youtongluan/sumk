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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yx.bean.BeanPublisher;
import org.yx.bean.IOC;
import org.yx.bean.InnerIOC;
import org.yx.bean.Plugin;
import org.yx.bean.Scaners;
import org.yx.common.StartConstants;
import org.yx.common.StartContext;
import org.yx.conf.AppInfo;
import org.yx.conf.SystemConfig;
import org.yx.conf.SystemConfigHolder;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.redis.RedisPool;
import org.yx.util.StringUtil;

public final class SumkServer {
	private static volatile boolean started;
	private static volatile boolean destoryed;
	private static volatile boolean httpEnable;
	private static volatile boolean rpcEnable;
	private static long startTime;

	public static long startTime() {
		return startTime;
	}

	public static void resetStatus() {
		started = false;
	}

	public static boolean isHttpEnable() {
		return httpEnable;
	}

	public static boolean isRpcEnable() {
		return rpcEnable;
	}

	public static void main(String[] args) {
		long begin = System.currentTimeMillis();
		start(args);
		Logs.system().info("启动完成,耗时：{}毫秒", System.currentTimeMillis() - begin);
	}

	public static void start() {
		start(Collections.emptyList());
	}

	public static void startAsTool() {
		start(StartConstants.NOHTTP, StartConstants.NOSOA, StartConstants.NOSOA_ClIENT);
	}

	public static void start(String... args) {
		Set<String> argSet = new HashSet<>();
		if (args != null && args.length > 0) {
			Collections.addAll(argSet, args);
		}
		start(argSet);
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
			started = true;
			destoryed = false;
			startTime = System.currentTimeMillis();
		}
		if (args == null) {
			args = Collections.emptyList();
		}
		try {
			handleSystemArgs();
			handleArgs(args);

			if (StartContext.inst().get(StartConstants.THREAD_ON_DEAMON) != null) {
				SumkThreadPool.setDaemon(true);
			}
			BeanPublisher publisher = new BeanPublisher();
			publisher.setListeners(Scaners.supplier().get());
			List<String> ps = new ArrayList<>();
			ps.add(AppInfo.get(StartConstants.IOC_PACKAGES));
			ps.add(AppInfo.get(StartConstants.INNER_PACKAGE));
			if (StartContext.inst().get(StartConstants.NOSOA) == null && StartContext.soaPort() >= 0) {
				rpcEnable = true;
			}
			if (StartContext.inst().get(StartConstants.NOHTTP) == null && StartContext.httpPort() > 0) {
				httpEnable = true;
			}
			publisher.publishBeans(allPackage(ps));
			SumkThreadPool.scheduleThreadPoolMonitor();
			StartContext.clear();
		} catch (Throwable e) {
			Log.printStack("sumk.error", e);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			}
			System.exit(1);
		}
	}

	private static void handleSystemArgs() {
	}

	private static void handleArgs(Collection<String> args) {
		if (args == null) {
			return;
		}
		args.forEach(arg -> {
			if (arg.contains("=")) {
				String[] kv = arg.split("=", 2);
				StartContext.inst().put(kv[0], kv[1]);
				return;
			}
			StartContext.inst().put(arg, Boolean.TRUE);
		});

	}

	private static List<String> allPackage(List<String> ps) {
		List<String> list = new ArrayList<>();
		for (String p : ps) {
			if (StringUtil.isEmpty(p)) {
				continue;
			}
			p = p.replace('，', ',');
			String[] ss = p.split(",");
			for (String s : ss) {
				s = s.trim();
				if (s.isEmpty()) {
					continue;
				}
				list.add(s);
			}
		}
		return list;
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
			lifes.forEach(b -> {
				try {
					b.stop();
				} catch (Exception e) {
					Log.printStack("sumk.error", e);
				}
			});
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
