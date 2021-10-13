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

import java.util.Objects;
import java.util.function.Supplier;

import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.yx.conf.AppInfo;

public class JettyHandlerSupplier {
	private static Supplier<GzipHandler> gzipHandlerSupplier = () -> {
		GzipHandler h = new GzipHandler();
		h.addIncludedMethods("POST");
		h.setMinGzipSize(AppInfo.getInt("sumk.webserver.gzip.minsize", 1000));
		return h;
	};

	private static Supplier<ResourceHandler> resourceHandlerSupplier = () -> {
		ResourceHandler handler = new ResourceHandler();
		String welcomes = AppInfo.get("sumk.webserver.resource.welcomes");
		if (welcomes != null && welcomes.length() > 0) {
			handler.setWelcomeFiles(welcomes.replace('，', ',').split(","));
		}
		return handler;
	};

	private static Supplier<SessionHandler> sessionHandlerSupplier = SessionHandler::new;

	public static Supplier<GzipHandler> gzipHandlerSupplier() {
		return gzipHandlerSupplier;
	}

	public static void setGzipHandlerSupplier(Supplier<GzipHandler> h) {
		JettyHandlerSupplier.gzipHandlerSupplier = Objects.requireNonNull(h);
	}

	public static Supplier<ResourceHandler> resourceHandlerSupplier() {
		return resourceHandlerSupplier;
	}

	public static void setResourceHandlerSupplier(Supplier<ResourceHandler> h) {
		JettyHandlerSupplier.resourceHandlerSupplier = Objects.requireNonNull(h);
	}

	public static Supplier<SessionHandler> sessionHandlerSupplier() {
		return sessionHandlerSupplier;
	}

	public static void setSessionHandlerSupplier(Supplier<SessionHandler> h) {
		JettyHandlerSupplier.sessionHandlerSupplier = Objects.requireNonNull(h);
	}

}
