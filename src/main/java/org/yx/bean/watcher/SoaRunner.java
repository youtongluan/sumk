package org.yx.bean.watcher;

import org.yx.common.ServerStarter;
import org.yx.common.StartConstants;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.util.StringUtils;

public class SoaRunner implements Runnable {

	@Override
	public void run() {
		try {
			if (StringUtils.isEmpty(AppInfo.get(StartConstants.SOA_PACKAGES)) || Boolean.getBoolean("nosoa")) {
				return;
			}
			int port = -1;
			try {
				port = Integer.parseInt(AppInfo.get("soa.port", "9527"));
			} catch (Exception e) {
				Log.get("SYS.45").error("soa port {} is not a number");
			}
			if (port > 0) {
				String clzName = AppInfo.get("soa.starter.class", "org.yx.rpc.server.start.SOAStarter");
				Class<?> clz = Class.forName(clzName);
				ServerStarter starter = (ServerStarter) clz.newInstance();
				starter.start(port);
			}
		} catch (Exception e) {
			Log.printStack(e);
			System.exit(-1);
		}

	}

}
