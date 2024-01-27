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

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.MultipartConfig;

import org.yx.bean.BeanKit;
import org.yx.bean.IOC;
import org.yx.conf.AppInfo;
import org.yx.http.kit.HttpSettings;
import org.yx.http.spec.HttpSpecs;
import org.yx.http.spec.SumkFilterSpec;
import org.yx.http.spec.SumkServletSpec;
import org.yx.http.user.HttpLoginWrapper;
import org.yx.http.user.LoginServlet;
import org.yx.log.Log;
import org.yx.log.Logs;
import org.yx.main.StartConstants;
import org.yx.main.SumkServer;
import org.yx.util.CollectionUtil;
import org.yx.util.Loader;
import org.yx.util.StringUtil;

/**
 * 如果使用tomcat等外部容器启动sumk，需要将sumk.webserver.disable设为1，并在web.xml中添加：<BR>
 * &lt;listener&gt;<br>
 * &nbsp;&nbsp;
 * &lt;listener-class&gt;org.yx.main.SumkLoaderListener&lt;/listener-class&gt;
 * <br>
 * &lt;/listener&gt;
 * 
 * @author 游夏
 *
 */
public class SumkLoaderListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Logs.http().debug("contextInitialized");
		try {
			SumkServer.start(Collections.singleton(StartConstants.EMBED_WEBSERVER_DISABLE));
			if (!SumkServer.isHttpEnable()) {
				return;
			}
			ServletContext context = sce.getServletContext();
			addFilters(context);
			addServlets(context);

			String path = AppInfo.get("sumk.http.login.path", "/login/*");
			if (IOC.getBeans(LoginServlet.class).size() > 0) {
				String loginPath = path;
				if (!loginPath.startsWith("/")) {
					loginPath = "/" + loginPath;
				}
				Logs.http().info("login path:{}", context.getContextPath() + loginPath);
				ServletRegistration.Dynamic dynamic = context.addServlet(loginPath, HttpLoginWrapper.class);
				dynamic.addMapping(loginPath);
				dynamic.setLoadOnStartup(1);
			}
			addListeners(context);
		} catch (Exception e) {
			Logs.http().error(e.getMessage(), e);
			throw e;
		}
	}

	private void addServlets(ServletContext context) {
		List<Servlet> servlets = IOC.getBeans(Servlet.class);
		for (Servlet bean : servlets) {
			Class<?> targetClz = BeanKit.getTargetClass(bean);
			SumkServletSpec sumk = HttpSpecs.extractSumkServlet(targetClz);
			if (sumk == null) {
				continue;
			}
			String name = sumk.name();
			if (name == null || name.isEmpty()) {
				name = bean.getClass().getSimpleName();
			}
			ServletRegistration.Dynamic dynamic = context.addServlet(name, bean);
			dynamic.setAsyncSupported(sumk.asyncSupported());
			dynamic.setLoadOnStartup(sumk.loadOnStartup());
			String[] values = sumk.path();
			String value = null;
			if (StringUtil.isNotEmpty(sumk.appKey())
					&& (value = AppInfo.get("sumk.http.servlet." + sumk.appKey())) != null) {
				values = StringUtil.toLatin(value).split(",");
			}
			dynamic.addMapping(values);

			if (HttpSettings.isUploadEnable()) {
				this.handleUpload(dynamic, targetClz, values);
			}
			Logs.http().trace("add web mapping : {} - {} -{}", name, bean.getClass().getSimpleName(),
					Arrays.toString(values));
		}
	}

	private void handleUpload(ServletRegistration.Dynamic dynamic, Class<?> targetClz, String[] paths) {
		try {
			MultipartConfig mc = targetClz.getAnnotation(MultipartConfig.class);
			if (mc == null) {
				return;
			}
			String location = mc.location();
			if (StringUtil.isEmpty(location)) {
				location = AppInfo.get("sumk.http.multipart.location", null);
				if (location == null) {
					Path p = Files.createTempDirectory("multi");
					location = p.toFile().getAbsolutePath();
				}
			}
			long maxFileSize = mc.maxFileSize() > 0 ? mc.maxFileSize()
					: AppInfo.getLong("sumk.http.multipart.maxFileSize", 1024L * 1024 * 10);
			long maxRequestSize = mc.maxRequestSize() > 0 ? mc.maxRequestSize()
					: AppInfo.getLong("sumk.http.multipart.maxRequestSize", 1024L * 1024 * 50);

			int fileSizeThreshold = mc.fileSizeThreshold() > 0 ? mc.fileSizeThreshold()
					: AppInfo.getInt("sumk.http.multipart.fileSizeThreshold", 1024 * 10);
			MultipartConfigElement c = new MultipartConfigElement(location, maxFileSize, maxRequestSize,
					fileSizeThreshold);
			dynamic.setMultipartConfig(c);
			if (Logs.http().isInfoEnabled()) {
				Logs.http().info("{} location={},maxFileSize={},maxRequestSize={},fileSizeThreshold={}",
						Arrays.toString(paths), location, maxFileSize, maxRequestSize, fileSizeThreshold);
			}
		} catch (Throwable e) {
			Logs.http().warn("不支持文件上传!!!", e);
		}
	}

	private void addFilters(ServletContext context) {
		List<Filter> filters = IOC.getBeans(Filter.class);
		if (org.yx.util.CollectionUtil.isEmpty(filters)) {
			return;
		}
		for (Filter bean : filters) {
			Class<?> targetClz = BeanKit.getTargetClass(bean);
			SumkFilterSpec sumk = HttpSpecs.extractSumkFilter(targetClz);
			if (sumk == null) {
				continue;
			}
			String name = sumk.name();
			if (name.isEmpty()) {
				name = bean.getClass().getSimpleName();
			}
			Logs.http().trace("add web filter : {} - {}", name, bean.getClass().getSimpleName());
			FilterRegistration.Dynamic r = context.addFilter(name, bean);
			r.setAsyncSupported(sumk.asyncSupported());
			DispatcherType[] type = sumk.dispatcherType();
			EnumSet<DispatcherType> types = null;
			if (type.length > 0) {
				types = EnumSet.copyOf(Arrays.asList(type));
			}

			r.addMappingForUrlPatterns(types, sumk.isMatchAfter(), sumk.path());
		}
	}

	private static InputStream openResourceAsStream(String name) {
		InputStream in = Loader.getResourceAsStream(name + "-impl");
		if (in != null) {
			return in;
		}
		return Loader.getResourceAsStream(name);
	}

	private void addListeners(ServletContext context) {
		try {
			InputStream in = openResourceAsStream("META-INF/http/listeners");
			addListener(context, CollectionUtil.loadList(in));
		} catch (Exception e) {
			Log.printStack("sumk.error", e);
			return;
		}
	}

	private void addListener(ServletContext context, List<String> intfs) throws ClassNotFoundException {
		for (String intf : intfs) {
			Class<?> clz = Loader.loadClass(intf);
			if (!EventListener.class.isAssignableFrom(clz)) {
				Logs.http().info(intf + " is not implement EventListener");
				continue;
			}
			@SuppressWarnings("unchecked")
			List<EventListener> listeners = (List<EventListener>) IOC.getBeans(clz);
			if (org.yx.util.CollectionUtil.isEmpty(listeners)) {
				continue;
			}
			for (EventListener lis : listeners) {
				Logs.http().trace("add web listener:{}", lis.getClass().getSimpleName());

				context.addListener(lis);
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		SumkServer.stop();
	}

}
