package org.yx.bean.watcher;

import java.util.ArrayList;
import java.util.List;

import org.yx.common.ServerStarter;
import org.yx.common.StartConstants;
import org.yx.common.StartContext;
import org.yx.conf.AppInfo;
import org.yx.http.ServletInfo;
import org.yx.http.UploadServer;
import org.yx.http.WebServer;
import org.yx.log.Log;
import org.yx.util.StringUtils;

public class HttpRunner implements Runnable {

	@Override
	public void run() {
		try {
			if (StringUtils.isEmpty(AppInfo.get(StartConstants.HTTP_PACKAGES)) || Boolean.getBoolean("nohttp")) {
				return;
			}
			int port = -1;
			try {
				port = Integer.valueOf(AppInfo.get("http.port", "80"));
			} catch (Exception e) {
				Log.get("SYS.45").error("http port {} is not a number");
			}
			if (port < 1) {
				return;
			}
			List<ServletInfo> servlets = (List<ServletInfo>) StartContext.inst.getOrCreate(ServletInfo.class,
					new ArrayList<ServletInfo>());
			servlets.add(new ServletInfo().setServletClz(WebServer.class).setPath("/webserver/*"));
			servlets.add(new ServletInfo().setServletClz(UploadServer.class).setPath("/upload/*"));
			String hs = AppInfo.get("http.starter.class", "org.yx.http.start.HttpStarter");
			Class<?> httpClz = Class.forName(hs);
			ServerStarter httpStarter = (ServerStarter) httpClz.newInstance();
			httpStarter.start(port);
		} catch (Exception e) {
			Log.printStack(e);
			System.exit(-1);
		}

	}

}
