package org.yx.bean.watcher;

import org.yx.bean.IOC;
import org.yx.common.ServerStarter;
import org.yx.common.StartConstants;
import org.yx.conf.AppInfo;
import org.yx.http.IntfHandlerFactorysBean;
import org.yx.http.UploadHandlerFactorysBean;
import org.yx.http.handler.HttpHandlerChain;
import org.yx.log.Log;
import org.yx.util.StringUtils;

public class HttpRunner implements Runnable {

	@Override
	public void run() {
		if (StringUtils.isEmpty(AppInfo.get(StartConstants.HTTP_PACKAGES)) || Boolean.getBoolean("nohttp")) {
			return;
		}
		try {
			HttpHandlerChain.inst.setHandlers(IOC.get(IntfHandlerFactorysBean.class).create());
			HttpHandlerChain.upload.setHandlers(IOC.get(UploadHandlerFactorysBean.class).create());
			int port = Integer.valueOf(AppInfo.get("http.port", "80"));
			if (port < 1) {
				return;
			}
			String hs = AppInfo.get("http.starter.class", "org.yx.http.start.HttpStarter");
			if (!hs.contains(".")) {
				return;
			}
			Class<?> httpClz = Class.forName(hs);
			ServerStarter httpStarter = (ServerStarter) httpClz.newInstance();
			httpStarter.start(port);
		} catch (Exception e) {
			Log.printStack(e);
			System.exit(-1);
		}

	}

}
