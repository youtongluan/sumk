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

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.yx.conf.AppInfo;
import org.yx.main.SumkServer;
import org.yx.main.SumkThreadPool;
import org.yx.util.SumkDate;

public class Monitors {

	public static final String LN = "\n";

	public static String serverInfo() {
		long startTime = SumkServer.startTime();
		long now = System.currentTimeMillis();
		long ms = now - startTime;
		StringBuilder sb = new StringBuilder();
		sb.append("start at : " + SumkDate.of(startTime).to_yyyy_MM_dd_HH_mm_ss_SSS()).append(LN)
				.append("run(ms) : " + ms).append(LN).append("localip : " + AppInfo.getIp()).append(LN)
				.append("pid : " + AppInfo.pid()).append(LN);
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
			sb.append(name).append("  ").append(f.format(mpmxb.getUsage().getInit())).append("  ")
					.append(f.format(mpmxb.getUsage().getMax())).append("  ")
					.append(f.format(mpmxb.getUsage().getCommitted())).append("  ")
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
					sb.append("  ");
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

	public static String threadPoolInfo() {
		ThreadPoolExecutor pool = (ThreadPoolExecutor) SumkThreadPool.executor();
		StringBuilder sb = new StringBuilder();
		sb.append("active thread : ").append(pool.getActiveCount());
		sb.append(", thread count : " + pool.getPoolSize());
		sb.append(" , queue : " + pool.getQueue().size());
		sb.append(LN);
		sb.append("only for current threads$ commited task : " + pool.getTaskCount());
		sb.append(", completed task : " + pool.getCompletedTaskCount());
		sb.append(LN);
		sb.append("max thread : ").append(pool.getMaximumPoolSize());
		sb.append(", idle timeout(ms) : ").append(pool.getKeepAliveTime(TimeUnit.MILLISECONDS));
		sb.append(LN);
		return sb.toString();
	}
}
