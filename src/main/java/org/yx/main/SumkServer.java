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
package org.yx.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yx.bean.BeanPublisher;
import org.yx.bean.IOC;
import org.yx.bean.Plugin;
import org.yx.bean.ScanerFactorysBean;
import org.yx.common.StartConstants;
import org.yx.common.StartContext;
import org.yx.conf.AppInfo;
import org.yx.listener.Listener;
import org.yx.log.Log;
import org.yx.rpc.client.Rpc;
import org.yx.util.StringUtil;

public class SumkServer {
	private static volatile boolean started;
	private static volatile boolean httpEnable;
	private static volatile boolean rpcEnable;

	public static boolean isHttpEnable() {
		return httpEnable;
	}

	public static boolean isRpcEnable() {
		return rpcEnable;
	}

	public static void main(String[] args) {
		start(Arrays.asList(args));
	}

	public static void start() {
		start(new ArrayList<String>());
	}

	public static void start(String... args) {
		Set<String> argSet = new HashSet<>();
		if (args != null && args.length > 0) {
			argSet.addAll(Arrays.asList(args));
		}
		start(argSet);
	}

	@SuppressWarnings("rawtypes")
	public static void start(Collection<String> args) {
		synchronized (SumkServer.class) {
			if (started) {
				return;
			}
			started = true;
		}
		try {
			handleSystemArgs();
			handleArgs(args);
			BeanPublisher.addListeners((List<Listener>) new ScanerFactorysBean().create());
			List<String> ps = new ArrayList<>();
			ps.add(AppInfo.get(StartConstants.IOC_PACKAGES));
			ps.add(AppInfo.get(StartConstants.INNER_PACKAGE));
			if (StartContext.inst.get(StartConstants.NOSOA) == null
					&& AppInfo.getInt(StartConstants.SOA_PORT, -1) > 0) {
				ps.add(AppInfo.get(StartConstants.SOA_PACKAGES));
				rpcEnable = true;
			}
			if (StartContext.inst.get(StartConstants.NOHTTP) == null) {
				ps.add(AppInfo.get(StartConstants.HTTP_PACKAGES));
				httpEnable = true;
			}
			BeanPublisher.publishBeans(allPackage(ps));
			if (AppInfo.getBoolean(StartConstants.SOA_PACKAGES + ".client.start", false)) {
				Rpc.init();
			}

		} catch (Throwable e) {
			e.printStackTrace();
			Log.printStack(e);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			}
			System.exit(-1);
		}
	}

	private static void handleSystemArgs() {
	}

	private static void handleArgs(Collection<String> args) {
		args.forEach(arg -> {
			if (arg.contains("=")) {
				String[] kv = arg.split("=", 2);
				StartContext.inst.put(kv[0], kv[1]);
				return;
			}
			StartContext.inst.put(arg, Boolean.TRUE);
		});

	}

	private static List<String> allPackage(List<String> ps) {
		List<String> list = new ArrayList<String>();
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

	private volatile static boolean destoryed = false;

	/**
	 * 
	 * @return true表示应用已经停止了
	 */
	public static boolean isDestoryed() {
		return destoryed;
	}

	public static void stop() {
		synchronized (SumkServer.class) {
			if (destoryed) {
				return;
			}
			destoryed = true;
		}
		SumkThreadPool.shutdown();
		List<Plugin> lifes = IOC.getBeans(Plugin.class);
		if (lifes == null || lifes.isEmpty()) {
			return;
		}
		Collections.reverse(lifes);
		lifes.forEach(b -> {
			try {
				b.stop();
			} catch (Exception e) {
				Log.printStack(e);
			}
		});
		Log.get("sumk.SYS").info("sumk server stoped!!!");
	}
}
