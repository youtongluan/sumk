package org.yx.main;

import java.util.ArrayList;
import java.util.List;

import org.yx.bean.BeanPublisher;
import org.yx.bean.ScanerFactorysBean;
import org.yx.common.StartConstants;
import org.yx.common.StartContext;
import org.yx.conf.AppInfo;
import org.yx.listener.Listener;
import org.yx.log.Log;
import org.yx.util.StringUtils;

public class Bootstrap {
	private static volatile boolean started = false;

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		if (started) {
			return;
		}
		started = true;
		try {
			BeanPublisher.addListeners((List<Listener>) new ScanerFactorysBean().create());
			BeanPublisher.publishBeans(
					allPackage(AppInfo.get(StartConstants.SOA_PACKAGES), AppInfo.get(StartConstants.HTTP_PACKAGES),
							AppInfo.get(StartConstants.IOC_PACKAGES), StartConstants.INNER_PACKAGE));

			StartContext.clear();
		} catch (Throwable e) {
			Log.printStack(e);
			System.exit(-1);
		}
	}

	/**
	 * 将逗号分隔符的字符串，拆分为无逗号的字符串
	 * 
	 * @param ps
	 *            每个字符串都可能含有,
	 * @return
	 */
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
