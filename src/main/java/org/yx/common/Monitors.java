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
package org.yx.common;

import static org.yx.conf.AppInfo.LN;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.yx.bean.InnerIOC;
import org.yx.conf.AppInfo;
import org.yx.log.LogLevel;
import org.yx.log.Loggers;
import org.yx.main.SumkServer;
import org.yx.util.SumkDate;

public class Monitors {

	private static final String BLACK = "  ";

	public static String serverInfo() {
		long startTime = SumkServer.startTime();
		long now = System.currentTimeMillis();
		long ms = now - startTime;
		StringBuilder sb = new StringBuilder();
		sb.append("start").append(BLACK).append(SumkDate.of(startTime).to_yyyy_MM_dd_HH_mm_ss_SSS().replace(" ", "T"))
				.append(BLACK).append("run(ms)").append(BLACK).append(ms).append(LN).append("localip").append(BLACK)
				.append(AppInfo.getLocalIp()).append(BLACK).append("pid").append(BLACK).append(AppInfo.pid())
				.append(LN);
		String v = AppInfo.groupId(null);
		if (v != null) {
			sb.append("groupId").append(BLACK).append(v).append(BLACK);
		}
		v = AppInfo.appId(null);
		if (v != null) {
			sb.append("appId").append(BLACK).append(v);
		}
		return sb.toString();
	}

	public static String systemInfo() {
		Map<Object, Object> map = new HashMap<>(System.getProperties());
		StringBuilder sb = new StringBuilder();
		map.forEach((k, v) -> {
			if (v != null) {
				v = v.toString().replace("\r", "\\r").replace("\n", "\\n");
			}
			sb.append(k).append(" : ").append(v).append(LN);
		});
		return sb.toString();
	}

	public static String jvmInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("## name   init   max   commited    used").append(LN);
		DecimalFormat f = new DecimalFormat("#,###");
		List<MemoryPoolMXBean> mpmxbs = ManagementFactory.getMemoryPoolMXBeans();
		for (MemoryPoolMXBean mpmxb : mpmxbs) {
			if (mpmxb == null || mpmxb.getUsage() == null) {
				continue;
			}
			String name = mpmxb.getName();
			if (name == null || name.isEmpty()) {
				continue;
			}
			sb.append(name).append(BLACK).append(f.format(mpmxb.getUsage().getInit())).append(BLACK)
					.append(f.format(mpmxb.getUsage().getMax())).append(BLACK)
					.append(f.format(mpmxb.getUsage().getCommitted())).append(BLACK)
					.append(f.format(mpmxb.getUsage().getUsed())).append(LN);
		}
		return sb.toString();
	}

	public static String allTrack() {
		Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
		StringBuilder sb = new StringBuilder();
		map.forEach((t, st) -> {
			sb.append(t.getName() + "  [id:" + t.getId() + "]").append(LN);
			for (StackTraceElement e : st) {
				if (sb.length() > 0) {
					sb.append(BLACK);
				}
				sb.append(e.getClassName() + "." + e.getMethodName() + "() ");
				if (e.getLineNumber() > 0) {
					sb.append(e.getLineNumber());
				}
				sb.append(LN);
			}
			sb.append(LN);
		});
		return sb.toString();
	}

	public static String threadPoolInfo(ThreadPoolExecutor pool) {
		StringBuilder sb = new StringBuilder();
		sb.append("active").append(BLACK).append(pool.getActiveCount()).append(BLACK).append("size").append(BLACK)
				.append(pool.getPoolSize()).append(BLACK).append("queue").append(BLACK).append(pool.getQueue().size())
				.append(BLACK).append(LN).append("max").append(BLACK).append(pool.getMaximumPoolSize()).append(BLACK)
				.append("keepAlive(ms)").append(BLACK).append(pool.getKeepAliveTime(TimeUnit.MILLISECONDS))
				.append(BLACK)

				.append("completed*").append(BLACK).append(pool.getCompletedTaskCount());
		return sb.toString();
	}

	public static List<String> beans() {
		Collection<Object> beans = InnerIOC.beans();
		List<String> names = new ArrayList<>();
		for (Object obj : beans) {
			names.add(obj.getClass().getName());
		}
		Collections.sort(names);
		return names;
	}

	public static String logLevels() {
		Map<String, LogLevel> map = new TreeMap<>(Loggers.currentLevels());
		StringBuilder sb = new StringBuilder("#logLevels:").append(LN);
		char[] black = new char[7];
		Arrays.fill(black, ' ');
		map.forEach((k, v) -> {
			sb.append(v).append(black, 0, black.length - v.name().length()).append(k).append(LN);
		});
		return sb.toString();
	}
}
