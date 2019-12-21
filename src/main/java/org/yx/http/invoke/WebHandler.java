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
package org.yx.http.invoke;

import java.util.List;

import org.yx.bean.IOC;
import org.yx.http.WebFilter;
import org.yx.http.handler.WebContext;

public final class WebHandler {

	private static WebFilter filter;
	private static final WebFilter LAST = new WebFilter() {

		private final WebVisitor visitor = new WebVisitorImpl();

		@Override
		public Object doFilter(WebContext ctx) throws Throwable {
			return visitor.visit(ctx);
		}

	};

	public static synchronized void init() {
		if (filter != null) {
			return;
		}
		List<WebFilter> list = IOC.getBeans(WebFilter.class);
		if (list == null || list.isEmpty()) {
			filter = LAST;
			return;
		}
		final int size = list.size();
		for (int i = 0; i < size; i++) {
			WebFilter current = list.get(i);
			if (i == size - 1) {
				current.setNext(LAST);
				break;
			}
			current.setNext(list.get(i + 1));
		}
		filter = list.get(0);
	}

	public static Object handle(WebContext ctx) throws Throwable {
		return filter.doFilter(ctx);
	}

}
