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
package org.yx.bean.watcher;

import java.lang.reflect.Constructor;

import org.yx.bean.Bean;
import org.yx.bean.IOC;
import org.yx.bean.Plugin;
import org.yx.common.StartConstants;
import org.yx.common.StartContext;
import org.yx.conf.AppInfo;
import org.yx.http.HttpSettings;
import org.yx.http.IntfHandlerFactorysBean;
import org.yx.http.UploadHandlerFactorysBean;
import org.yx.http.handler.HttpHandlerChain;
import org.yx.log.Log;
import org.yx.main.SumkServer;

@Bean
public class HttpServer implements Plugin {

	private Plugin server;

	@Override
	public void start() {
		if (!SumkServer.isHttpEnable()) {
			return;
		}
		try {
			HttpHandlerChain.inst.setHandlers(IOC.get(IntfHandlerFactorysBean.class).create());
			if (HttpSettings.isUploadEnable()) {
				HttpHandlerChain.upload.setHandlers(IOC.get(UploadHandlerFactorysBean.class).create());
			}
			int port = AppInfo.getInt(StartConstants.HTTP_PORT, -1);
			if (port < 1) {
				return;
			}
			String nojetty = "sumk.http.nojetty";
			if (StartContext.inst.get(nojetty) != null || AppInfo.getBoolean(nojetty, false)) {
				return;
			}
			String hs = AppInfo.get("http.starter.class", "org.yx.http.start.JettyServer");
			if (!hs.contains(".")) {
				return;
			}
			Class<?> httpClz = Class.forName(hs);
			Constructor<?> c = httpClz.getConstructor(int.class);
			server = (Plugin) c.newInstance(port);
			server.start();
		} catch (Exception e) {
			Log.printStack(e);
			System.exit(-1);
		}

	}

	@Override
	public void stop() {
		if (this.server != null) {
			server.stop();
		}
	}

	@Override
	public int order() {
		return 10000;
	}

}
