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
import java.util.Collection;

import org.yx.annotation.Bean;
import org.yx.bean.InnerIOC;
import org.yx.bean.Loader;
import org.yx.bean.Plugin;
import org.yx.common.Lifecycle;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.main.StartContext;
import org.yx.main.SumkServer;
import org.yx.rpc.RpcSettings;
import org.yx.rpc.server.impl.RpcHandler;
import org.yx.rpc.server.start.SoaAnnotationResolver;

@Bean
public class SoaPlugin implements Plugin {

	protected Lifecycle server;

	@Override
	public void startAsync() {
		int port = StartContext.soaPort();

		if (!SumkServer.isRpcEnable() || port < 0) {
			return;
		}
		try {
			RpcSettings.init();
			resolveSoaAnnotation(InnerIOC.beans());
			RpcHandler.init();

			String clzName = AppInfo.get("sumk.rpc.starter.class", "org.yx.rpc.server.start.SoaServer");
			Class<?> clz = Loader.loadClassExactly(clzName);
			Constructor<?> c = clz.getConstructor(int.class);
			server = (Lifecycle) c.newInstance(port);
		} catch (Throwable e) {
			Log.printStack("sumk.error", e);
			throw new SumkException(-35345436, "rpc服务启动失败");
		}
	}

	protected void resolveSoaAnnotation(Collection<Object> beans) {
		SoaAnnotationResolver factory = new SoaAnnotationResolver();
		try {
			for (Object bean : beans) {
				factory.resolve(bean);
			}
		} catch (Exception e) {
			throw SumkException.wrap(e);
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
