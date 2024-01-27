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
package org.yx.http.monitor;

import static org.yx.common.monitor.Monitors.BLANK;

import static org.yx.conf.AppInfo.LN;

import java.lang.management.GarbageCollectorMXBean;
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
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.yx.base.matcher.BooleanMatcher;
import org.yx.base.matcher.Matchers;
import org.yx.bean.InnerIOC;
import org.yx.bean.NameSlot;
import org.yx.conf.AppInfo;
import org.yx.conf.Const;
import org.yx.log.LogLevel;
import org.yx.log.Loggers;
import org.yx.main.SumkServer;
import org.yx.util.SumkDate;

public final class HttpMonitors {

	public static String serverInfo() {
		long startTime = SumkServer.startTime();
		long now = System.currentTimeMillis();
		long ms = now - startTime;
		StringBuilder sb = new StringBuilder();
		sb.append("start").append(BLANK).append(SumkDate.of(startTime).to_yyyy_MM_dd_HH_mm_ss_SSS().replace(" ", "T"))
				.append(BLANK).append("run(ms)").append(BLANK).append(ms).append(LN).append("localip").append(BLANK)
				.append(AppInfo.getLocalIp()).append(BLANK).append("pid").append(BLANK).append(AppInfo.pid()).append(LN)
				.append("framework").append(BLANK).append(Const.sumkVersion());
		String v = AppInfo.appId(null);
		if (v != null) {
			sb.append(BLANK).append("appId").append(BLANK).append(v);
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
			sb.append(name).append(BLANK).append(f.format(mpmxb.getUsage().getInit())).append(BLANK)
					.append(f.format(mpmxb.getUsage().getMax())).append(BLANK)
					.append(f.format(mpmxb.getUsage().getCommitted())).append(BLANK)
					.append(f.format(mpmxb.getUsage().getUsed())).append(LN);
		}
		return sb.toString();
	}

	public static String stack(boolean full) {
		Predicate<String> ignore = full ? BooleanMatcher.FALSE
				: Matchers.createWildcardMatcher(
						"org.yx.*,java.*,javax.*,sun.*,org.eclipse.jetty.*,org.apache.zookeeper.*,io.netty.*,org.apache.mina.*");
		Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
		StringBuilder all = new StringBuilder(2000).append("线程总数:").append(map.size()).append(LN);
		for (Entry<Thread, StackTraceElement[]> en : map.entrySet()) {
			Thread t = en.getKey();
			StackTraceElement[] st = en.getValue();
			boolean matched = false;
			StringBuilder sb = new StringBuilder(256).append(t.getName()).append("  [id:").append(t.getId()).append("]")
					.append(BLANK).append(t.getState()).append(LN);
			boolean first = true;
			for (StackTraceElement e : st) {
				if (first) {
					first = false;
				} else {
					sb.append(BLANK);
				}
				if (!ignore.test(e.getClassName())) {
					matched = true;
				}
				sb.append(e.getClassName()).append('.').append(e.getMethodName());
				if (e.getLineNumber() > 0) {
					sb.append(" ").append(e.getLineNumber());
				}
				sb.append(LN);
			}
			if (matched) {
				all.append(sb.append(LN));
			}
		}
		return all.toString();
	}

	public static String threadPoolInfo(ThreadPoolExecutor pool) {
		StringBuilder sb = new StringBuilder();
		sb.append("active").append(BLANK).append(pool.getActiveCount()).append(BLANK).append("size").append(BLANK)
				.append(pool.getPoolSize()).append(BLANK).append("queue").append(BLANK).append(pool.getQueue().size())
				.append(BLANK).append(LN).append("max").append(BLANK).append(pool.getMaximumPoolSize()).append(BLANK)
				.append("keepAlive(ms)").append(BLANK).append(pool.getKeepAliveTime(TimeUnit.MILLISECONDS))
				.append(BLANK)

				.append("completed*").append(BLANK).append(pool.getCompletedTaskCount());
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

	public static String beansName() {
		StringBuilder sb = new StringBuilder();
		List<String> names = InnerIOC.beanNames();
		Collections.sort(names);
		for (String name : names) {
			NameSlot slot = InnerIOC.getSlot(name);
			sb.append(slot).append(Const.LN);
		}
		return sb.toString();
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

	public static String sumkDateCacheChangeCount() {
		StringBuilder sb = new StringBuilder();
		sb.append("##sumkDateCached").append(BLANK).append(SumkDate.cacheChangeCount());
		return sb.toString();
	}

	public static String gcInfo() {
		StringBuilder sb = new StringBuilder(64).append("##name").append(BLANK).append("count").append(BLANK)
				.append("time(ms)");
		for (GarbageCollectorMXBean mxBean : ManagementFactory.getGarbageCollectorMXBeans()) {
			sb.append(LN).append(getGcName(mxBean.getName())).append(BLANK).append(mxBean.getCollectionCount())
					.append(BLANK).append(mxBean.getCollectionTime());
		}
		return sb.toString();
	}

	private static String getGcName(String name) {
		if ("PS Scavenge".equals(name) || "ParNew".equals(name) || "G1 Young Generation".equals(name)
				|| "Copy".equals(name)) {
			return "Young";
		}
		if ("PS MarkSweep".equals(name) || "ConcurrentMarkSweep".equals(name) || "G1 Old Generation".equals(name)
				|| "MarkSweepCompact".equals(name)) {
			return "Old";
		}
		return name;
	}

}
