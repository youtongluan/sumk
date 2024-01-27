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
package org.yx.http.handler;

import java.util.List;

import org.slf4j.Logger;
import org.yx.log.Log;

public final class HttpHandlerChain {

	public static final HttpHandlerChain rest = new HttpHandlerChain();
	public static final HttpHandlerChain multipart = new HttpHandlerChain();

	private Logger LOG = Log.get("sumk.http.chain");
	private HttpHandler[] handlers;

	public void setHandlers(List<HttpHandler> handlers) {
		this.handlers = handlers.toArray(new HttpHandler[handlers.size()]);
	}

	public void handle(WebContext ctx) throws Throwable {
		for (HttpHandler h : this.handlers) {
			if (h.order() < ctx.getLowestOrder()) {
				continue;
			}
			if (LOG.isTraceEnabled()) {
				if (ctx.data() instanceof String) {
					String s = ((String) ctx.data());
					LOG.trace("{} - {} with data:{}", ctx.rawAct(), h.getClass().getSimpleName(), s);
				}
			}
			h.handle(ctx);
		}
	}
}
