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
package org.yx.http.start;

import java.io.IOException;
import java.net.BindException;
import java.nio.channels.ServerSocketChannel;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;

import javax.servlet.ServletContextListener;

import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler.ContextScopeListener;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.yx.bean.IOC;
import org.yx.common.Lifecycle;
import org.yx.common.StartContext;
import org.yx.conf.AppInfo;
import org.yx.log.Log;
import org.yx.main.SumkLoaderListener;
import org.yx.util.CollectionUtil;
import org.yx.util.StringUtil;

public class JettyServer implements Lifecycle {

	protected final int port;
	protected Server server;
	private boolean started = false;

	public JettyServer(int port) {
		this.port = port;
		this.init();
	}

	protected ConnectionFactory[] getConnectionFactorys() throws Exception {
		return new ConnectionFactory[] { new HttpConnectionFactory() };
	}

	protected ServerConnector createConnector() throws Exception {

		ServerConnector connector = new ServerConnector(server, null, null, null,
				AppInfo.getInt("http.connector.acceptors", 0), AppInfo.getInt("http.connector.selectors", 5),
				getConnectionFactorys()) {
			@Override
			protected ServerSocketChannel openAcceptChannel() throws IOException {
				IOException ex = null;
				try {
					return super.openAcceptChannel();
				} catch (BindException e) {
					ex = e;
				}

				for (int i = 0; i < AppInfo.getInt("http.bind.retry", 100); i++) {
					try {
						Thread.sleep(AppInfo.getLong("http.bind.sleepTime", 1000));
					} catch (InterruptedException e1) {
						Log.get("sumk.http").error("showdown because of InterruptedException");
						System.exit(-1);
					}
					Log.get("sumk.http").warn("{} was occupied,begin retry {}", this.getPort(), i);
					try {
						return super.openAcceptChannel();
					} catch (BindException e) {
						if (isInheritChannel()) {
							throw e;
						}
						ex = e;
					}
				}
				throw ex;
			}
		};
		connector.setReuseAddress(AppInfo.getBoolean("http.reuseAddr", false));
		int backlog = AppInfo.getInt("http.backlog", 0);
		if (backlog > 0) {
			connector.setAcceptQueueSize(backlog);
		}
		return connector;
	}

	protected synchronized void init() {
		try {
			server = new Server(new ExecutorThreadPool(HttpExcutors.getThreadPool()));
			ServerConnector connector = this.createConnector();
			Log.get("sumk.http").info("listen port: {}", port);
			String host = StartContext.httpHost();
			if (host != null && host.length() > 0) {
				connector.setHost(host);
			}
			connector.setPort(port);

			server.setConnectors(new Connector[] { connector });
			ServletContextHandler context = createServletContextHandler();
			context.setContextPath(AppInfo.get("http.jetty.web.root", "/"));
			context.addEventListener(new SumkLoaderListener());
			addUserListener(context, Arrays.asList(ServletContextListener.class, ContextScopeListener.class));
			String resourcePath = AppInfo.get("http.jetty.resource");
			if (StringUtil.isNotEmpty(resourcePath)) {
				ResourceHandler resourceHandler = createResourceHandler();
				resourceHandler.setResourceBase(resourcePath);
				context.insertHandler(resourceHandler);
			}

			if (AppInfo.getBoolean("http.jetty.session.enable", false)) {
				SessionHandler h = createSessionHandler();
				context.insertHandler(h);
			}
			server.setHandler(context);
		} catch (Throwable e) {
			Log.printStack("sumk.http", e);
			System.exit(-1);
		}

	}

	@Override
	public synchronized void start() {
		if (started || server == null) {
			return;
		}
		try {
			server.start();
			started = true;
		} catch (Throwable e) {
			Log.printStack("sumk.http", e);
			System.exit(-1);
		}

	}

	/**
	 * @return
	 */
	private ServletContextHandler createServletContextHandler() {
		ServletContextHandler handler = IOC.get(ServletContextHandler.class);
		if (handler != null) {
			return handler;
		}
		handler = new ServletContextHandler();
		String welcomes = AppInfo.get("http.jetty.welcomes");
		if (welcomes != null && welcomes.length() > 0) {
			handler.setWelcomeFiles(welcomes.replace('，', ',').split(","));
		}
		GzipHandler gzipHandler = IOC.get(GzipHandler.class);
		if (gzipHandler != null) {
			handler.setGzipHandler(gzipHandler);
		}
		return handler;
	}

	private ResourceHandler createResourceHandler() {
		ResourceHandler handler = IOC.get(ResourceHandler.class);
		if (handler != null) {
			return handler;
		}
		handler = new ResourceHandler();
		String welcomes = AppInfo.get("http.jetty.resource.welcomes");
		if (welcomes != null && welcomes.length() > 0) {
			handler.setWelcomeFiles(welcomes.replace('，', ',').split(","));
		}
		return handler;
	}

	private SessionHandler createSessionHandler() {
		SessionHandler handler = IOC.get(SessionHandler.class);
		if (handler != null) {
			return handler;
		}
		handler = new SessionHandler();
		return handler;
	}

	private void addUserListener(ServletContextHandler context, List<Class<? extends EventListener>> intfs) {
		for (Class<? extends EventListener> intf : intfs) {
			@SuppressWarnings("unchecked")
			List<EventListener> listeners = (List<EventListener>) IOC.getBeans(intf);
			if (CollectionUtil.isEmpty(listeners)) {
				continue;
			}
			listeners.forEach(lis -> context.addEventListener(lis));
		}
	}

	@Override
	public void stop() {
		if (server != null) {
			try {
				server.stop();
				this.started = false;
			} catch (Exception e) {
				Log.printStack("sumk.http", e);
			}
		}
	}

}
