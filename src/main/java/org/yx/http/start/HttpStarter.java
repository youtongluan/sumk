package org.yx.http.start;

import java.util.concurrent.LinkedBlockingDeque;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.yx.common.ServerStarter;
import org.yx.common.StartContext;
import org.yx.conf.AppInfo;
import org.yx.http.UploadServer;
import org.yx.http.WebServer;
import org.yx.http.filter.HttpLoginWrapper;
import org.yx.http.handler.AesDecodeHandler;
import org.yx.http.handler.AesEncodeHandler;
import org.yx.http.handler.Base64DecodeHandler;
import org.yx.http.handler.Base64EncodeHandler;
import org.yx.http.handler.HttpHandlerChain;
import org.yx.http.handler.InvokeHandler;
import org.yx.http.handler.ReqBodyHandler;
import org.yx.http.handler.ReqHeaderHandler;
import org.yx.http.handler.ReqToStringHandler;
import org.yx.http.handler.ReqUserHandler;
import org.yx.http.handler.RespBodyHandler;
import org.yx.http.handler.RespHeaderHandler;
import org.yx.http.handler.RespToStringHandler;
import org.yx.http.handler.SignValidateHandler;
import org.yx.http.handler.ToByteHandler;
import org.yx.http.handler.UploadHandler;
import org.yx.log.Log;

public class HttpStarter implements ServerStarter{
	
	public void start(int port) throws Exception {
		initHandlers();
		QueuedThreadPool pool = new QueuedThreadPool(200, 8, 60000,
				new LinkedBlockingDeque<Runnable>(1000));
		Server server = new Server(pool);
		ServerConnector connector = new ServerConnector(server, null, null,
				null, 0, 5, new HttpConnectionFactory());
		Log.get("HttpServer").info("listen port："+port);
		String host=AppInfo.get("http.host");
		if(host!=null && host.length()>0){
			connector.setHost(host);
		}
		connector.setPort(port);
		connector.setReuseAddress(true);
		server.setConnectors(new Connector[] { connector });
		ServletContextHandler context = new ServletContextHandler();
		context.setContextPath(AppInfo.get("http.web.root","/intf"));
		context.addServlet(WebServer.class, "/webserver/*");
		context.addServlet(UploadServer.class, "/upload/*");
		Object path=StartContext.inst.map.get().get(StartContext.HTTP_LOGIN_PATH);
		if(path!=null && String.class.isInstance(path)){
			String loginPath=(String)path;
			if(!loginPath.startsWith("/")){
				loginPath="/"+loginPath;
			}
			Log.get("http").info("login path:{}",context.getContextPath()+loginPath);
			context.addServlet(HttpLoginWrapper.class, loginPath);
		}
		server.setHandler(context);
		server.start();
	}
	
	private void initHandlers() {
		ReqUserHandler userHandler=new ReqUserHandler();
		
		HttpHandlerChain chain=HttpHandlerChain.inst;
		chain.addHandler(new ReqHeaderHandler());
		chain.addHandler(new ReqBodyHandler());
		chain.addHandler(userHandler);
		chain.addHandler(new Base64DecodeHandler());
		chain.addHandler(new AesDecodeHandler());
		chain.addHandler(new ReqToStringHandler());
		chain.addHandler(new SignValidateHandler());
		chain.addHandler(new InvokeHandler());
		chain.addHandler(new RespToStringHandler());
		chain.addHandler(new ToByteHandler());
		chain.addHandler(new AesEncodeHandler());
		chain.addHandler(new Base64EncodeHandler());
		chain.addHandler(new RespHeaderHandler());
		chain.addHandler(new RespBodyHandler());
		
		chain=HttpHandlerChain.upload;
		chain.addHandler(new ReqHeaderHandler());
		chain.addHandler(new UploadHandler());
		chain.addHandler(userHandler);
		chain.addHandler(new Base64DecodeHandler());
		chain.addHandler(new AesDecodeHandler());
		chain.addHandler(new ReqToStringHandler());
		chain.addHandler(new SignValidateHandler());
		chain.addHandler(new InvokeHandler());
		chain.addHandler(new RespToStringHandler());
		chain.addHandler(new ToByteHandler());
		chain.addHandler(new AesEncodeHandler());
		chain.addHandler(new Base64EncodeHandler());
		chain.addHandler(new RespHeaderHandler());
		chain.addHandler(new RespBodyHandler());
	}
}
