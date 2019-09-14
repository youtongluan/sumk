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
package org.yx.rpc.server;

import java.lang.reflect.Constructor;

import org.yx.annotation.Bean;
import org.yx.bean.Plugin;
import org.yx.common.Lifecycle;
import org.yx.common.StartContext;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.main.SumkServer;

@Bean
public class SoaPlugin implements Plugin {

	private Lifecycle server;

	@Override
	public void startAsync() {
		try {
			if (!SumkServer.isRpcEnable()) {
				return;
			}
			int port = StartContext.soaPort();
			if (port > -1) {
				String clzName = AppInfo.get("soa.starter.class", "org.yx.rpc.server.start.SOAServer");
				Class<?> clz = Class.forName(clzName);
				Constructor<?> c = clz.getConstructor(int.class);
				server = (Lifecycle) c.newInstance(port);
			}
		} catch (Throwable e) {
			Log.printStack("sumk.error", e);
			System.exit(-1);
		}

	}

	@Override
	public void afterStarted() {
		if (server != null) {
			server.start();
		}
	}

	@Override
	public void stop() {
		if (server != null) {
			server.stop();
			server = null;
		}
	}

	@Override
	public int order() {
		return 10000;
	}

}
