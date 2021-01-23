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
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.yx.bean.IOC;
import org.yx.common.Lifecycle;
import org.yx.conf.AppInfo;
import org.yx.exception.SumkException;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.main.StartContext;
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
				AppInfo.getInt("sumk.jetty.connector.acceptors", -1),
				AppInfo.getInt("sumk.jetty.connector.selectors", -1), getConnectionFactorys()) {
			@Override
			protected ServerSocketChannel openAcceptChannel() throws IOException {
				IOException ex = null;
				try {
					return super.openAcceptChannel();
				} catch (IOException e) {
					ex = e;
				}

				for (int i = 0; i < AppInfo.getInt("sumk.jetty.bind.retry", 100); i++) {
					try {
						Thread.sleep(AppInfo.getLong("sumk.jetty.bind.sleepTime", 2000));
					} catch (InterruptedException e1) {
						Logs.http().error("showdown because of InterruptedException");
						Thread.currentThread().interrupt();
						throw new SumkException(-34534560, "收到中断." + e1);
					}
					Logs.http().warn("{} was occupied({}),begin retry {}", this.getPort(), ex.getMessage(), i);
					try {
						return super.openAcceptChannel();
					} catch (IOException e) {
						if (isInheritChannel()) {
							throw e;
						}
						ex = e;
					}
				}
				throw ex;
			}
		};
		connector.setReuseAddress(AppInfo.getBoolean("sumk.jetty.reuseAddr", false));
		int backlog = AppInfo.getInt("sumk.jetty.backlog", 0);
		if (backlog > 0) {
			connector.setAcceptQueueSize(backlog);
		}
		return connector;
	}

	protected synchronized void init() {
		try {
			buildJettyProperties();
			server = new Server(new ExecutorThreadPool(StartContext.inst().getHttpExecutor()));
			ServerConnector connector = this.createConnector();
			Logs.http().info("listen at port: {}", port);
			String host = StartContext.httpHost();
			if (host != null && host.length() > 0) {
				connector.setHost(host);
			}
			connector.setPort(port);

			server.setConnectors(new Connector[] { connector });
			ServletContextHandler context = createServletContextHandler();
			context.setContextPath(AppInfo.get("sumk.jetty.web.root", "/"));
			context.addEventListener(new SumkLoaderListener());
			addUserListener(context, Arrays.asList(ServletContextListener.class, ContextScopeListener.class));
			String resourcePath = AppInfo.get("sumk.jetty.resource");
			if (StringUtil.isNotEmpty(resourcePath)) {
				ResourceHandler resourceHandler = JettyHandlerSupplier.resourceHandlerSupplier().get();
				if (resourceHandler != null) {
					resourceHandler.setResourceBase(resourcePath);
					context.insertHandler(resourceHandler);
				}
			}

			if (AppInfo.getBoolean("sumk.jetty.session.enable", false)) {
				SessionHandler h = JettyHandlerSupplier.sessionHandlerSupplier().get();
				if (h != null) {
					context.insertHandler(h);
				}
			}
			addUserHandlers(context);
			server.setHandler(context);
		} catch (Throwable e) {
			Logs.http().error(e.getLocalizedMessage(), e);
			StartContext.startFail();
		}

	}

	protected void addUserHandlers(ServletContextHandler context) {
		Object obj = StartContext.inst().get(HandlerWrapper.class);
		if (obj == null) {
			return;
		}
		if (HandlerWrapper[].class.isInstance(obj)) {
			HandlerWrapper[] hs = (HandlerWrapper[]) obj;
			for (HandlerWrapper h : hs) {
				Logs.http().info("add jetty handler {}", h);
				context.insertHandler(h);
			}
		}
	}

	protected void buildJettyProperties() {
		String key = "org.eclipse.jetty.server.Request.maxFormContentSize";
		String v = AppInfo.get(key);
		if (v != null && v.length() > 0 && System.getProperty(key) == null) {
			System.setProperty(key, v);
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
			Logs.http().error(e.getLocalizedMessage(), e);
			throw new SumkException(234234, "jetty启动失败");
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
		String welcomes = AppInfo.get("sumk.jetty.welcomes");
		if (welcomes != null && welcomes.length() > 0) {
			handler.setWelcomeFiles(welcomes.replace('，', ',').split(","));
		}
		GzipHandler gzipHandler = JettyHandlerSupplier.gzipHandlerSupplier().get();
		if (gzipHandler != null) {
			handler.setGzipHandler(gzipHandler);
		}
		return handler;
	}

	private void addUserListener(ServletContextHandler context, List<Class<? extends EventListener>> intfs) {
		for (Class<? extends EventListener> intf : intfs) {
			@SuppressWarnings("unchecked")
			List<EventListener> listeners = (List<EventListener>) IOC.getBeans(intf);
			if (CollectionUtil.isEmpty(listeners)) {
				continue;
			}
			for (EventListener lis : listeners) {
				context.addEventListener(lis);
			}
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
