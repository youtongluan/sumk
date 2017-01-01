package org.yx.http.start;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.yx.common.ServerStarter;
import org.yx.common.StartContext;
import org.yx.conf.AppInfo;
import org.yx.http.ServletInfo;
import org.yx.http.filter.HttpLoginWrapper;
import org.yx.http.filter.LoginServlet;
import org.yx.log.Log;
import org.yx.util.StringUtils;

public class HttpStarter implements ServerStarter {

	public void start(int port) throws Exception {
		QueuedThreadPool pool = new QueuedThreadPool(AppInfo.getInt("http.pool.maxThreads", 200),
				AppInfo.getInt("http.pool.minThreads", 8), AppInfo.getInt("http.pool.idleTimeout", 60000),
				new LinkedBlockingDeque<Runnable>(AppInfo.getInt("http.pool.queues", 1000)));
		Server server = new Server(pool);
		ServerConnector connector = new ServerConnector(server, null, null, null,
				AppInfo.getInt("http.connector.acceptors", 0), AppInfo.getInt("http.connector.selectors", 5),
				new HttpConnectionFactory());
		Log.get("HttpServer").info("listen port：" + port);
		String host = AppInfo.get("http.host");
		if (host != null && host.length() > 0) {
			connector.setHost(host);
		}
		connector.setPort(port);
		connector.setReuseAddress(true);

		server.setConnectors(new Connector[] { connector });
		ServletContextHandler context = new ServletContextHandler();
		context.setContextPath(AppInfo.get("http.web.root", "/intf"));
		@SuppressWarnings("unchecked")
		List<ServletInfo> servlets = (List<ServletInfo>) StartContext.inst.get(ServletInfo.class);
		for (ServletInfo info : servlets) {
			context.addServlet(info.getServletClz(), info.getPath());
		}

		Object path = StartContext.inst.get(LoginServlet.class);
		if (path != null && String.class.isInstance(path)) {
			String loginPath = (String) path;
			if (!loginPath.startsWith("/")) {
				loginPath = "/" + loginPath;
			}
			Log.get("http").info("login path:{}", context.getContextPath() + loginPath);
			context.addServlet(HttpLoginWrapper.class, loginPath);
		}
		String resourcePath = AppInfo.get("http.resource");
		if (StringUtils.isNotEmpty(resourcePath)) {
			ResourceHandler resourceHandler = new ResourceHandler();
			resourceHandler.setResourceBase(resourcePath);
			context.insertHandler(resourceHandler);
		}
		server.setHandler(context);
		server.start();
	}

}
