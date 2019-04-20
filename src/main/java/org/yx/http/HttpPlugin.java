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
package org.yx.http;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.yx.annotation.Bean;
import org.yx.bean.IOC;
import org.yx.bean.Loader;
import org.yx.bean.Plugin;
import org.yx.common.Lifecycle;
import org.yx.common.StartContext;
import org.yx.conf.AppInfo;
import org.yx.http.handler.HttpHandlerChain;
import org.yx.log.Log;
import org.yx.main.SumkServer;
import org.yx.util.StringUtil;
import org.yx.util.secury.AESEncry;
import org.yx.util.secury.Encry;
import org.yx.util.secury.FastEncry;

@Bean
public class HttpPlugin implements Plugin {

	private Lifecycle server;

	public static final String KEY_STORE_PATH = "http.ssl.keyStore";

	private static final String HTTP_SERVER_CLASS = "org.yx.http.start.JettyServer";
	private static final String HTTPS_SERVER_CLASS = "org.yx.http.start.JettyHttpsServer";

	public void buildEncry() throws Exception {
		String cipher = AppInfo.get("http.encry.cipher");
		if (cipher == null || cipher.length() == 0) {
			return;
		}
		Encry en = null;
		if ("fast".equalsIgnoreCase(cipher)) {
			en = new FastEncry();
		} else if ("AES".equalsIgnoreCase(cipher)) {
			en = new AESEncry();
		} else {
			Class<?> clz = Loader.loadClass(cipher);
			en = (Encry) clz.newInstance();
		}
		Field f = EncryUtil.class.getDeclaredField("encry");
		f.setAccessible(true);
		f.set(null, en);
		Log.get("sumk.SYS").trace("encry:{}", en);
	}

	@Override
	public void stop() {
		if (this.server != null) {
			server.stop();
			this.server = null;
		}
	}

	@Override
	public int order() {
		return 10010;
	}

	@Override
	public void startAsync() {
		if (!SumkServer.isHttpEnable()) {
			return;
		}
		try {
			this.buildEncry();
			HttpHandlerChain.inst.setHandlers(IOC.get(IntfHandlerFactorysBean.class).create());
			if (HttpSettings.isUploadEnable()) {
				HttpHandlerChain.upload.setHandlers(IOC.get(UploadHandlerFactorysBean.class).create());
			}
			int port = StartContext.httpPort();
			if (port < 1) {
				return;
			}
			String nojetty = "sumk.http.nojetty";
			if (StartContext.inst.get(nojetty) != null || AppInfo.getBoolean(nojetty, false)) {
				return;
			}
			String httpServerClass = StringUtil.isEmpty(AppInfo.get(KEY_STORE_PATH)) ? HTTP_SERVER_CLASS
					: HTTPS_SERVER_CLASS;
			String hs = AppInfo.get("http.starter.class", httpServerClass);
			if (!hs.contains(".")) {
				return;
			}
			Class<?> httpClz = Class.forName(hs);
			Constructor<?> c = httpClz.getConstructor(int.class);
			server = (Lifecycle) c.newInstance(port);
		} catch (Throwable e) {
			Log.printStack(e);
			System.exit(-1);
		}
	}

	@Override
	public void afterStarted() {
		Lifecycle server = this.server;
		if (!SumkServer.isHttpEnable() || server == null) {
			return;
		}
		server.start();
	}

}
