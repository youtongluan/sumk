package org.yx.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yx.bean.BeanPublisher;
import org.yx.bean.ScanerFactorysBean;
import org.yx.common.StartConstants;
import org.yx.common.StartContext;
import org.yx.conf.AppInfo;
import org.yx.listener.Listener;
import org.yx.log.Log;
import org.yx.log.LogType;
import org.yx.util.StringUtils;

public class SumkServer {
	private static volatile boolean started = false;

	public static void main(String[] args) {
		start(args);
	}

	public static void start() {
		start(new ArrayList<String>());
	}

	public static void start(String[] args) {
		Set<String> argSet = new HashSet<>();
		if (args != null && args.length > 0) {
			argSet.addAll(Arrays.asList(args));
		}
		start(argSet);
	}

	@SuppressWarnings("rawtypes")
	public static synchronized void start(Collection<String> args) {
		if (started) {
			return;
		}
		started = true;
		try {
			handleSystemArgs();
			handleArgs(args);
			BeanPublisher.addListeners((List<Listener>) new ScanerFactorysBean().create());
			BeanPublisher.publishBeans(
					allPackage(AppInfo.get(StartConstants.SOA_PACKAGES), AppInfo.get(StartConstants.HTTP_PACKAGES),
							AppInfo.get(StartConstants.IOC_PACKAGES), StartConstants.INNER_PACKAGE));

			StartContext.clear();
		} catch (Throwable e) {
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
		if (args.contains("slf4j")) {
			Log.setLogType(LogType.slf4j);
		}

	}

	private static String[] allPackage(String... ps) {
		List<String> list = new ArrayList<String>();
		for (String p : ps) {
			if (StringUtils.isEmpty(p)) {
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
		return list.toArray(new String[list.size()]);
	}
}
