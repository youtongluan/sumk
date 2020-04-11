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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.yx.annotation.Bean;
import org.yx.bean.IOC;
import org.yx.bean.Plugin;
import org.yx.common.Lifecycle;
import org.yx.common.StartConstants;
import org.yx.common.StartContext;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.http.act.HttpActions;
import org.yx.http.handler.HttpHandler;
import org.yx.http.handler.HttpHandlerChain;
import org.yx.http.handler.RestType;
import org.yx.http.invoke.WebHandler;
import org.yx.http.kit.HttpSettings;
import org.yx.http.start.WebAnnotationResolver;
import org.yx.http.user.WebSessions;
import org.yx.log.Logs;
import org.yx.main.SumkServer;
import org.yx.util.ExceptionUtil;
import org.yx.util.StringUtil;

@Bean
public class HttpPlugin implements Plugin {

	protected Lifecycle server;

	public static final String KEY_STORE_PATH = "sumk.jetty.ssl.keyStore";

	private static final String HTTP_SERVER_CLASS = "org.yx.http.start.JettyServer";
	private static final String HTTPS_SERVER_CLASS = "org.yx.http.start.JettyHttpsServer";

	@Override
	public void stop() {
		if (this.server != null) {
			server.stop();
			this.server = null;
		}
	}

	@Override
	public int order() {
		return 10100;
	}

	protected void resolveWebAnnotation(List<Object> beans) {
		WebAnnotationResolver factory = new WebAnnotationResolver();
		try {
			for (Object bean : beans) {
				factory.resolve(bean);
			}
		} catch (Exception e) {
			throw SumkException.create(e);
		}
	}

	@Override
	public void startAsync() {
		int port = StartContext.httpPort();
		if (!this.isHttpEnable() || port < 1) {
			return;
		}
		try {
			HttpHeaderName.init();
			HttpSettings.init();
			HttpActions.init();
			List<Object> beans = StartContext.inst().getBeans();
			resolveWebAnnotation(beans);
			WebHandler.init();
			this.addFusingObserver();
			this.buildHttpHandlers();
			this.initServer(port);
		} catch (Exception e) {
			throw ExceptionUtil.toRuntimeException(e);
		}
	}

	protected void initServer(int port) throws Exception {
		String nojetty = StartConstants.NOJETTY;
		if (StartContext.inst().get(nojetty) != null || AppInfo.getBoolean(nojetty, false)) {
			return;
		}
		String httpServerClass = StringUtil.isEmpty(AppInfo.get(KEY_STORE_PATH)) ? HTTP_SERVER_CLASS
				: HTTPS_SERVER_CLASS;
		String hs = AppInfo.get("sumk.http.starter.class", httpServerClass);
		if (!hs.contains(".")) {
			return;
		}
		Class<?> httpClz = Class.forName(hs);
		Constructor<?> c = httpClz.getConstructor(int.class);
		this.server = (Lifecycle) c.newInstance(port);
	}

	protected void buildHttpHandlers() {
		List<HttpHandler> handlers = IOC.getBeans(HttpHandler.class);
		List<HttpHandler> restHandlers = new ArrayList<>(handlers.size());
		List<HttpHandler> uploadHandlers = new ArrayList<>(handlers.size());
		for (HttpHandler h : handlers) {
			if (h.supportRestType(RestType.PLAIN)) {
				restHandlers.add(h);
			}
			if (h.supportRestType(RestType.UPLOAD)) {
				uploadHandlers.add(h);
			}
		}
		HttpHandlerChain.inst.setHandlers(restHandlers);
		Logger logger = Logs.http();
		if (logger.isDebugEnabled()) {
			logger.debug("rest  handlers:{}", this.buildString(restHandlers));
		}
		if (HttpSettings.isUploadEnable()) {
			HttpHandlerChain.upload.setHandlers(uploadHandlers);
			if (logger.isDebugEnabled()) {
				logger.debug("upload handlers:{}", this.buildString(uploadHandlers));
			}
		}
	}

	protected boolean isHttpEnable() {
		if (!SumkServer.isHttpEnable()) {
			return false;
		}
		try {
			Class.forName("javax.servlet.http.HttpServlet");
		} catch (Exception e) {
			Logs.http().error("javax-servlet-api-**.jar is not imported");
			return false;
		}
		return true;
	}

	protected void addFusingObserver() {
		AppInfo.addObserver(info -> {
			HttpSettings.setCookieEnable(AppInfo.getBoolean("sumk.http.header.usecookie", true));
			HttpSettings.setHttpSessionTimeoutInMs(1000L * AppInfo.getInt("sumk.http.session.timeout", 60 * 30));
			String fusing = AppInfo.get("sumk.http.fusing", null);
			if (fusing == null) {
				HttpSettings.setFusing(Collections.emptySet());
			} else {
				Set<String> set = new HashSet<>();
				String[] fs = StringUtil.toLatin(fusing).split(",");
				for (String f : fs) {
					set.add(f.trim());
				}
				HttpSettings.setFusing(set);
			}
		});
	}

	@Override
	public void afterStarted() {
		Lifecycle server = this.server;
		if (!SumkServer.isHttpEnable() || server == null) {
			return;
		}
		WebSessions.initSession();
		server.start();
	}

	protected String buildString(List<HttpHandler> hs) {
		StringBuilder sb = new StringBuilder();
		for (HttpHandler h : hs) {
			sb.append(" ").append(h.getClass().getSimpleName()).append("(").append(h.order()).append(")");
		}
		return sb.toString();
	}

}
